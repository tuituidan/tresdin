package com.tuituidan.tresdin.exception;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.tuituidan.tresdin.util.StringExtUtils;

/**
 * 下载失败.
 *
 * @author tuituidan
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
