package com.bee.remote.provider.exception;

import com.bee.common.constants.ErrorCodeConstants;
import com.bee.common.exception.RpcException;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public class RequestAbortedException extends RpcException{

    private static final long serialVersionUID = -4223025391546495067L;

    public RequestAbortedException() {
        super();
    }

    public RequestAbortedException(String message) {
        super(message);
    }

    public RequestAbortedException(Throwable cause) {
        super(cause);
    }

    public RequestAbortedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return ErrorCodeConstants.REQUEST_ABORT;
    }
}
