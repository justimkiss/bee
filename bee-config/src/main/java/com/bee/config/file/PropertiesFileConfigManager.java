package com.bee.config.file;

import com.bee.common.constants.Constants;
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
    private static final String PROPERTIES_PATH = Constants.BEE_CONFIG_PATH;
    private static final String GLOBAL_PROPERTIES_PATH = Constants.GLOBAL_BEE_CONFIG_PATH;

    private String ip;
    private Map<String, String> config;

    public PropertiesFileConfigManager() {
        init();
    }

    private void init() {
        config = new HashMap<String, String>();
        File file = null;
        try {
            file = ResourceUtils.getFile(PROPERTIES_PATH);
        } catch (FileNotFoundException e) {
            LOGGER.warn(String.format("file not found, path:[%s]", PROPERTIES_PATH));
            file = new File(GLOBAL_PROPERTIES_PATH);
            if(!file.exists() || !file.isFile()) {
                LOGGER.warn(String.format("file not found, path:[%s]", PROPERTIES_PATH));
                file = null;
            }
        }
        if(file == null) return;
        PropertiesUtils.convertPropertiesToMap(config, file);
        ip = config.get("ip");
    }


    @Override
    public String getLocalIP() {
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
