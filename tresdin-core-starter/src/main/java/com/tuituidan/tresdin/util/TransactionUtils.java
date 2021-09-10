package com.tuituidan.tresdin.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * TransactionUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
@Component
public class TransactionUtils implements ApplicationContextAware {

    private static TransactionUtils transactionUtils;

    /**
     * 事务处理.
     *
     * @param runnable runnable
     */
    @Transactional(rollbackFor = Exception.class)
    public void run(Runnable runnable) {
        runnable.run();
    }

    /**
     * 事务处理.
     *
     * @param runnable runnable
     */
    public static void execute(Runnable runnable) {
        transactionUtils.run(runnable);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TransactionUtils.init(applicationContext.getBean(TransactionUtils.class));
    }

    private static void init(TransactionUtils transactionUtil) {
        transactionUtils = transactionUtil;
    }
}
