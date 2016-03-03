package com.bee.remote.invoker.route.statics;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.invoker.domain.InvokerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jeoy.zhou on 2/18/16.
 */
public final class InvokerStaticsHolder {

    private static final Logger LOGGER = Logger.getLogger(InvokerStaticsHolder.class);

    private static final ConcurrentHashMap<String, AtomicLong> REQUESTS = new ConcurrentHashMap<String, AtomicLong>();

    public static AtomicLong getCapacity(InvokerContext invokerContext) {
        if (invokerContext == null)
            throw new IllegalArgumentException("InvokerStaticsHolder: getCapacity argument[invokerContext] is null");
        if (invokerContext.getClient() == null)
            throw new IllegalArgumentException("InvokerStaticsHolder: getCapacity argument[invokerContext.client] is null");
        return getCapacity(invokerContext.getClient().getAddress());
    }

    public static long getCapacityValue(String address) {
        return getCapacity(address).get();
    }

    public static AtomicLong getCapacity(String address) {
        if (StringUtils.isBlank(address))
            throw new IllegalArgumentException("InvokerStaticsHolder: getCapacity argument[addresst] is null");
        AtomicLong request = REQUESTS.get(address);
        if (request == null) {
            request = new AtomicLong(1);
            AtomicLong oldRequest = REQUESTS.putIfAbsent(address, request);
            if (oldRequest != null)
                request = oldRequest;
        }
        return request;
    }

    public static void flowIn(InvokerContext invokerContext) {
        if (!needRecordRequest(invokerContext.getRequest())) return;
        AtomicLong request = InvokerStaticsHolder.getCapacity(invokerContext);
        request.incrementAndGet();
    }

    private static boolean needRecordRequest(InvocationRequest invocationRequest) {
        return invocationRequest != null && Constants.MESSAGE_TYPE_SERVICE.equals(invocationRequest.getMessageType());
    }
}
