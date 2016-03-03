package com.bee.remote.invoker.cluster;

import com.bee.common.constants.Constants;
import com.bee.remote.invoker.domain.InvokerContext;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeoy.zhou on 2/17/16.
 */
public class ClusterFactory {

    private static final Logger LOGGER = Logger.getLogger(ClusterFactory.class);
    private static final Map<String, Cluster> CLUSTER_MAP = new HashMap<String, Cluster>();

    static {
        CLUSTER_MAP.put(Constants.CLUSTER_FAILFAST, new FastFailCluster());
    }

    public static Cluster selectCluster(InvokerContext invokerContext) {
        String key = invokerContext.getInvokerConfig().getCallType();
        Cluster cluster = CLUSTER_MAP.get(key);
        if (cluster == null) cluster = CLUSTER_MAP.get(Constants.CLUSTER_FAILFAST);
        return cluster;
    }

}
