package com.bee.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeoy.zhou on 12/14/15.
 */
public class NamedThreadFactory implements ThreadFactory{

    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);
    private final String prefix;
    private final ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("bee");
    }

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix + "-thread-";
        SecurityManager securityManager = System.getSecurityManager();
        this.threadGroup = (securityManager == null) ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = prefix + POOL_SEQ.getAndIncrement();
        Thread thread = new Thread(threadGroup, r, threadName, 0);
        return thread;
    }
}
