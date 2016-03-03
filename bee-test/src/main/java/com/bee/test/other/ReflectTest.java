package com.bee.test.other;

import com.bee.test.protostuff.User;
import org.junit.Test;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public class ReflectTest {

    @Test
    public void test() throws IllegalAccessException, InstantiationException {
        Class<User> clazz = User.class;
        long start = System.currentTimeMillis();
        for(int i=0; i<10000000; i++) {
            User user = clazz.newInstance();
        }
        long end = System.currentTimeMillis();
        System.out.println("reflect: " + (end - start));
    }

    @Test
    public void test1() throws IllegalAccessException, InstantiationException {
        Objenesis objenesis = new ObjenesisStd(true);
        Class<User> clazz = User.class;
        long start = System.currentTimeMillis();
        for(int i=0; i<10000000; i++) {
            User user = objenesis.newInstance(clazz);
        }
        long end = System.currentTimeMillis();
        System.out.println("objenesis: " + (end - start));
    }
}
