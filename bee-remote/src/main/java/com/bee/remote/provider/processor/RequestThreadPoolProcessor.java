package com.bee.remote.provider.processor;

import com.bee.common.constants.Constants;
import com.bee.common.thread.DefaultThreadFactory;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.process.filter.ServiceInvocationOperation;
import com.bee.remote.provider.config.ServiceConfig;
import com.bee.remote.provider.domain.ProviderContext;
import com.bee.remote.provider.process.ProviderProcessHandlerFactory;
import org.apache.log4j.Logger;

import java.util.concurrent.*;

/**
 * Created by jeoy.zhou on 2/29/16.
 */
public class RequestThreadPoolProcessor extends AbstractRequestProcessor {

    private static final Logger LOGGER = Logger.getLogger(RequestThreadPoolProcessor.class);
    private static final float CANCEL_RADIO = Constants.DEFAULT_CANCEL_RADIO;

    private ThreadPoolExecutor requestProcessThreadPool;

    public RequestThreadPoolProcessor(ServiceConfig serviceConfig) {
        requestProcessThreadPool = new ThreadPoolExecutor(serviceConfig.getCorePoolSize(), serviceConfig.getMaxPoolSize(),
                30, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(serviceConfig.getWorkQueueSize()),
                new DefaultThreadFactory("Bee-Sever-Request-Processor-" + serviceConfig.getProtocol() + "-" + serviceConfig.getPort()), new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public void doStart() {
    }

    @Override
    public void doStop() {
        if (requestProcessThreadPool == null) return;
        try {
            requestProcessThreadPool.shutdown();
            requestProcessThreadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            requestProcessThreadPool = null;
        }
    }


    @Override
    public boolean needCancelRequest(InvocationRequest request) {
        ThreadPoolExecutor threadPoolExecutor = selectPool(request);
        return threadPoolExecutor.getPoolSize() >= threadPoolExecutor.getMaximumPoolSize() * CANCEL_RADIO;
    }

    @Override
    public Future<InvocationResponse> doProcessRequest(final InvocationRequest request, final ProviderContext providerContext) {
        getRequestContextMap().put(request, providerContext);
        Callable<InvocationResponse> callable = new Callable<InvocationResponse>() {
            @Override
            public InvocationResponse call() throws Exception {
                try {
                    ServiceInvocationOperation<ProviderContext> providerOperation = ProviderProcessHandlerFactory.selectOperation(request.getMessageType());
                    if (providerOperation == null) {
                        if (LOGGER.isDebugEnabled())
                            LOGGER.debug("can not find responseOperationHandle by messageType: " + request.getMessageType());
                        return null;
                    }
                    providerContext.setThread(Thread.currentThread());
                    return providerOperation.invoke(providerContext);
                } catch (Exception e) {
                     LOGGER.error("Process request failed with invocation handler, you should never be here.", e);
                } finally {
                    getRequestContextMap().remove(request);
                }
                return null;
            }
        };
        // TODO 简单做
        ExecutorService executorService = selectPool(request);
        try {
            return executorService.submit(callable);
        } catch (RejectedExecutionException e) {
            throw new RejectedExecutionException(getProcessorStatus(request), e);
        } finally {
            getRequestContextMap().remove(request);
        }
    }

    @Override
    public String getProcessorStatus(InvocationRequest request) {
        return getThreadPoolStatus(selectPool(request));
    }

    private String getThreadPoolStatus(ThreadPoolExecutor e) {
        if (e == null) return null;
        return String.format(
                "request pool size:%d(active:%d,core:%d,max:%d,largest:%d),task count:%d(completed:%d),queue size:%d",
                e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(),
                e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(), e.getQueue().size());
    }

    private ThreadPoolExecutor selectPool(InvocationRequest request) {
        return requestProcessThreadPool;
    }

}
