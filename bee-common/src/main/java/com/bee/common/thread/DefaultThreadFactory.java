package com.bee.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeoy.zhou on 12/23/15.
 */
public class DefaultThreadFactory implements ThreadFactory{

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private AtomicInteger threadNumber;
    private ThreadGroup threadGroup;
    private String namePrefix;
    private boolean isDaemon = true;

    public DefaultThreadFactory() {
        this("Default-thread");
    }

    public DefaultThreadFactory(String namePrefix) {
        this(namePrefix, true);
    }

    public DefaultThreadFactory(String namePrefix, boolean isDaemon) {
        this.isDaemon = isDaemon;
        this.threadNumber = new AtomicInteger(1);
        Integer poolnum = poolNumber.getAndIncrement();
        this.namePrefix = namePrefix + "-" + poolnum + "-thread-";
        this.threadGroup = new ThreadGroup(namePrefix + "-" + poolnum + "-threadGroup");
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(this.threadGroup, r, namePrefix + this.threadNumber.getAndIncrement());
        thread.setDaemon(this.isDaemon);
        if(thread.getPriority() != 5) {
            thread.setPriority(5);
        }
        return thread;
    }

    public ThreadGroup getThreadGroup() {
        return this.threadGroup;
    }
}
