package com.bee.test.other;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jeoy.zhou on 2/15/16.
 */
public class ThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new Task(), 1l, 1l, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new TaskOne(), 1l, 1l, TimeUnit.SECONDS);
        Thread.sleep(Integer.MAX_VALUE);
    }

    private static class Task implements Runnable{

        @Override
        public void run() {
            System.out.println("test");
        }
    }

    private static class TaskOne implements Runnable {

        @Override
        public void run() {
            System.out.println("task one");
        }
    }
}
