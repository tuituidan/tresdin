package com.tuituidan.tresdin.util.thread;

import com.tuituidan.tresdin.util.thread.config.ThreadPoolConfig;
import com.tuituidan.tresdin.util.thread.config.ThreadPoolConfigItem;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 通用的异步工具类.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2021/1/21
 */
@Component
public class AsyncUtils implements ApplicationContextAware {

    /**
     * 任务执行线程池.
     */
    private static ThreadPoolExecutor threadPool;

    /**
     * runAsync.
     *
     * @param task task
     * @return Void Void
     */
    public static CompletableFuture<Void> runAsync(Runnable task) {
        return CompletableFuture.runAsync(task, threadPool);
    }

    /**
     * setApplicationContext.
     *
     * @param applicationContext applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        AsyncUtils.init(applicationContext.getBean(ThreadPoolConfig.class).getCommon());
    }

    private static void init(ThreadPoolConfigItem config) {
        threadPool = CompletableUtils.createThreadPoolExecutor(config);
    }

}
