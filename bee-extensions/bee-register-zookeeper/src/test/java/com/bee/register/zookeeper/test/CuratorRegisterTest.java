package com.bee.register.zookeeper.test;

import com.bee.register.zookeeper.AbstractTest;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

/**
 * Created by jeoy.zhou on 12/18/15.
 */
public class CuratorRegisterTest extends AbstractTest{

    private static final String connectStr = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
//    @Test
    public void testCurator() throws Exception {
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        CuratorFramework client = CuratorFrameworkFactory.newClient(connectStr, 5000, 3000, retryPolicy);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                                    .connectString(connectStr)
                                    .sessionTimeoutMs(500000)
                                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                                    .namespace("test")
                                    .build();
        client.start();
        byte[] bytes = client.getData().forPath("/member");
        System.out.println(bytes);
        Thread.sleep(100000);
    }

//    @Test
    public void testListenerCache() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectStr)
                .sessionTimeoutMs(500000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("test")
                .build();
        client.start();

        nodeCache(client, "/second");
        client.create().forPath("/second");
        Thread.sleep(Integer.MAX_VALUE);
    }

    private void nodeCache(CuratorFramework client, String path) throws Exception {
        final NodeCache nodeCache = new NodeCache(client, path, false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("node cache:" + nodeCache.getCurrentData().getData());
            }
        });
    }

    @Test
    public void testListener() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectStr)
                .sessionTimeoutMs(500000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("test")
                .build();
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("node state: " + newState);
            }
        });
        client.getCuratorListenable().addListener(new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println("===========event-start==========");
                System.out.println(event);
                System.out.println(event.getData());
                System.out.println(event.getType());
                System.out.println(event.getPath());
                System.out.println("===========event-end==========");
            }
        });
        client.start();
        System.out.println("main thread: start");
        boolean isConnect = client.getZookeeperClient().blockUntilConnectedOrTimedOut();
        System.out.println(isConnect);
        System.out.println("main thread: end");
        System.out.println("result for first:" + new String(client.getData().watched().forPath("/first")));
        Thread.sleep(1000000);
    }

    private void createNode(CuratorFramework client, String path) throws Exception {
        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            throw  e;
        }
    }

}
