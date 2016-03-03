package com.bee.register.config;

import com.bee.common.constants.Constants;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jeoy.zhou on 3/3/16.
 */
public class DefaultRegisterConfigManager implements RegisterConfigManager {

    private static final Logger LOGGER = Logger.getLogger(DefaultRegisterConfigManager.class);

    private static final String ZOOKEEPER_CONFIG_PATH = Constants.ZOOKEEPER_CONFIG_PATH;
    private static final ClassLoader CLASS_LOADER = DefaultRegisterConfigManager.class.getClassLoader();

    @Override
    public Properties getRegisterConfig() {
        InputStream is = CLASS_LOADER.getResourceAsStream(ZOOKEEPER_CONFIG_PATH);
        if (is == null) {
            LOGGER.error("not find resource: " + ZOOKEEPER_CONFIG_PATH);
            throw new IllegalArgumentException("not find resource: " + ZOOKEEPER_CONFIG_PATH);
        }
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new IllegalArgumentException("not find resource: " + ZOOKEEPER_CONFIG_PATH);
        }
        return properties;
    }

}

