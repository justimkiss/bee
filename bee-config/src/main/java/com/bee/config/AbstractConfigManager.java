package com.bee.config;

import com.bee.common.util.NetUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 3/30/16.
 */
public abstract class AbstractConfigManager implements ConfigManager {

    private static final Logger LOGGER = Logger.getLogger(AbstractConfigManager.class);

    @Override
    public String getLocalIP() {
        String value = null;
        try {
            value = doGetLocalIP();
        } catch (Throwable e) {
            LOGGER.error("error while reading local ip:" + e.getMessage());
        }
        if (StringUtils.isBlank(value)) {
            value = NetUtils.getFirstLocalIp();
        }
        return value;
    }

    protected abstract String doGetLocalIP();

}
