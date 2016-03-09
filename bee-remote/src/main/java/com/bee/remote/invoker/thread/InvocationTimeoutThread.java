package com.bee.remote.invoker.thread;


import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.invoker.callback.CallBack;
import com.bee.remote.invoker.callback.impl.CallBackFuture;
import com.bee.remote.invoker.domain.RemoteInvocationBean;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by jeoy.zhou on 3/4/16.
 */
public class InvocationTimeoutThread implements Runnable{

    private static final Logger LOGGER = Logger.getLogger(InvocationTimeoutThread.class);

    private Map<Long, RemoteInvocationBean> invocationBeanMap;

    public InvocationTimeoutThread(Map<Long, RemoteInvocationBean> invocationBeanMap) {
        this.invocationBeanMap = invocationBeanMap;
    }

    @Override
    public void run() {
        try {
            long currentTime = System.currentTimeMillis();
            for (Long seq : invocationBeanMap.keySet()) {
                RemoteInvocationBean invocationBean = invocationBeanMap.get(seq);
                if (invocationBean == null) continue;
                InvocationRequest request = invocationBean.getRequest();
                if (request.getTimeout() <= 0 || request.getCreateMillisTime() <= 0
                        || request.getTimeout() + request.getCreateMillisTime() >= currentTime)
                    continue;
                CallBack callBack = invocationBean.getCallback();
                if (callBack instanceof CallBackFuture) {
                    ((CallBackFuture) callBack).cancel();
                }
                invocationBeanMap.remove(seq);
                LOGGER.warn(String.format("remove timeout request, process time: %d \r\nrequest: %s", currentTime, request));
            }
        } catch (Exception e) {
            LOGGER.error("checking remote call timeout failed", e);
        }

    }
}
