package com.bee.config.file;

import com.bee.config.ConfigManager;
import com.bee.config.loader.ConfigManagerLoader;

/**
 * Created by jeoy.zhou on 12/7/15.
 */
public class PropertiesFileConfigManagerTest {

    public void testPropertiesFile() {
        ConfigManager configManager = ConfigManagerLoader.getConfigManager();
        System.out.println(configManager);
    }
}
