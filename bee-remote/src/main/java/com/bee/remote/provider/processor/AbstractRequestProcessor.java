package com.bee.remote.provider.processor;

import com.bee.common.constants.Constants;
import com.bee.common.thread.DefaultThreadFactory;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.processor.AbstractResponseProcessor;
import com.bee.remote.provider.domain.ProviderContext;
import com.bee.remote.provider.thread.RequestTimeOutThread;
import com.bee.remote.provider.utils.ProviderUtils;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by jeoy.zhou on 2/29/16.
 */
public abstract class AbstractRequestProcessor implements RequestProcessor {

    private static final Logger LOGGER = Logger.getLogger(AbstractResponseProcessor.class);

    private static final int CORE_THREAD = Runtime.getRuntime().availableProcessors() + 1;
    private static final int INTERVAL_SECOND = 1;
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    protected Map<InvocationRequest, ProviderContext> requestContextMap = new ConcurrentHashMap<InvocationRequest, ProviderContext>();

    public abstract void doStart();
    public abstract void doStop();
    public abstract Future<InvocationResponse> doProcessRequest(InvocationRequest request, ProviderContext providerContext);

    @Override
    public void start() {
        RequestTimeOutThread requestTimeOutThread = new RequestTimeOutThread(requestContextMap, this);
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(CORE_THREAD,
                new DefaultThreadFactory("ResponseProcessor-Provider-timeout-Checkout"), new ThreadPoolExecutor.AbortPolicy());
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(requestTimeOutThread, INTERVAL_SECOND, INTERVAL_SECOND, TimeUnit.SECONDS);
        doStart();
    }

    @Override
    public Future<InvocationResponse> processRequest(InvocationRequest request, ProviderContext providerContext) {
        if (request.getCreateMillisTime() == 0) {
            request.setCreateMillisTime(System.currentTimeMillis());
        }
        Future<InvocationResponse> future = null;
        try {
            future = doProcessRequest(request, providerContext);
        } catch (Exception e) {
            String errorMsg =  "process request failed:" + request;
            if (request.getCallType() == Constants.CALL_BACK_TYPE_REPLY
                    && request.getMessageType() != Constants.MESSAGE_TYPE_HEART) {
                providerContext.getChannel().write(ProviderUtils.createFailResponse(request, e));
            }
            LOGGER.error(errorMsg, e);
        }
        providerContext.setFuture(future);
        return future;
    }

    @Override
    public void destroy() throws Exception {
        try {
            scheduledThreadPoolExecutor.shutdown();
            scheduledThreadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } finally {
            scheduledThreadPoolExecutor = null;
        }
        doStop();
    }

    protected Map<InvocationRequest, ProviderContext> getRequestContextMap() {
        return requestContextMap;
    }
}
