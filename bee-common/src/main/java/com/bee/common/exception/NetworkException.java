package com.bee.common.exception;

import com.bee.common.constants.ErrorCodeConstants;

/**
 * Created by jeoy.zhou on 2/19/16.
 */
public class NetworkException extends RpcException{

    private static final long serialVersionUID = -9161248768469428200L;

    public NetworkException() {
        super();
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return ErrorCodeConstants.NETWORK_FAILURE;
    }
}
