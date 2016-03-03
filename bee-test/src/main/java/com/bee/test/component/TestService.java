package com.bee.test.component;

import org.springframework.stereotype.Service;

/**
 * Created by jeoy.zhou on 12/24/15.
 */
@Service("testService")
public class TestService {

    public void test() {
        System.out.println("this is a test service");
    }
}
