package com.bee.remote.invoker.process;

import com.bee.remote.common.codec.domain.InvocationContext;
import com.bee.remote.common.codec.domain.InvocationResponse;

/**
 * Created by jeoy.zhou on 1/5/16.
 */
public interface ServiceInvocationProcess {

    /**
     * 动态代理处理链  接口定义
     * @param invocationContext
     * @return
     * @throws Exception
     */
    InvocationResponse handle(InvocationContext invocationContext) throws Exception;

}
