package com.bee.config.loader;

import com.bee.common.extension.ExtensionLoader;
import com.bee.config.ConfigManager;
import com.bee.config.file.PropertiesFileConfigManager;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 12/5/15.
 */
public class ConfigManagerLoader {

    private static final Logger LOGGER = Logger.getLogger(ConfigManagerLoader.class);
    private static ConfigManager CONFIG_MANAGER = ExtensionLoader.getExtension(ConfigManager.class);

    static {
        if(CONFIG_MANAGER == null) {
            CONFIG_MANAGER = new PropertiesFileConfigManager();
        }
        LOGGER.info(CONFIG_MANAGER);
    }

    public static ConfigManager getConfigManager() {
        return CONFIG_MANAGER;
    }
}
