package com.bee.config;

/**
 * Created by jeoy.zhou on 12/5/15.
 */
public interface ConfigManager {

    public String getServiceAddress();

    public String getConfig(String key);

}
