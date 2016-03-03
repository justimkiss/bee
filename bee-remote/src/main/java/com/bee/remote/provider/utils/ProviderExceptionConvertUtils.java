package com.bee.remote.provider.utils;

import com.bee.common.exception.RpcException;
import com.bee.remote.exception.BeeException;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public final class ProviderExceptionConvertUtils {

    private static final Logger LOGGER = Logger.getLogger(ProviderExceptionConvertUtils.class);

    public static BeeException convert(Throwable e) {
        if (e == null) return null;
        if (e instanceof RpcException) {
            RpcException rpcEx = (RpcException) e;
            BeeException newException = new BeeException(String.format("#%s@%s", rpcEx
                    .getClass().getSimpleName(), rpcEx.getMessage()));
            newException.setStackTrace(rpcEx.getStackTrace());
            return newException;
        } else {
            BeeException newException = new BeeException(String.format("@%s@%s", e.getClass().getSimpleName(),
                    e.getMessage()));
            newException.setStackTrace(e.getStackTrace());
            return newException;
        }
    }
}
