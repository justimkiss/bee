package com.bee.test.spring;

import com.bee.test.JUnit4ClassRunner;
import com.bee.test.component.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by jeoy.zhou on 12/24/15.
 */
@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/spring-server2.xml"})
public class ServiceRegisterTest2 {

    @Autowired
    private TestService testService;

    @Test
    public void testRegister() throws InterruptedException {
        testService.test();
        Thread.sleep(Long.MAX_VALUE);
    }

}
