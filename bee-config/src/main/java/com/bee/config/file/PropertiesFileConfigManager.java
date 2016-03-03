package com.bee.config.file;

import com.bee.config.ConfigManager;
import com.bee.config.utills.PropertiesUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeoy.zhou on 12/7/15.
 */
public class PropertiesFileConfigManager implements ConfigManager {

    private static final Logger LOGGER = Logger.getLogger(PropertiesFileConfigManager.class);
    private static final String PROPERTIES_PATH = "classpath:config/bee/bee.properties";
    private static final String GLOBAL_PROPERTIES_PATH = "/data/webapps/config/bee/bee.properties";

    private String ip = null;
    private Map<String, String> config;

    public PropertiesFileConfigManager() {
        init();
    }

    private void init() {
        config = new HashMap<String, String>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = null;
        try {
            file = ResourceUtils.getFile(PROPERTIES_PATH);
        } catch (FileNotFoundException e) {
            LOGGER.warn(String.format("file not found, path:[%s]", PROPERTIES_PATH), e);
            file = new File(GLOBAL_PROPERTIES_PATH);
            if(!file.exists() || !file.isFile()) {
                LOGGER.warn(String.format("file not found, path:[%s]", PROPERTIES_PATH));
                file = null;
            }
        }
        if(file == null) return;
        PropertiesUtils.convertPropertiesToMap(config, file);
        ip = (String) config.get("ip");
    }

    @Override
    public String getServiceAddress() {
        return ip;
    }

    @Override
    public String getConfig(String key) {
        return config.get(key);
    }

    @Override
    public String toString() {
        return "PropertiesFileConfigManager";
    }
}
