package com.bee.remote.invoker.service;

import com.bee.common.constants.Constants;
import com.bee.common.thread.DefaultThreadFactory;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.domain.Disposable;
import com.bee.remote.invoker.callback.CallBack;
import com.bee.remote.invoker.domain.RemoteInvocationBean;
import com.bee.remote.invoker.thread.InvocationTimeoutThread;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jeoy.zhou on 2/24/16.
 */
public final class ServiceInvocationRepository implements Disposable{

    private static final Logger LOGGER = Logger.getLogger(ServiceInvocationRepository.class);

    private static final long INTERVAL = Constants.DEFAULT_INVOCATION_CHECK_INTERVAL;
    private static final ServiceInvocationRepository SERVICE_INVOCATION_REPOSITORY = new ServiceInvocationRepository();
    private static Map<Long, RemoteInvocationBean> INVOCATIONS = new ConcurrentHashMap<Long, RemoteInvocationBean>();
    private static ScheduledThreadPoolExecutor TIME_CHECK_POOL;

    private ServiceInvocationRepository(){}

    public static ServiceInvocationRepository getInstance() {
        return SERVICE_INVOCATION_REPOSITORY;
    }

    public void init() {
        InvocationTimeoutThread invocationTimeoutThread = new InvocationTimeoutThread(INVOCATIONS);
        TIME_CHECK_POOL = new ScheduledThreadPoolExecutor(1,
                new DefaultThreadFactory("ServiceInvocationRepository-timeout-check"), new ThreadPoolExecutor.AbortPolicy());
        TIME_CHECK_POOL.scheduleWithFixedDelay(invocationTimeoutThread, INTERVAL, INTERVAL, TimeUnit.MILLISECONDS);
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

    @Override
    public void destroy() throws Exception {
        TIME_CHECK_POOL.shutdown();
    }
}
