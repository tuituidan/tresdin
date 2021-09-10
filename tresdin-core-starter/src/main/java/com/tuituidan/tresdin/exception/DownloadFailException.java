package com.tuituidan.tresdin.exception;


import com.tuituidan.tresdin.util.StringExtUtils;

import ch.qos.logback.classic.spi.EventArgUtil;

/**
 * 下载失败.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/12/11
 */
public class DownloadFailException extends RuntimeException {

    private static final long serialVersionUID = 921853791074985058L;

    /**
     * HttpRequestErrorException.
     *
     * @param message message
     * @param args args
     */
    public DownloadFailException(String message, Object... args) {
        super(StringExtUtils.format(message, args), EventArgUtil.extractThrowable(args));
    }
}
