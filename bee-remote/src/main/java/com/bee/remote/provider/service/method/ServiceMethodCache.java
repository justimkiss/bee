package com.bee.remote.provider.service.method;

import com.bee.common.constants.Constants;
import com.bee.common.exception.RpcException;
import com.bee.common.util.ClassUtils;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeoy.zhou on 12/10/15.
 */
public class ServiceMethodCache {

    private static final Set<String> IGNORE_METHODS = new HashSet<String>();
    private Map<String, Map<Integer, List<ServiceMethodDesc>>> METHOD_CACHES = new ConcurrentHashMap<String, Map<Integer, List<ServiceMethodDesc>>>();
    private Map<String, Map<ServiceParamDesc, ServiceMethodDesc>> BEST_METHOD_CACHE = new ConcurrentHashMap<String, Map<ServiceParamDesc, ServiceMethodDesc>>();
    private int methodSize;
    private Object service;

    static {
        Method[] methods = Objects.class.getMethods();
        for(Method method : methods) {
            IGNORE_METHODS.add(method.getName());
        }
        methods = Class.class.getMethods();
        for(Method method : methods) {
            IGNORE_METHODS.add(method.getName());
        }
    }

    public ServiceMethodCache(Object service) {
        this.service = service;
        init();
    }

    public ServiceMethodDesc getMethod(String methodName, ServiceParamDesc serviceParamDesc) {
        ServiceMethodDesc serviceMethodDesc = getMethodByCache(methodName, serviceParamDesc);
        if(serviceMethodDesc == null) {
            synchronized (this) {
                serviceMethodDesc = getMethodByCache(methodName, serviceParamDesc);
                if(serviceMethodDesc == null) {
                    serviceMethodDesc = getBestMethod(methodName, serviceParamDesc);
                    BEST_METHOD_CACHE.get(methodName).put(serviceParamDesc, serviceMethodDesc);
                }
            }
        }
        return serviceMethodDesc;
    }

    private ServiceMethodDesc getMethodByCache(String methodName, ServiceParamDesc serviceParamDesc) {
        Map<ServiceParamDesc, ServiceMethodDesc> map = BEST_METHOD_CACHE.get(serviceParamDesc);
        if(map == null) {
            map = new HashMap<ServiceParamDesc, ServiceMethodDesc>();
            BEST_METHOD_CACHE.put(methodName, map);
        }
        return map.get(serviceParamDesc);
    }

    private ServiceMethodDesc getBestMethod(String methodName, ServiceParamDesc serviceParamDesc) {
        Map<Integer, List<ServiceMethodDesc>> serviceMap = METHOD_CACHES.get(methodName);
        if(serviceMap == null) {
            throw new RpcException(String.format("ServiceMethodCache: service[%s] not contains method[%s]", service, methodName));
        }
        List<ServiceMethodDesc> methodDescList = serviceMap.get(serviceParamDesc.getLength());
        if(CollectionUtils.isEmpty(methodDescList)) {
            throw new RpcException(String.format("ServiceMethodCache: service[%s] not contains method[%s] with params length[%d]",
                    service, methodName, serviceParamDesc.getLength()));
        }
        if(serviceParamDesc.getLength() == 0) {
            return methodDescList.get(0);
        }
        int matchingValue = -1;
        ServiceMethodDesc serviceMethodDesc = null;
        for (ServiceMethodDesc m : methodDescList) {
            int mv = matching(m, serviceParamDesc.getParamNames(), false);
            if (mv > matchingValue) {
                matchingValue = mv;
                serviceMethodDesc = m;
            }
        }
        if (matchingValue < 0) {
            for (ServiceMethodDesc m : methodDescList) {
                int mv = matching(m, serviceParamDesc.getParamNames(), true);
                if (mv > matchingValue) {
                    matchingValue = mv;
                    serviceMethodDesc = m;
                }
            }
            if (matchingValue >= 0) {
                serviceMethodDesc.setNeedCast(true);
            }
        }
        if (matchingValue < 0) {
            throw new RpcException(String.format("ServiceMethodCache: service[%s] is not matched with method[%s] for parameter class types",
                    service, methodName));
        }
        return serviceMethodDesc;
    }

    private int matching(ServiceMethodDesc method, String[] paramClassNames, boolean cast) {
        int k = 0;
        for (int i = 0; i < paramClassNames.length; i++) {
            if (paramClassNames[i].equals(Constants.TRANSFER_NULL)) {
                continue;
            }
            Class<?> paramClass = null;
            try {
                paramClass = ClassUtils.loadClass(paramClassNames[i]);
            } catch (ClassNotFoundException e) {
                throw new RpcException("no class found for parameter:" + paramClassNames[i]);
            }
            if (paramClass == method.getParamsClass()[i]) {
                k++;
            } else if (cast) {
                if (paramClassNames[i].equals(Double.class.getName())) {
                    paramClass = Float.class;
                } else if (paramClassNames[i].equals(Integer.class.getName())) {
                    paramClass = Short.class;
                }
                if (paramClass == method.getParamsClass()[i]) {
                    k++;
                }
            }
            if (!method.getParamsClass()[i].isAssignableFrom(paramClass)) {
                return -1;
            }
        }
        return k;
    }


    private void init() {
        Method[] methods = service.getClass().getMethods();
        for(Method m : methods) {
            if(!IGNORE_METHODS.contains(m.getName())) {
                m.setAccessible(true);
                addMethodCache(m);
            }
        }
    }

    private void addMethodCache(Method method) {
        Map<Integer, List<ServiceMethodDesc>> methodMap = METHOD_CACHES.get(method.getName());
        if(methodMap == null) {
            methodMap = new HashMap<Integer, List<ServiceMethodDesc>>();
            METHOD_CACHES.put(method.getName(), methodMap);
        }
        List<ServiceMethodDesc> methodList = methodMap.get(method.getParameterCount());
        if(methodList == null) {
            methodList = new ArrayList<ServiceMethodDesc>();
            methodMap.put(method.getParameterCount(), methodList);
        }
        methodList.add(new ServiceMethodDesc(method, service));
        methodSize++;
    }
}
