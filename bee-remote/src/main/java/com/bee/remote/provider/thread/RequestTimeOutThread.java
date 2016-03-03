package com.bee.remote.provider.thread;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.provider.domain.ProviderContext;
import com.bee.remote.provider.exception.ProcessTimeoutException;
import com.bee.remote.provider.exception.RequestAbortedException;
import com.bee.remote.provider.processor.RequestProcessor;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by jeoy.zhou on 3/3/16.
 */
public class RequestTimeOutThread implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(RequestTimeOutThread.class);
    private Map<InvocationRequest, ProviderContext> requestContextMap;
    private RequestProcessor requestProcessor;

    public RequestTimeOutThread(Map<InvocationRequest, ProviderContext> requestContextMap,
                                RequestProcessor requestProcessor) {
        this.requestContextMap = requestContextMap;
        this.requestProcessor = requestProcessor;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        for (InvocationRequest request : requestContextMap.keySet()) {
            try {
                if (request.getTimeout() <= 0 || request.getCreateMillisTime() <= 0
                        || (request.getCreateMillisTime() + request.getTimeout()) > currentTime) {
                    continue;
                }
                ProviderContext providerContext = requestContextMap.get(request);
                if (providerContext == null) {
                    LOGGER.error("provider context is null with request:" + request);
                    continue;
                }
                boolean stopAtOnce = true;
                if (requestProcessor != null)
                    stopAtOnce = requestProcessor.needCancelRequest(request);
                if (request.getMessageType() != Constants.MESSAGE_TYPE_HEART) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("timeout while processing request, from:")
                            .append(providerContext.getChannel() == null ? "" : providerContext.getChannel().getRemoteAddress())
                            .append(", process time:").append(System.currentTimeMillis())
                            .append("\r\nrequest:").append(request)
                            .append("\r\nprocessor stats:interrupt:").append(stopAtOnce).append(",")
                            .append(this.requestProcessor.getProcessorStatus(request));
                    Exception exception = null;
                    Thread thread = providerContext.getThread();
                    if (thread != null) {
                        msg.append("\r\nthe request has not been executed");
                        exception = new RequestAbortedException(msg.toString());
                        exception.setStackTrace(new StackTraceElement[] {});
                    } else {
                        exception = new ProcessTimeoutException(msg.toString());
                        exception.setStackTrace(thread.getStackTrace());
                    }
                }
                Future<? extends InvocationResponse> future = providerContext.getFuture();
                if (future != null && !future.isCancelled()) {
                    future.cancel(stopAtOnce);
                }
            } finally {
                requestContextMap.remove(request);
            }
        }
    }
}
