package com.jeoy.bee.monitor.cat;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.MessageProducer;
import com.jeoy.bee.monitor.Monitor;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jeoy.zhou on 3/29/16.
 */
public class CatMonitor implements Monitor {

    private static final Logger LOGGER = Logger.getLogger(CatMonitor.class);
    private AtomicLong errorCount = new AtomicLong();
    private volatile boolean isInit = false;
    private MessageProducer producer;

    @Override
    public void init() {
        if (!isInit) {
            synchronized (this) {
                if (!isInit) {
                    try {
                        this.producer = Cat.getProducer();
                    } catch (Exception e) {
                        LOGGER.error("producer error", e);
                    }
                    isInit = true;
                }
            }
        }
    }


    @Override
    public void logError(Throwable e) {
        if (e != null) {
            try {
                if (this.producer == null) {
                    this.producer = Cat.getProducer();
                }
                if (this.producer != null) {
                    this.producer.logError(e);
                }
            } catch (Exception t) {
                logMonitorError(t);
            }
        }
    }

    @Override
    public void logError(String msg, Throwable e) {
        if (e != null) {
            try {
                if (this.producer == null) {
                    this.producer = Cat.getProducer();
                }
                if (this.producer != null) {
                    this.producer.logError(msg, e);
                }
            } catch (Exception t) {
                logMonitorError(t);
            }
        }
    }

    @Override
    public void logEvent(String name, String event, String desc) {
        try {
            if (this.producer == null) {
                this.producer = Cat.getProducer();
            }
            if (this.producer != null) {
                this.producer.logEvent(name, event, Event.SUCCESS, desc);
            }
        } catch (Exception e) {
            logMonitorError(e);
        }
    }

    @Override
    public void logMonitorError(Throwable t) {
        try {
            String errorMsg = "[Cat]Monitor pigeon call failed.";
            Long errorCounter = errorCount.incrementAndGet();
            if (errorCounter <= 50) {
                LOGGER.error(errorMsg, t);
            } else if (errorCounter < 1000 && errorCounter % 40 == 0) {
                LOGGER.error(errorMsg, t);
            } else if (errorCounter % 200 == 0) {
                LOGGER.error(errorMsg, t);
            }
        } catch (Throwable e2) {/* do nothing */
        }
    }

    @Override
    public String toString() {
        return "CatMonitor";
    }
}
