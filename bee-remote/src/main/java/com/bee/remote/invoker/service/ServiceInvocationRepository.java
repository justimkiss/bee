package com.bee.remote.invoker.service;

import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.callback.CallBack;
import com.bee.remote.invoker.domain.RemoteInvocationBean;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeoy.zhou on 2/24/16.
 */
public final class ServiceInvocationRepository {

    private static final Logger LOGGER = Logger.getLogger(ServiceInvocationRepository.class);

    private static final ServiceInvocationRepository SERVICE_INVOCATION_REPOSITORY = new ServiceInvocationRepository();
    private static Map<Long, RemoteInvocationBean> INVOCATIONS = new ConcurrentHashMap<Long, RemoteInvocationBean>();


    private ServiceInvocationRepository(){}

    public static ServiceInvocationRepository getInstance() {
        return SERVICE_INVOCATION_REPOSITORY;
    }

    public static void put(long seq, RemoteInvocationBean remoteInvocationBean) {
        INVOCATIONS.put(seq, remoteInvocationBean);
    }

    public static void remove(long seq) {
        INVOCATIONS.remove(seq);
    }

    public static void receiveResponse(InvocationResponse response) {
        RemoteInvocationBean remoteInvocationBean = INVOCATIONS.get(response.getSeq());
        if (remoteInvocationBean == null) {
            LOGGER.warn("the response has expires: " + response);
            return;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("received response:" + response);
        InvocationRequest request = remoteInvocationBean.getRequest();
        try {
            CallBack callBack = remoteInvocationBean.getCallback();
            if (callBack != null) {
                callBack.callBack(response);
                callBack.completed();
            }
        } finally {
            remove(response.getSeq());
        }
    }

}
