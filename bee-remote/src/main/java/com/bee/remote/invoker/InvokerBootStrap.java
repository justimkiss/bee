package com.bee.remote.invoker;

import com.bee.register.RegisterManager;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 1/5/16.
 */
public class InvokerBootStrap {

    private static final Logger LOGGER = Logger.getLogger(InvokerBootStrap.class);
    private static boolean isStart = false;

    public static boolean isStart() {
        return isStart;
    }

    public static void start() {
        if(isStart) return;
        synchronized (InvokerBootStrap.class) {
            if(isStart) return;
            RegisterManager.getInstance();
            // TODO
        }
    }

}
