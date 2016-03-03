package com.bee.register.zookeeper.test;

import com.bee.register.zookeeper.AbstractTest;
import com.bee.register.zookeeper.CuratorRegister;
import org.junit.Test;

import java.util.List;

/**
 * Created by jeoy.zhou on 12/24/15.
 */
public class CuratorTest extends AbstractTest{

    @Test
    public void curatorTest() throws InterruptedException {
        CuratorRegister register = new CuratorRegister();
        register.init();
        String serviceName = "/test";
        String serviceAddress = "127.0.0.1:8080";
//        register.registerService(serviceName, serviceAddress);


        Thread.sleep(Integer.MAX_VALUE);

    }


    @Test
    public void getChildrenList() throws Exception {
        CuratorRegister register = new CuratorRegister();
        register.init();

        List<String> nodes = register.getServiceAddressList("/memberservice");
        for(String node : nodes) {
            System.out.println(node);
        }
    }
}
