package com.bee.remote.invoker.utils;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.SerializerFactory;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.callback.CallBack;
import com.bee.remote.invoker.callback.sync.ServiceFuture;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.invoker.domain.InvokerContext;
import com.bee.remote.invoker.domain.RemoteInvocationBean;
import com.bee.remote.invoker.service.ServiceInvocationRepository;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jeoy.zhou on 2/18/16.
 */
public final class InvokerUtils {

    private static final Logger LOGGER = Logger.getLogger(InvokerUtils.class);

    private static final AtomicLong REQUEST_SEQ = new AtomicLong();


    public static InvocationResponse sendRequest(Client client, InvocationRequest request, CallBack callback) {
        if (request.getCallType() == Constants.CALL_BACK_TYPE_REPLY) {
            RemoteInvocationBean remoteInvocationBean = new RemoteInvocationBean(request, callback);
            ServiceInvocationRepository.put(request.getSeq(), remoteInvocationBean);
            if (callback != null) {
                callback.setClient(client);
                callback.setRequest(request);
            }
        }
        InvocationResponse response = null;
        try {
            response = client.sendRequest(request, callback);
        } catch (Exception e) {
            LOGGER.warn("InvokerUtils: sendRequest error", e);
            ServiceInvocationRepository.remove(request.getSeq());
        } finally {
            if (response != null)
                ServiceInvocationRepository.remove(response.getSeq());
        }
        return response;
    }

    public static InvocationRequest createRemoteCallRequest(InvokerContext invokerContext) {
        InvokerConfig<?> invokerConfig = invokerContext.getInvokerConfig();
        InvocationRequest request = invokerContext.getRequest();
        if (request != null) {
            request = SerializerFactory.getSerializer(invokerConfig.getSerialize()).newRequest(invokerContext);
            invokerContext.setRequest(request);
        }
        request.setSeq(REQUEST_SEQ.incrementAndGet());
        return request;
    }

    public static InvocationResponse createFutureResponse(ServiceFuture serviceFuture) {
        FutureResponse response = new FutureResponse();
        response.setServiceFuture(serviceFuture);
        return response;
    }

    public static InvocationResponse createNoReturnResponse() {
        return new NoReturnResponse();
    }


    public static class FutureResponse extends SpecialResponse {

        private static final long serialVersionUID = 7292103209593548830L;

        private ServiceFuture serviceFuture;

        @Override
        public ServiceFuture getServiceFuture() {
            return serviceFuture;
        }

        @Override
        public void setServiceFuture(ServiceFuture serviceFuture) {
            this.serviceFuture = serviceFuture;
        }
    }

    public static class NoReturnResponse extends SpecialResponse {

        private static final long serialVersionUID = -8530717525920317983L;

    }

    public static abstract class SpecialResponse implements InvocationResponse {

        private static final long serialVersionUID = 5797408560349877848L;

        private long invokerRequestTime;
        private long invokerResponseTime;
        private long providerRequestTime;
        private long providerResponseTime;
        private ServiceFuture serviceFuture;

        public long getInvokerRequestTime() {
            return invokerRequestTime;
        }

        public void setInvokerRequestTime(long invokerRequestTime) {
            this.invokerRequestTime = invokerRequestTime;
        }

        public long getInvokerResponseTime() {
            return invokerResponseTime;
        }

        public void setInvokerResponseTime(long invokerResponseTime) {
            this.invokerResponseTime = invokerResponseTime;
        }

        public long getProviderRequestTime() {
            return providerRequestTime;
        }

        public void setProviderRequestTime(long providerRequestTime) {
            this.providerRequestTime = providerRequestTime;
        }

        public long getProviderResponseTime() {
            return providerResponseTime;
        }

        public void setProviderResponseTime(long providerResponseTime) {
            this.providerResponseTime = providerResponseTime;
        }

        public ServiceFuture getServiceFuture() {
            return serviceFuture;
        }

        public void setServiceFuture(ServiceFuture serviceFuture) {
            this.serviceFuture = serviceFuture;
        }

        @Override
        public void setMessageType(int messageType) {

        }

        @Override
        public byte getSerialize() {
            return 0;
        }

        @Override
        public void setSerialize(byte serialize) {

        }

        @Override
        public long getSeq() {
            return 0;
        }

        @Override
        public void setSeq(long seq) {

        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public void setSize(int size) {

        }

        @Override
        public Object getContext() {
            return null;
        }

        @Override
        public void setContext(Object context) {

        }

        @Override
        public int getMessageType() {
            return 0;
        }

        @Override
        public String getCause() {
            return null;
        }

        @Override
        public Object getReturn() {
            return null;
        }

        @Override
        public void setReturn(Object obj) {

        }

        @Override
        public Map<String, Serializable> getResponseValues() {
            return null;
        }

        @Override
        public void setResponseValues(Map<String, Serializable> responseValues) {

        }
    }
}
