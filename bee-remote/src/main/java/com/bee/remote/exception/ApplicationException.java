package com.bee.remote.exception;

/**
 * Created by jeoy.zhou on 3/9/16.
 */
public class ApplicationException extends RuntimeException{

    private static final long serialVersionUID = 7075711261861226056L;

    protected String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public ApplicationException() {
        super();
    }

    public ApplicationException(String msg) {
        super(msg);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

    public ApplicationException(String msg, String errorCode, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
    }

    public ApplicationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
