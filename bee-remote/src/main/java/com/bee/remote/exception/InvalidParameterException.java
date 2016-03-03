package com.bee.remote.exception;

import com.bee.common.constants.ErrorCodeConstants;
import com.bee.common.exception.RpcException;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public class InvalidParameterException extends RpcException{

    private static final long serialVersionUID = 4700374333327093857L;

    public InvalidParameterException() {
        super();
    }

    public InvalidParameterException(String msg) {
        super(msg);
    }

    public InvalidParameterException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public String getErrorCode() {
        return ErrorCodeConstants.INVALID_PARAMETER;
    }
}
