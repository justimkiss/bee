package com.bee.remote.invoker.processor;

/**
 * Created by jeoy.zhou on 2/24/16.
 */
public final class ResponseProcessorFactory {

    private static final ResponseProcessor RESPONSE_PROCESSOR = new ResponseThreadPoolProcessor();

    public static final ResponseProcessor getProcessor() {
        return RESPONSE_PROCESSOR;
    }

    public static final void shutdown() {
        RESPONSE_PROCESSOR.shutdown();
    }

}
