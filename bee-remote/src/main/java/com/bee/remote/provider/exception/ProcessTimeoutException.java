package com.bee.remote.provider.exception;

import com.bee.common.constants.ErrorCodeConstants;
import com.bee.common.exception.RpcException;

/**
 * Created by jeoy.zhou on 3/3/16.
 */
public class ProcessTimeoutException extends RpcException {

    private static final long serialVersionUID = 2861687495439601670L;

    public ProcessTimeoutException() {
        super();
    }

    public ProcessTimeoutException(String message) {
        super(message);
    }

    public ProcessTimeoutException(Throwable cause) {
        super(cause);
    }

    public ProcessTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return ErrorCodeConstants.PROCESS_TIMEOUT;
    }
}
