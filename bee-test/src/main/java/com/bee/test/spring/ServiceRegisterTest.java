package com.bee.test.spring;

import com.bee.test.AbstractTest;
import com.bee.test.component.TestService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jeoy.zhou on 12/24/15.
 */
public class ServiceRegisterTest extends AbstractTest{

    @Autowired
    private TestService testService;

    @Test
    public void testRegister() {
        testService.test();
    }

}
