package com.bee.remote.common.process.filter;

import com.bee.remote.common.codec.domain.InvocationContext;
import com.bee.remote.common.codec.domain.InvocationResponse;

/**
 * Created by jeoy.zhou on 1/31/16.
 */
public interface ServiceInvocationOperation<T extends InvocationContext> {

    /**
     * 业务处理过滤器
     * @param invocationContext
     * @return
     */
    InvocationResponse invoke(T invocationContext) throws Exception;


}
