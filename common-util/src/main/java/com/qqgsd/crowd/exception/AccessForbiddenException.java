package com.qqgsd.crowd.exception;

/**
 * 表示用户未登录就访问受保护资源抛出的异常
 */
public class AccessForbiddenException extends RuntimeException{

    public AccessForbiddenException() {
        super();
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }
}
