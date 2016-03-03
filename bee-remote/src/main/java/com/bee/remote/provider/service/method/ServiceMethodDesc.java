package com.bee.remote.provider.service.method;

import com.bee.remote.common.utils.InvocationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jeoy.zhou on 12/10/15.
 */
public class ServiceMethodDesc {

    private Method method;
    private Object service;
    private Class<?>[] paramsClass;
    private Class<?>[] originalParamsClass;
    private Integer length;
    private boolean needCast = false;

    public ServiceMethodDesc(Method method, Object service) {
        this.method = method;
        this.service = service;
        this.originalParamsClass = method.getParameterTypes();
        this.length = originalParamsClass.length;
        initParamsClass();
    }

    public Object invoke(Object[] arguments) throws InvocationTargetException, IllegalArgumentException {
        try {
            return this.method.invoke(this.getService(), arguments);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("invalid parameter types:" +
                    InvocationUtils.getRemoteCallFullName(this.getMethod().getName(), arguments),
                    e.getCause());
        }
    }

    private void initParamsClass() {
        paramsClass = new Class<?>[originalParamsClass.length];
        Class<?> tmpClass = null;
        for(int i=0; i < originalParamsClass.length; i++) {
            tmpClass = originalParamsClass[i];
            if (tmpClass == byte.class) {
                tmpClass = Byte.class;
            } else if (tmpClass == short.class) {
                tmpClass = Short.class;
            } else if (tmpClass == int.class) {
                tmpClass = Integer.class;
            } else if (tmpClass == boolean.class) {
                tmpClass = Boolean.class;
            } else if (tmpClass == long.class) {
                tmpClass = Long.class;
            } else if (tmpClass == float.class) {
                tmpClass = Float.class;
            } else if (tmpClass == double.class) {
                tmpClass = Double.class;
            }
            paramsClass[i] = tmpClass;
        }
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public Class<?>[] getParamsClass() {
        return paramsClass;
    }

    public void setParamsClass(Class<?>[] paramsClass) {
        this.paramsClass = paramsClass;
    }

    public Class<?>[] getOriginalParamsClass() {
        return originalParamsClass;
    }

    public void setOriginalParamsClass(Class<?>[] originalParamsClass) {
        this.originalParamsClass = originalParamsClass;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public boolean isNeedCast() {
        return needCast;
    }

    public void setNeedCast(boolean needCast) {
        this.needCast = needCast;
    }
}
