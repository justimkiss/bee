package com.jeoy.bee.monitor.impl;

import com.jeoy.bee.monitor.Monitor;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 3/29/16.
 */
public class SimpleMonitor implements Monitor {

    private static final Logger LOGGER = Logger.getLogger(SimpleMonitor.class);

    @Override
    public void init() {

    }

    @Override
    public void logError(Throwable e) {

    }

    @Override
    public void logError(String msg, Throwable e) {

    }

    @Override
    public void logEvent(String name, String event, String desc) {

    }

    @Override
    public void logMonitorError(Throwable e) {

    }

    @Override
    public String toString() {
        return "simpleMonitor";
    }
}
