package com.bee.remote.provider.process;

import com.bee.common.constants.Constants;
import com.bee.remote.common.domain.Disposable;
import com.bee.remote.common.process.filter.AbstractProviderOperation;
import com.bee.remote.common.process.filter.ServiceInvocationOperation;
import com.bee.remote.provider.domain.ProviderContext;
import com.bee.remote.provider.process.impl.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeoy.zhou on 2/29/16.
 */
public final class ProviderProcessHandlerFactory {

    private static final Logger LOGGER = Logger.getLogger(ProviderProcessHandlerFactory.class);

    private static AbstractProviderOperation bizOperation;
    private static AbstractProviderOperation heartBeatOperation;

    private static List<AbstractProviderOperation> bizOperationList = new LinkedList<AbstractProviderOperation>();
    private static List<AbstractProviderOperation> hearBeatOperationList = new LinkedList<AbstractProviderOperation>();

    private static boolean isInit = false;

    public static void init() {
        if (!isInit) {
            registerBizOperation(new WriteResponseProviderOperation());
            registerBizOperation(new ExceptionProviderOperation());
            registerBizOperation(new GatewayProviderOperation());
            registerBizOperation(new BusinessProviderOperation());
            bizOperation = createServiceInvocationOperation(bizOperationList);
            registerHeartBeatOperation(new HeartBeatProviderOperation());
            heartBeatOperation = createServiceInvocationOperation(hearBeatOperationList);
            isInit = true;
        }
    }

    /**
     * 回去response处理链
     * @param messageType
     * @return
     */
    public static ServiceInvocationOperation<ProviderContext> selectOperation(int messageType) {
        if (Constants.MESSAGE_TYPE_HEART == messageType) {
            return heartBeatOperation;
        }
        return bizOperation;
    }

    private static AbstractProviderOperation createServiceInvocationOperation(List<AbstractProviderOperation> serviceInvocationOperations) {
        if(CollectionUtils.isEmpty(serviceInvocationOperations)) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("InvokerProcessHandlerFactory:createServiceInvocationHandler no operation");
                return null;
            }
        }
        Iterator<AbstractProviderOperation> iterator = serviceInvocationOperations.iterator();
        AbstractProviderOperation serviceInvocationOperation = iterator.next();
        AbstractProviderOperation providerOperation = serviceInvocationOperation;
        AbstractProviderOperation tmp = null;
        while (iterator.hasNext()) {
            tmp = iterator.next();
            providerOperation.setProviderOperation(tmp);
            providerOperation = tmp;
        }
        return serviceInvocationOperation;
    }

    public static void destroy() throws Exception {
        for (AbstractProviderOperation providerOperation : bizOperationList) {
            if (providerOperation instanceof Disposable) {
                try {
                    ((Disposable) providerOperation).destroy();
                } catch (Exception e) {

                }
            }
        }
        for (AbstractProviderOperation providerOperation : hearBeatOperationList) {
            if (providerOperation instanceof Disposable) {
                try {
                    ((Disposable) providerOperation).destroy();
                } catch (Exception e) {

                }
            }
        }
        bizOperationList.clear();
        hearBeatOperationList.clear();
    }

    private static void registerBizOperation(AbstractProviderOperation providerOperation) {
        if (providerOperation == null) return;
        bizOperationList.add(providerOperation);
    }

    private static void registerHeartBeatOperation(AbstractProviderOperation providerOperation) {
        if (providerOperation == null) return;
        hearBeatOperationList.add(providerOperation);
    }

}
