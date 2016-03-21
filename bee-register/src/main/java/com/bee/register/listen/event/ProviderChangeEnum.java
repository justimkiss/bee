package com.bee.register.listen.event;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public enum ProviderChangeEnum {

    ADD("add", "add node"),
    REMOVE("remove", "remote node"),
    CHANGE("change", "change node vaule");

    private String key;
    private String desc;

    private ProviderChangeEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return  this.key;
    }

    public String getDesc() {
        return this.desc;
    }
}
