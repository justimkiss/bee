package com.bee.register.zookeeper;


import com.bee.common.thread.DefaultThreadFactory;
import com.bee.register.zookeeper.curator.CuratorEventListener;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jeoy.zhou on 12/22/15.
 */
public class CuratorClient {

    private static final Logger LOGGER = Logger.getLogger(CuratorClient.class);
    private static final String CHARSET_UTF8 = "UTF-8";
    private CuratorFramework client;
    private static ExecutorService curatorEventThreadPool = Executors.newCachedThreadPool(
            new DefaultThreadFactory("Bee-curator-event-listener"));


    public CuratorClient(CuratorParam curatorParam) throws InterruptedException {
        CuratorFramework newClient = CuratorFrameworkFactory.builder()
                .sessionTimeoutMs(curatorParam.getSessionTimeOut())
                .connectionTimeoutMs(curatorParam.getConnectTimeOut())
                .connectString(curatorParam.getAddress())
                .retryPolicy(new RetryNTimes(curatorParam.getRetries(), curatorParam.getRetryInterval()))
                .namespace(curatorParam.getNameSpace())
                .build();
        newClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                if(LOGGER.isInfoEnabled())
                    LOGGER.info("zookeeper client status change to: " + newState.toString());
            }
        });
        newClient.getCuratorListenable().addListener(new CuratorEventListener(), curatorEventThreadPool);
        newClient.start();
        boolean isConnect = newClient.getZookeeperClient().blockUntilConnectedOrTimedOut();
        CuratorFramework oldClient = this.client;
        closeCuratorClient(oldClient);
        this.client = newClient;
        if(isConnect) {
            if(LOGGER.isInfoEnabled())
                LOGGER.info("CuratorClient: already connected");
        } else {
            LOGGER.error("CuratorClient: failed to connect zookeeper server");
        }
    }


    /**
     * 获取节点数据
     * @param path
     * @return
     * @throws Exception
     */
    public String get(String path) throws Exception {
        byte[] bytes = null;
        try {
            bytes = this.client.getData().forPath(path);
            String result = new String(bytes, CHARSET_UTF8);
            if(LOGGER.isInfoEnabled())
                LOGGER.debug(String.format("CuratorClient: getData success path[%s], value[%s]", path, result));
            return result;
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            LOGGER.error(String.format("CuratorClient: getData fail path[%s]", path));
            return null;
        }
    }

    public void set(String path, String value) throws Exception {
        if(StringUtils.isBlank(path))
            throw new IllegalArgumentException("CuratorClient: set param[path] is null");
        if(exists(path)) {
            byte[] vals = null;
            if(StringUtils.isBlank(value)) {
                vals = new byte[0];
            } else {
                vals = value.getBytes(CHARSET_UTF8);
            }
            this.client.setData().forPath(path, vals);
        }else {
            createEphemeralNode(path, value);
        }
        if(LOGGER.isDebugEnabled())
            LOGGER.debug(String.format("CuratorClient: set path[%s], value[%s]", path, value));
    }
    /**
     * 创建永久节点
     * @param path
     * @throws Exception
     */
    public void createPersistentNode(String path) throws Exception {
        createPersistentNode(path, null);
    }

    /**
     * 创建永久节点
     * @param path
     * @param value
     * @throws Exception
     */
    public void createPersistentNode(String path, String value) throws Exception {
        createNode(path, value, CreateMode.PERSISTENT);
    }

    /**
     * 创建临时节点
     * @param path
     * @throws Exception
     */
    public void createEphemeralNode(String path) throws Exception {
        createEphemeralNode(path, null);
    }

    /**
     * 创建临时节点
     * @param path
     * @param value
     * @throws Exception
     */
    public void createEphemeralNode(String path, String value) throws Exception {
        createNode(path, value, CreateMode.EPHEMERAL);
    }

    /**
     * 创建节点
     * @param path
     * @param value
     * @param createMode
     * @throws Exception
     */
    public void createNode(String path, String value, CreateMode createMode) throws Exception {
        if(StringUtils.isBlank(path))
            throw new IllegalArgumentException("CuratorClient: createNode param[path] is null");
        byte[] vals = null;
        if(StringUtils.isBlank(value)) {
            vals = new byte[0];
        } else {
            vals = value.getBytes(CHARSET_UTF8);
        }
        if(createMode == null) {
            createMode = CreateMode.EPHEMERAL;
        }
        this.client.create().creatingParentContainersIfNeeded().withMode(createMode).forPath(path, vals);
        if(LOGGER.isDebugEnabled())
            LOGGER.debug(String.format("CuratorClient: createNode, path[%s], value[%s], createmode[%s]",
                path, value, createMode));
    }

    public void deleteNode(String path) throws Exception {
        if(StringUtils.isBlank(path))
            throw new IllegalArgumentException("CuratorClient: deleteNode param[path] is null");
        this.client.delete().forPath(path);
        if(LOGGER.isDebugEnabled())
            LOGGER.debug(String.format("CuratorClient: deleteNode path[%s] success", path));
    }

    public boolean exists(String path) throws Exception {
        return getNodeStat(path) != null;
    }

    public List<String> getChildrenNodes(String path) throws Exception {
        if(StringUtils.isBlank(path))
            throw new IllegalArgumentException("CuratorClient: getChildrenNodes param[path] is null");
        List<String> childNodes = this.client.getChildren().forPath(path);
        if(LOGGER.isDebugEnabled())
            LOGGER.debug(String.format("CuratorClient: getChildrenNodes path[%s], childrenNodes[%s]", path, childNodes));
        return childNodes;
    }

    /**
     * 获取节点信息
     * @param path
     * @return
     * @throws Exception
     */
    public Stat getNodeStat(String path) throws Exception {
        if(StringUtils.isBlank(path))
            throw new IllegalArgumentException("CuratorClient: getStat param[path] is null");
        Stat stat = this.client.checkExists().forPath(path);
        if(LOGGER.isDebugEnabled())
            LOGGER.debug(String.format("CuratorClient: getStat path[%s]", path));
        return stat;
    }

    private void closeCuratorClient(CuratorFramework client) {
        if(client == null) {
            LOGGER.warn("CuratorClient: client is null, no need close");
            return;
        }
        try {
            client.close();
            if(LOGGER.isInfoEnabled())
                LOGGER.info("CuratorClient: old client close success");
        } catch (Exception e) {
            LOGGER.error("CuratorClient: client close fail", e);
        }
    }

}
