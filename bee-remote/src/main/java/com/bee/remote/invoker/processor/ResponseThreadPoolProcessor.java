package com.bee.remote.invoker.processor;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.service.ServiceInvocationRepository;
import org.apache.log4j.Logger;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jeoy.zhou on 2/24/16.
 */
public class ResponseThreadPoolProcessor extends AbstractResponseProcessor{

    private static final Logger LOGGER = Logger.getLogger(ResponseThreadPoolProcessor.class);

    private static final int CORE_POOL_SIZE = Constants.DEFAULT_RESPONSE_PROCESSOR_CORE_SIZE;
    private static final int MAX_POOL_SIZE = Constants.DEFAULT_RESPONSE_PROCESSOR_MAx_SIZE;
    private static final int QUEUE_SIZE = Constants.DEFAULT_RESPONSE_PROCESSOR_QUEUE_SIZE;

    private static ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    public ResponseThreadPoolProcessor() {
        this(CORE_POOL_SIZE, MAX_POOL_SIZE, QUEUE_SIZE);
    }

    public ResponseThreadPoolProcessor(int corePoolSize, int maxPoolSize, int queueSize) {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                30, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(queueSize), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    protected void doProcessProcess(final InvocationResponse invocationResponse, Client client) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                ServiceInvocationRepository.receiveResponse(invocationResponse);
            }
        };
        try {
            THREAD_POOL_EXECUTOR.execute(task);
        } catch (RejectedExecutionException e) {
            LOGGER.error("process response failed", e);
            throw e;
        }
    }

    @Override
    public void shutdown() {
        THREAD_POOL_EXECUTOR.shutdown();
    }
}
