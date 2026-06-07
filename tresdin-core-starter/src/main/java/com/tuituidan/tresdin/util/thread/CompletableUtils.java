package com.tuituidan.tresdin.util.thread;

import com.tuituidan.tresdin.consts.TresdinConsts;
import com.tuituidan.tresdin.util.BeanExtUtils;
import com.tuituidan.tresdin.util.StringExtUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * CompletableUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
@Component
@Slf4j
public class CompletableUtils implements ApplicationContextAware {

    /**
     * 任务执行线程池.
     */
    private static final Map<String, ThreadPoolExecutor> THREAD_POOL_EXECUTOR_MAP = new HashMap<>();

    private static final Map<String, ThreadPoolConfig> THREAD_POOL_CONFIG_MAP = new HashMap<>();

    private static final String DEF_KEY = "default";

    private static final int QUEUE_SIZE = 1000;

    private static final Long KEEP_ALIVE_TIME = 10L;

    /**
     * waitAll.
     *
     * @param futures futures
     */
    public static void waitAll(List<CompletableFuture<?>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    /**
     * waitAll.
     *
     * @param futures futures
     */
    public static void waitAll(CompletableFuture<?>... futures) {
        CompletableFuture.allOf(futures).join();
    }

    /**
     * runAsync.
     *
     * @param task task
     * @return Void
     */
    public static CompletableFuture<Void> runAsync(Runnable task) {
        return CompletableFuture.runAsync(wrapTraceId(task), getThreadPoolExecutor(DEF_KEY));
    }

    /**
     * runAsync
     *
     * @param task task
     * @param <T> T
     * @return CompletableFuture
     */
    public static <T> CompletableFuture<T> runAsync(Supplier<T> task) {
        return CompletableFuture.supplyAsync(wrapTraceId(task), getThreadPoolExecutor(DEF_KEY));
    }

    /**
     * runAsync.
     *
     * @param task task
     * @param poolName poolName
     * @return Void
     */
    public static CompletableFuture<Void> runAsync(Runnable task, String poolName) {
        return CompletableFuture.runAsync(wrapTraceId(task), getThreadPoolExecutor(poolName));
    }

    /**
     * runAsync
     *
     * @param task task
     * @param poolName poolName
     * @param <T> T
     * @return CompletableFuture
     */
    public static <T> CompletableFuture<T> runAsync(Supplier<T> task, String poolName) {
        return CompletableFuture.supplyAsync(wrapTraceId(task), getThreadPoolExecutor(poolName));
    }

    /**
     * setApplicationContext.
     *
     * @param applicationContext applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ThreadPoolConfig config = applicationContext.getBean(ThreadPoolConfig.class);
        Map<String, ThreadPoolConfig> configMap = new HashMap<>();
        configMap.put(DEF_KEY, config);
        if (MapUtils.isNotEmpty(config.getExtPool())) {
            configMap.putAll(config.getExtPool());
        }
        ThreadPoolConfig defaultConfig = getDefaultConfig(configMap);
        for (Entry<String, ThreadPoolConfig> entry : configMap.entrySet()) {
            ThreadPoolConfig targetConfig = BeanExtUtils.convert(defaultConfig, ThreadPoolConfig::new);
            BeanExtUtils.copyNotNullProperties(entry.getValue(), targetConfig);
            THREAD_POOL_CONFIG_MAP.put(entry.getKey(), targetConfig);
        }
    }

    private static final Object LOCK = new Object();

    private static ThreadPoolExecutor getThreadPoolExecutor(String poolName) {
        ThreadPoolExecutor executor = THREAD_POOL_EXECUTOR_MAP.get(poolName);
        if (executor != null) {
            return executor;
        }
        synchronized (LOCK) {
            executor = THREAD_POOL_EXECUTOR_MAP.get(poolName);
            if (executor != null) {
                return executor;
            }
            ThreadPoolConfig config = THREAD_POOL_CONFIG_MAP.get(poolName);
            Assert.notNull(config, "线程池配置不存在-" + poolName);
            executor = newThreadPoolExecutor(config);
            THREAD_POOL_EXECUTOR_MAP.put(poolName, executor);
            return executor;
        }
    }

    private static ThreadPoolConfig getDefaultConfig(Map<String, ThreadPoolConfig> configMap) {
        int extCoreCount = 0;
        int nullCoreCount = 0;
        int extMaxCount = 0;
        int nullMaxCount = 0;
        for (ThreadPoolConfig item : configMap.values()) {
            if (item.getCorePoolNum() == null) {
                nullCoreCount++;
            } else {
                extCoreCount += item.getCorePoolNum();
            }
            if (item.getMaxPoolNum() == null) {
                nullMaxCount++;
            } else {
                extMaxCount += item.getMaxPoolNum();
            }
        }
        int basicCoreNum = 2 * Runtime.getRuntime().availableProcessors() + 1;
        int basicMaxNum = 2 * basicCoreNum;
        int corePoolNum = (basicCoreNum - extCoreCount) / (nullCoreCount > 0 ? nullCoreCount : 1);
        int maximumPoolSize = (basicMaxNum - extMaxCount) / (nullMaxCount > 0 ? nullMaxCount : 1);
        ThreadPoolConfig config = new ThreadPoolConfig();
        config.setCorePoolNum(corePoolNum);
        config.setMaxPoolNum(maximumPoolSize);
        config.setKeepAliveTime(KEEP_ALIVE_TIME);
        config.setQueueSize(QUEUE_SIZE);
        config.setThreadNamePrefix("tresdin-thread");
        return config;
    }

    private static ThreadPoolExecutor newThreadPoolExecutor(ThreadPoolConfig config) {
        log.info(StringExtUtils.format("初始化线程池:{},核心线程数:{},最大线程数:{},线程存活时间:{}分钟,队列大小：{}",
                config.getThreadNamePrefix(),
                config.getCorePoolNum(),
                config.getMaxPoolNum(),
                config.getKeepAliveTime(),
                config.getQueueSize()));
        return new ThreadPoolExecutor(config.getCorePoolNum(),
                config.getMaxPoolNum(),
                config.getKeepAliveTime(), TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(config.getQueueSize()),
                new BasicThreadFactory.Builder().namingPattern(config.getThreadNamePrefix() + "-%d").build(),
                CompletableUtils::rejectedExecution);
    }

    private static void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException ex) {
                log.error("线程加入队列失败", ex);
                Thread.currentThread().interrupt();
            }
        }
    }

    private static Runnable wrapTraceId(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            putMdcContextTraceId(contextMap);
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    private static <T> Supplier<T> wrapTraceId(Supplier<T> task) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            putMdcContextTraceId(contextMap);
            try {
                return task.get();
            } finally {
                MDC.clear();
            }
        };
    }

    private static void putMdcContextTraceId(Map<String, String> contextMap) {
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
        if (StringUtils.isBlank(MDC.get(TresdinConsts.TRACE_ID))) {
            MDC.put(TresdinConsts.TRACE_ID, StringExtUtils.getUuid());
        }
    }

}
