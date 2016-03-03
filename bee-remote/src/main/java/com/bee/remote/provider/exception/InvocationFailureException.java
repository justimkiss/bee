package com.bee.remote.provider.exception;

import com.bee.common.constants.ErrorCodeConstants;
import com.bee.common.exception.RpcException;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public class InvocationFailureException extends RpcException {

    private static final long serialVersionUID = -6096370003754060448L;

    public InvocationFailureException() {
        super();
    }

    public InvocationFailureException(String message) {
        super(message);
    }

    public InvocationFailureException(Throwable cause) {
        super(cause);
    }

    public InvocationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return ErrorCodeConstants.INVOCATION_FAILURE;
    }
}
