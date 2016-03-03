package com.bee.remote.provider.utils;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.SerializerFactory;
import com.bee.remote.common.codec.domain.DefaultResponse;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public final class ProviderUtils {

    private static final Logger LOGGER = Logger.getLogger(ProviderUtils.class);

    /**
     * 创建请求失败的返回结果
     * @param request
     * @param e
     * @return
     */
    public static InvocationResponse createFailResponse(InvocationRequest request, Throwable e) {
        InvocationResponse response = null;
        if (request.getMessageType() == Constants.MESSAGE_TYPE_HEART) {
            response = new DefaultResponse(request.getSerialize(), request.getSeq(), request.getMessageType(), ProviderExceptionConvertUtils.convert(e));
        } else {
            response = createThrowableResponse(request.getSeq(), request.getSerialize(), e);
        }
        return response;
    }

    public static InvocationResponse createThrowableResponse(long seq, byte serialization, Throwable e) {
        InvocationResponse response = SerializerFactory.getSerializer(serialization).newResponse();
        response.setSeq(seq);
        response.setSerialize(serialization);
        response.setMessageType(Constants.MESSAGE_TYPE_EXCEPTION);
        response.setReturn(ProviderExceptionConvertUtils.convert(e));
        return response;
    }

    public static InvocationResponse createServiceExceptionResponse(InvocationRequest request, Throwable e) {
        InvocationResponse response = SerializerFactory.getSerializer(request.getSerialize()).newResponse();
        response.setSeq(request.getSeq());
        response.setSerialize(request.getSerialize());
        response.setMessageType(Constants.MESSAGE_TYPE_SERVICE_EXCEPTION);
        response.setReturn(e);
        return response;
    }

    public static InvocationResponse createSuccessResponse(InvocationRequest request, Object result) {
        InvocationResponse response = SerializerFactory.getSerializer(request.getSerialize()).newResponse();
        response.setSeq(request.getSeq());
        response.setSerialize(request.getSerialize());
        response.setMessageType(Constants.MESSAGE_TYPE_SERVICE);
        response.setReturn(request);
        return response;
    }
}
