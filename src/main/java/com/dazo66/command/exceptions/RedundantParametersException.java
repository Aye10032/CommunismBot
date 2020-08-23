package com.dazo66.command.exceptions;

/**
 * 命令执行时参数过多的异常
 * @author Dazo66
 */
public class RedundantParametersException extends RuntimeException {
    public RedundantParametersException() {
    }

    public RedundantParametersException(String message) {
        super(message);
    }

    public RedundantParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedundantParametersException(Throwable cause) {
        super(cause);
    }

    public RedundantParametersException(String message, Throwable cause,
                                        boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
