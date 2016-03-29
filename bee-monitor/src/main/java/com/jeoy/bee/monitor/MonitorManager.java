package com.jeoy.bee.monitor;

import com.bee.common.extension.ExtensionLoader;
import com.jeoy.bee.monitor.impl.SimpleMonitor;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 3/29/16.
 */
public final class MonitorManager {

    private static final Logger LOGGER = Logger.getLogger(MonitorManager.class);
    private static Monitor monitor = ExtensionLoader.getExtension(Monitor.class);

    static {
        if (monitor == null) {
            monitor = new SimpleMonitor();
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("monitor:" + monitor);
        monitor.init();
    }

    public static Monitor getMonitor() {
        return monitor;
    }
}
