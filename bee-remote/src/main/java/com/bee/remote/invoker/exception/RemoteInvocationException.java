package com.bee.remote.invoker.exception;

import com.bee.common.constants.ErrorCodeConstants;
import com.bee.common.exception.RpcException;

/**
 * Created by jeoy.zhou on 3/9/16.
 */
public class RemoteInvocationException extends RpcException{

    private static final long serialVersionUID = 5048933399492673897L;

    public RemoteInvocationException() {
        super();
    }

    public RemoteInvocationException(String message) {
        super(message);
    }

    public RemoteInvocationException(Throwable cause) {
        super(cause);
    }

    public RemoteInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return ErrorCodeConstants.REMOTE_INVOCATION;
    }
}
