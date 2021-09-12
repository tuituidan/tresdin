package com.tuituidan.tresdin.util.thread;

import com.tuituidan.tresdin.util.BeanExtUtils;
import com.tuituidan.tresdin.util.thread.config.ThreadPoolConfigItem;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * CompletableUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
@UtilityClass
@Slf4j
public class CompletableUtils  {

    private static final ThreadPoolConfigItem DEF_CONFIG = new ThreadPoolConfigItem();

    private static final int QUEUE_SIZE = 1000;

    private static final Long KEEP_ALIVE_TIME = 10L;

    static {
        int corePoolNum = 2 * Runtime.getRuntime().availableProcessors() + 1;
        int maximumPoolSize = 2 * corePoolNum;
        DEF_CONFIG.setCorePoolNum(corePoolNum);
        DEF_CONFIG.setMaxPoolNum(maximumPoolSize);
        DEF_CONFIG.setKeepAliveTime(KEEP_ALIVE_TIME);
        DEF_CONFIG.setQueueSize(QUEUE_SIZE);
        DEF_CONFIG.setThreadNamePrefix("tresdin-common-");
    }

    static ThreadPoolExecutor createThreadPoolExecutor(ThreadPoolConfigItem config) {
        ThreadPoolConfigItem targetConfig = BeanExtUtils.convert(DEF_CONFIG, ThreadPoolConfigItem.class);
        if (config != null) {
            BeanExtUtils.copyNotNullProperties(config, targetConfig);
        }
        return createThreadPoolExecutor(targetConfig, new ArrayBlockingQueue<>(targetConfig.getQueueSize()));
    }

    static ThreadPoolExecutor createThreadPoolExecutor(ThreadPoolConfigItem config, BlockingQueue<Runnable> queue) {
        ThreadPoolConfigItem targetConfig = BeanExtUtils.convert(DEF_CONFIG, ThreadPoolConfigItem.class);
        if (config != null) {
            BeanExtUtils.copyNotNullProperties(config, targetConfig);
        }
        return newThreadPoolExecutor(targetConfig, queue);
    }

    private static ThreadPoolExecutor newThreadPoolExecutor(ThreadPoolConfigItem config,
                                                            BlockingQueue<Runnable> queue) {
        return new ThreadPoolExecutor(config.getCorePoolNum(),
                config.getMaxPoolNum(),
                config.getKeepAliveTime(), TimeUnit.SECONDS,
                queue,
                new BasicThreadFactory.Builder().namingPattern(config.getThreadNamePrefix() + "-%d").build(),
                CompletableUtils::rejectedExecution);
    }

    /**
     * waitAll.
     *
     * @param futures futures
     */
    public static void waitAll(List<CompletableFuture<?>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
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
}
