package com.bee.register.zookeeper.curator;

import com.bee.common.constants.Constants;
import com.bee.register.listen.listener.DefaultServiceChangeListener;
import com.bee.register.listen.listener.ServiceChangeListener;
import com.bee.register.zookeeper.CuratorClient;
import com.bee.register.zookeeper.utils.CuratorUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;


/**
 * Created by jeoy.zhou on 12/23/15.
 */
public class CuratorEventListener implements CuratorListener{

    private static final Logger LOGGER = Logger.getLogger(CuratorEventListener.class);
    private static final int ADDRESS = 1;
    private static final int WEIGHT = 2;
    private static final String SERVICE_PATH_TAG = Constants.PATH_SEPARATOR + Constants.ZOOKEEPER_PROVIDERS_NODE;
    private static final String WEIGHT_PATH_TAG = Constants.PATH_SEPARATOR + Constants.ZOOKEEPER_WEIGHT_NODE;
    private static final ServiceChangeListener SERVICE_CHANGE_LISTENER = new DefaultServiceChangeListener();
    private CuratorClient client;

    public CuratorEventListener(CuratorClient client) {
        this.client = client;
    }

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
        WatchedEvent watchedEvent = (event == null) ? null : event.getWatchedEvent();
        if (watchedEvent == null
                || Watcher.Event.EventType.None == watchedEvent.getType())
            return;
        if (LOGGER.isInfoEnabled())
            LOGGER.info(String.format("zookeeper event received, type: %s, path: %s", watchedEvent.getType(), watchedEvent.getPath()));
        try {
            EventInfo eventInfo = parseEventInfo(watchedEvent);
            if (eventInfo == null) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(String.format("no operate event, type: %s, path: %s", watchedEvent.getType(), watchedEvent.getPath()));
                return;
            }
            if (LOGGER.isInfoEnabled())
                LOGGER.info(String.format("zookeeper parse type: %s, eventtype: %s", eventInfo.type, eventInfo.eventType));

            if (eventInfo.type == ADDRESS) {
                // 监听服务提供的更节点
                serviceAddressChange(eventInfo);
            } else if (eventInfo.type == WEIGHT && Watcher.Event.EventType.NodeDataChanged == eventInfo.eventType) {
                // 监听服务提供的权重更节点
                serviceWeightChange(eventInfo);
            } else {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(String.format("no operate event, eventType: %s, path: %s", watchedEvent.getType(), watchedEvent.getPath()));
            }
        } catch (Exception e) {
            LOGGER.error("Error in ZookeeperWatcher.process()", e);
        }
        // 无须再次对节点进行监听，在获取子节点信息时，自动添加监听
    }

    private void serviceAddressChange(EventInfo eventInfo) throws Exception {
        List<String> childList = null;
        if (eventInfo.eventType == Watcher.Event.EventType.NodeChildrenChanged) {
            try {
                childList = this.client.getChildrenNodes(eventInfo.path, true);
            } catch (KeeperException.NoNodeException e) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("not find zookeeper node[path: " + eventInfo.path + "]");
                childList = Lists.newArrayList();
            }
        } else if (eventInfo.eventType == Watcher.Event.EventType.NodeDeleted) {
            childList = Lists.newArrayList();
        } else {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(String.format("no operate event, eventType: %s, path: %s", eventInfo.eventType, eventInfo.path));
            return;
        }
        if (childList == null) return;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Service address changed, path " + eventInfo.path + " value " + childList);
        SERVICE_CHANGE_LISTENER.onServiceAddressChange(eventInfo.serviceName, childList);
    }

    private void serviceWeightChange(EventInfo eventInfo) throws Exception {
        String weight = this.client.get(eventInfo.path, true);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("service weight changed, path " + eventInfo.path + " value " + weight);
        int value = StringUtils.isBlank(weight) ? 0 : Integer.valueOf(weight);
        SERVICE_CHANGE_LISTENER.onWeightChange(eventInfo.path, value);
    }

    private EventInfo parseEventInfo(WatchedEvent watchedEvent) {
        String path = watchedEvent.getPath();
        if (!path.contains(SERVICE_PATH_TAG) && !path.contains(WEIGHT_PATH_TAG)) return null;
        EventInfo eventInfo = new EventInfo();
        eventInfo.path = path;
        eventInfo.eventType = watchedEvent.getType();
        if (path.contains(SERVICE_PATH_TAG)) {
            eventInfo.serviceName = CuratorUtils.unescape(path.substring(0, path.length() - SERVICE_PATH_TAG.length()));
            eventInfo.type = ADDRESS;
        } else if (path.contains(WEIGHT_PATH_TAG)) {
            eventInfo.serviceName = CuratorUtils.unescape(path.substring(0, path.length() - WEIGHT_PATH_TAG.length()));
            eventInfo.type = WEIGHT;
        }
        if (eventInfo.serviceName.startsWith(Constants.PATH_SEPARATOR))
            eventInfo.serviceName = eventInfo.serviceName.substring(1);
        return eventInfo;
    }

    private class EventInfo {
        private String path;
        private String serviceName;
        private int type;
        private Watcher.Event.EventType eventType;

    }
}
