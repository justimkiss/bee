package com.bee.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by jeoy.zhou on 12/24/15.
 */
@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/spring-server.xml"})
public abstract class AbstractTest {
}
