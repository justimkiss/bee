package com.bee.register.zookeeper.test;

import com.bee.register.RegisterManager;
import org.junit.Test;

/**
 * Created by jeoy.zhou on 3/4/16.
 */
public class CuratorClientTest {

    @Test
    public void get() throws InterruptedException {
        RegisterManager registerManager = RegisterManager.getInstance();
        registerManager.unregisterService("com.bee.remote.test.component.testService_1.0.0", "10.227.24.70:3344");
        Thread.sleep(Long.MAX_VALUE);
    }
}
