package com.bee.common.exception;

/**
 * Created by jeoy.zhou on 12/8/15.
 */
public class RpcException extends RuntimeException{

    private static final long serialVersionUID = -3593290532643390495L;

    protected String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public RpcException() {
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String msg, String errorCode, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
    }
}
