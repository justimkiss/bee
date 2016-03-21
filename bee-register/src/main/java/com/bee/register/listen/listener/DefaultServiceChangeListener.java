package com.bee.register.listen.listener;

import com.bee.common.constants.Constants;
import com.bee.common.domain.HostInfo;
import com.bee.register.RegisterManager;
import com.bee.register.listen.RegisterListenManager;
import com.bee.register.listen.event.ProviderChangeEnum;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * Created by jeoy.zhou on 3/17/16.
 */
public class DefaultServiceChangeListener implements ServiceChangeListener {

    private static final Logger LOGGER = Logger.getLogger(DefaultServiceChangeListener.class);

    @Override
    public synchronized void onServiceAddressChange(String serviceName, List<String> hosts) {
        try {
            Set<HostInfo> hostInfoSet = parseHostInfos(serviceName, hosts);
            Set<HostInfo> oldHostInfoSet = RegisterManager.getInstance().getCacheServiceHostInfoByServiceName(serviceName);
            if (oldHostInfoSet == null) oldHostInfoSet = Sets.newHashSet();
            Set<HostInfo> toAddSet = Sets.difference(hostInfoSet, oldHostInfoSet);
            Set<HostInfo> toRemoveSet = Sets.difference(oldHostInfoSet, hostInfoSet);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("service hosts changed, to added hosts:" + toAddSet);
                LOGGER.debug("service hosts changed, to removed hosts:" + toRemoveSet);
            }
            for (HostInfo hostInfo : toAddSet) {
                RegisterListenManager.providerChanged(serviceName, hostInfo.getHost(),
                        hostInfo.getPort(), hostInfo.getWeight(), ProviderChangeEnum.ADD);
            }
            for (HostInfo hostInfo : toRemoveSet) {
                RegisterListenManager.providerChanged(serviceName, hostInfo.getHost(),
                        hostInfo.getPort(), hostInfo.getWeight(), ProviderChangeEnum.REMOVE);
            }
        } catch (Exception e) {
            LOGGER.error("DefaultServiceChangeListener: onServiceAddressChange error", e);
        }
    }

    @Override
    public synchronized void onWeightChange(String host, int weight) {
        HostInfo hostInfo = parseAddress(host);
        RegisterListenManager.weightChanged(hostInfo.getHost(), hostInfo.getPort(), weight, ProviderChangeEnum.REMOVE);
    }

    private Set<HostInfo> parseHostInfos(String serviceName, List<String> hosts) {
        Set<HostInfo> result = Sets.newHashSet();
        if (StringUtils.isBlank(serviceName) || CollectionUtils.isEmpty(hosts)) return result;
        HostInfo hostInfo = null;
        for (String host : hosts) {
            hostInfo = parseAddress(host);
            hostInfo.setServiceName(serviceName);
            hostInfo.setWeight(RegisterManager.getInstance().getServiceWeight(serviceName, hostInfo.getConnect()));
            result.add(hostInfo);
        }
        return result;
    }

    private HostInfo parseAddress(String address) {
        HostInfo hostInfo = new HostInfo();
        String[] tmp = address.split(Constants.COLON_SYMBOL);
        hostInfo.setHost(tmp[0]);
        hostInfo.setPort(Integer.valueOf(tmp[1]));
        return hostInfo;
    }
}
