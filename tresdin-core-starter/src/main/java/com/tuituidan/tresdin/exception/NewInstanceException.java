package com.tuituidan.tresdin.exception;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.tuituidan.tresdin.util.StringExtUtils;

/**
 * NewInstanceException.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
public class NewInstanceException extends RuntimeException {
    private static final long serialVersionUID = -4429513569174786724L;

    /**
     * WrapperException.
     *
     * @param message message
     * @param args    args
     */
    public NewInstanceException(String message, Object... args) {
        super(StringExtUtils.format(message, args), EventArgUtil.extractThrowable(args));
    }
}
