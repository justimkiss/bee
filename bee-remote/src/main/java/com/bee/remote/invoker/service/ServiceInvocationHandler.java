package com.bee.remote.invoker.service;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationContext;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.common.process.filter.ServiceInvocationOperation;
import com.bee.remote.invoker.config.InvokerConfig;
import com.bee.remote.invoker.domain.DefaultInvokerContext;
import com.bee.remote.invoker.utils.InvokerUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

/**
 * Created by jeoy.zhou on 1/5/16.
 */
public class ServiceInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = Logger.getLogger(ServiceInvocationHandler.class);
    private static final String METHOD_TO_STRING = "toString";
    private static final String METHOD_HASH_CODE = "hashCode";
    private static final String METHOD_EQUALS = "equals";
    private final InvokerConfig<?> invokerConfig;
    private final ServiceInvocationOperation<InvocationContext> serviceInvocationOperation;

    public ServiceInvocationHandler(InvokerConfig<?> invokerConfig, ServiceInvocationOperation<InvocationContext> serviceInvocationOperation) {
        this.invokerConfig = invokerConfig;
        this.serviceInvocationOperation = serviceInvocationOperation;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterType = method.getParameterTypes();
        if(method.getDeclaringClass() == Object.class) {
            return method.invoke(serviceInvocationOperation, args);
        }
        int paramLength = parameterType.length;
        if(METHOD_TO_STRING.equals(methodName) && paramLength == 0) {
            return serviceInvocationOperation.toString();
        }
        if(METHOD_HASH_CODE.equals(methodName) && paramLength == 0) {
            return serviceInvocationOperation.hashCode();
        }
        if(METHOD_EQUALS.equals(methodName) && paramLength == 1) {
            return serviceInvocationOperation.equals(args[0]);
        }
        return handleResult(
                serviceInvocationOperation.invoke(new DefaultInvokerContext(invokerConfig, methodName, parameterType, args)),
                method.getReturnType());
    }

    private Object handleResult(InvocationResponse response, Class<?> resultType) throws Exception{
        Object obj = response.getReturn();
        if(obj != null) {
            int messageType = response.getMessageType();
            if(messageType == Constants.MESSAGE_TYPE_SERVICE) {
                return obj;
            } else if(messageType == Constants.MESSAGE_TYPE_SERVICE_EXCEPTION) {
                throw InvokerUtils.toApplicationException(response);
            } else if (messageType == Constants.MESSAGE_TYPE_EXCEPTION) {
                throw InvokerUtils.toRpcException(response);
            }
            throw new InvalidParameterException("ServiceInvocationHandler: unsupported response with message type ==> " + messageType);
        }
        return null;
    }

    private Object getReturn(Class<?> returnType) {
        if (returnType == byte.class) {
            return (byte) 0;
        } else if (returnType == short.class) {
            return (short) 0;
        } else if (returnType == int.class) {
            return 0;
        } else if (returnType == boolean.class) {
            return false;
        } else if (returnType == long.class) {
            return 0l;
        } else if (returnType == float.class) {
            return 0.0f;
        } else if (returnType == double.class) {
            return 0.0d;
        } else {
            return null;
        }
    }
}
