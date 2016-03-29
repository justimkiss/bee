package com.jeoy.bee.monitor;

/**
 * Created by jeoy.zhou on 3/29/16.
 */
public interface Monitor {

    public void init();

    public void logError(Throwable e);

    public void logError(String msg, Throwable e);

    public void logEvent(String name, String event, String desc);

    public void logMonitorError(Throwable e);

}
