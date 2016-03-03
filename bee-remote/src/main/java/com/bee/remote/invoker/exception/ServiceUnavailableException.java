package com.bee.remote.invoker.exception;

import com.bee.common.constants.ErrorCodeConstants;
import com.bee.common.exception.RpcException;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public class ServiceUnavailableException extends RpcException{

    private static final long serialVersionUID = 9033631119158069542L;

    public ServiceUnavailableException() {
        super();
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return ErrorCodeConstants.SERVICE_UNAVAILABLE_ERROR;
    }
}
