package com.bee.common.exception;

/**
 * Created by jeoy.zhou on 12/16/15.
 */
public class RegisterException extends RuntimeException{

    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterException() {
        super();
    }

    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(Throwable e) {
        super(e);
    }
}
