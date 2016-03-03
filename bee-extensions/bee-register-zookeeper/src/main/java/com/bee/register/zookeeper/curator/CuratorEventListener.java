package com.bee.register.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.log4j.Logger;


/**
 * Created by jeoy.zhou on 12/23/15.
 */
public class CuratorEventListener implements CuratorListener{

    private static final Logger LOGGER = Logger.getLogger(CuratorEventListener.class);

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {


    }
}
