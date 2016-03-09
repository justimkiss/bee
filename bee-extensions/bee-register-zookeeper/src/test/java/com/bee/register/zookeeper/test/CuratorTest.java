package com.bee.register.zookeeper.test;

import com.bee.register.RegisterManager;
import com.bee.register.zookeeper.AbstractTest;

import java.util.List;

/**
 * Created by jeoy.zhou on 12/24/15.
 */
public class CuratorTest extends AbstractTest{

//    @Test
    public void curatorTest() throws InterruptedException {
        RegisterManager registerManager = RegisterManager.getInstance();
        registerManager.unregisterService("com.bee.remote.test.component.testService_1.0.0", "10.227.24.70:3344");
        Thread.sleep(Integer.MAX_VALUE);
    }

//    @Test
    public void getChildrenList() throws Exception {
        RegisterManager registerManager = RegisterManager.getInstance();
        List<String> nodes = registerManager.getServiceAddressList("/memberservice");
        for(String node : nodes) {
            System.out.println(node);
        }
    }
}
