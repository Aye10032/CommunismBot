package com.dazo66.command.exceptions;

/**
 * 如果当前深度所有分支判定不正确时存在ifnot则调用
 * @author Dazo66
 */
public class IfNotRuntiomeException extends RuntimeException {
    public IfNotRuntiomeException() {
    }

    public IfNotRuntiomeException(String message) {
        super(message);
    }

    public IfNotRuntiomeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IfNotRuntiomeException(Throwable cause) {
        super(cause);
    }

    public IfNotRuntiomeException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
