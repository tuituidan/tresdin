package com.tuituidan.tresdin.mybatis.util;

/**
 * 简化版雪花算法，去掉了工作机器和数据中心，仅限单体服务使用，使用枚举实现单例.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2023/2/7
 */
public enum SnowFlake {
    /**
     * 实例.
     */
    INSTANCE;

    /**
     * 起始的时间戳
     */
    private static final long START_STMP = 1606013605853L;

    /**
     * 序列号占用的位数
     */
    private static final long SEQUENCE_BIT = 12L;

    /**
     * 生成序列的掩码
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 毫秒内序列
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastStmp = -1L;

    /**
     * 获取ID
     *
     * @return long
     */
    private synchronized long nextId() {
        long currStmp = System.currentTimeMillis();
        if (currStmp < lastStmp) {
            throw new UnsupportedOperationException("系统时钟回拨，无法生成ID");
        }
        if (currStmp == lastStmp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            sequence = 0L;
        }

        lastStmp = currStmp;
        return (currStmp - START_STMP) << SEQUENCE_BIT
                | sequence;
    }

    /**
     * 获取小一毫秒秒时间戳
     *
     * @return long
     */
    private long getNextMill() {
        long mill = System.currentTimeMillis();
        while (mill <= lastStmp) {
            mill = System.currentTimeMillis();
        }
        return mill;
    }

    /**
     * 获取ID.
     *
     * @return String
     */
    public static long newId() {
        return INSTANCE.nextId();
    }
}

