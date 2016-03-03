package com.bee.remote.common.codec;

import com.bee.common.util.ClassUtils;
import com.bee.remote.common.codec.domain.*;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.invoker.domain.InvokerContext;
import com.bee.remote.invoker.process.InvokerProcessHandlerFactory;
import com.bee.remote.invoker.service.ServiceInvocationHandler;
import org.apache.commons.lang.SerializationException;

import java.lang.reflect.Proxy;

/**
 * Created by jeoy.zhou on 1/5/16.
 */
public abstract class AbstractSerializer implements Serializer{

    @Override
    public Object getProxy(InvokerConfig<?> invokerConfig) {
        return Proxy.newProxyInstance(ClassUtils.getCurrentClassLoader(null),
                new Class[]{invokerConfig.getServiceInterface()},
                new ServiceInvocationHandler(invokerConfig, InvokerProcessHandlerFactory.getServiceInvocationOperation()));
    }

    @Override
    public InvocationRequest newRequest(InvokerContext invokerContext) throws SerializationException {
        return new DefaultRequest(invokerContext);
    }

    @Override
    public InvocationResponse newResponse() throws SerializationException {
        return new DefaultResponse();
    }
}
