package com.bee.remote.invoker.thread;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.DefaultRequest;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.Client;
import com.bee.remote.invoker.callback.impl.CallBackFuture;
import com.bee.remote.invoker.listener.ClusterListenerManager;
import com.bee.remote.invoker.utils.InvokerUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jeoy.zhou on 2/15/16.
 */
public class HeartBeatTask implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(HeartBeatTask.class);
    private static final String HEART_TASK_SERVICE = "BeeHeartBeatService";
    private static final String HEART_TASK_METHOD = "heartBeat";
    private static final int heartBeatHealthCount = 5;
    private static final int heartBeatDeadCount = 5;
    private static volatile Set<String> inactiveAddresses = new HashSet<String>();
    private static final ConcurrentHashMap<String, HeartBeanStat> HEART_BEAN_STAT_MAP = new ConcurrentHashMap<String, HeartBeanStat>();
    private final Map<String, List<Client>> WORKING_CLIENTS;
    private final AtomicLong heartSeq = new AtomicLong();

    public HeartBeatTask(Map<String, List<Client>> workingClients) {
        this.WORKING_CLIENTS = workingClients;
    }

    @Override
    public void run() {
        if (!Thread.currentThread().isInterrupted()) {
            try {
                if(WORKING_CLIENTS != null) {
                    Set<Client> clients = new HashSet<Client>();
                    for(List<Client> tmpClients : WORKING_CLIENTS.values()) {
                        if(CollectionUtils.isNotEmpty(tmpClients))
                            clients.addAll(tmpClients);
                    }
                    for(Client client : clients) {
                        if(LOGGER.isDebugEnabled()) {
                            LOGGER.debug("[heartbeat] checking service provider:" + client);
                        }
                        if(client.isConnected()) {
                            sendHeartBeatRequest(client);
                        } else {
                            if(LOGGER.isInfoEnabled()) {
                                LOGGER.info("[heartbeat] remove connect:" + client);
                            }
                            ClusterListenerManager.getInstance().removeConnect(client);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("[heartbeat] task failed:", e);
            }
        }
    }

    public static boolean isActiveAddress(String address) {
        return !inactiveAddresses.contains(address);
    }


    /**
     * 发送心跳请求
     * @param client
     */
    private void sendHeartBeatRequest(Client client) {
        HeartBeanStat heartBeanStat = getHeartBeanStatWithCreate(client.getAddress());
        InvocationRequest request = createHeartRequest(client);
        try {
            InvocationResponse response = null;
            CallBackFuture callBack = new CallBackFuture();
            response = InvokerUtils.sendRequest(client, request, callBack);
            if (response == null) {
                response = callBack.get(Constants.DEFAULT_HEARTBEAT_TIMEOUT);
            }
            if (response != null && request.getSeq() == response.getSeq()) {
                heartBeanStat.incrSucceedAndGet();
            } else {
                heartBeanStat.incrFailedAndGet();
            }
        } catch (Exception e) {
            heartBeanStat.incrFailedAndGet();
            e.printStackTrace();
        }
        notifyHeartBeatStatChanged(client);
    }

    private void notifyHeartBeatStatChanged(Client client) {
        try {
            HeartBeanStat heartBeanStat = HEART_BEAN_STAT_MAP.get(client.getAddress());
            if (heartBeanStat.successCount.longValue() >= heartBeatHealthCount) {
                if (!client.isActive()) {
                    client.setActive(true);
                    inactiveAddresses.remove(client.getAddress());
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("@service-activate:" + client + ",  inactive addresses:" + inactiveAddresses);
                }
            } else if (heartBeanStat.failCount.longValue() >= heartBeatDeadCount) {
                if (client.isActive()) {
                    client.setActive(false);
                    inactiveAddresses.add(client.getAddress());
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("@service-deactivate:" + client + ", inactive addresses:" + inactiveAddresses);
                }
            }
            heartBeanStat.reset();
        } catch (Exception e) {
            LOGGER.error("notifyHeartBeatStatChanged error", e);
        }
    }

    private HeartBeanStat getHeartBeanStatWithCreate(String connect) {
        HeartBeanStat heartBeanStat = HEART_BEAN_STAT_MAP.get(connect);
        if (heartBeanStat == null) {
            HeartBeanStat newStat = new HeartBeanStat(connect);
            heartBeanStat = HEART_BEAN_STAT_MAP.putIfAbsent(connect, newStat);
            if (heartBeanStat == null) {
                heartBeanStat = newStat;
            }
        }
        return heartBeanStat;
    }

    private InvocationRequest createHeartRequest(Client client) {
        InvocationRequest request = new DefaultRequest();
        request.setCreateMillisTime(System.currentTimeMillis());
        request.setCallType(Constants.CALL_BACK_TYPE_REPLY);
        request.setServiceName(HEART_TASK_SERVICE + client.getAddress());
        request.setMethodName(HEART_TASK_METHOD);
        request.setSerialize(Constants.SERIALIZER_PROTO);
        request.setMessageType(Constants.MESSAGE_TYPE_HEART);
        request.setTimeout(Constants.DEFAULT_HEARTBEAT_TIMEOUT);
        request.setSeq(-heartSeq.incrementAndGet());
        return request;
    }


    class HeartBeanStat {
        private String address;
        private AtomicLong successCount = new AtomicLong();
        private AtomicLong failCount = new AtomicLong();

        public HeartBeanStat(String address) {
            this.address = address;
        }

        public long incrSucceedAndGet() {
            failCount.set(0l);
            return successCount.incrementAndGet();
        }

        public long incrFailedAndGet() {
            successCount.set(0l);
            return failCount.incrementAndGet();
        }

        public void reset() {
            successCount.set(0l);
            failCount.set(0l);
        }
    }

}
