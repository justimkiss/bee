package com.bee.common.constants;

import java.util.regex.Pattern;

/**
 * Created by jeoy.zhou on 12/8/15.
 */
public final class Constants {

    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
    // providerConfig
    public static final Pattern BEE_SERVICE_URL_PATTERN = Pattern.compile("(^[a-zA-Z][a-zA-Z.]{8,})_(\\d[\\d.]*\\d|\\d)$");
//    public static final Pattern BEE_SERVICE_URL_PATTERN = Pattern.compile("(^http://[\\w.]+/[\\w]+/[\\w]+)_(\\d[\\d.]*\\d|\\d)$");

    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();
    public static final int DEFAULT_THREAD_NUM = CPU_NUM + 1;

    // interfaceUtils
    public static final String DEFAULT_INTERFACEUTILS_PACKAGE = "com.bee";

    // serviceConfig port default
    public static final int DEFAULT_PORT = 3333;
    public static final int DEFAULT_HTTP_PORT = 4080;
    public static final String PROTOCOL_HTTP = "http";
    public static final String PROTOCOL_DEFAULT = "default";

    // ServiceMethodCache
    public static final String TRANSFER_NULL = "null";

    public static final String ZOOKEEPER_CONFIG_PATH = "classpath:config/zookeeper/zookeeper.properties";
    public static final String GLOBAL_ZOOKEEPER_CONFIG_PATH = "/data/webapps/config/zookeeper/zookeeper.properties";
    public static final String BEE_CONFIG_PATH = "classpath:config/bee/bee.properties";
    public static final String GLOBAL_BEE_CONFIG_PATH = "/data/webapps/config/bee/bee.properties";

    // curatorParams default config
    public static final String ZOOKEEPER_DEFAULT_NAMESPACE = "bee/server";
    public static final Integer ZOOKEEPER_DEFAULT_RETRY_TIME = 300;
    public static final Integer ZOOKEEPER_DEFAULT_RETRY_INTERVAL = 3000;
    public static final Integer ZOOKEEPER_DEFAULT_SESSION_TIMEOUT = 30000;
    public static final Integer ZOOKEEPER_DEFAULT_CONNECTION_TIMEOUT = 15000;

    // utils service path
    public static final String PATH_SEPARATOR = "/";
    public static final String PLACEHOLDER = "@";
    public static final String DOT_SEPARATOR = ",";
    public static final String COLON_SYMBOL = ":";
    public static final String ZOOKEEPER_PROVIDERS_NODE = "providers";
    public static final String ZOOKEEPER_CONSUMER_NODE = "consumers";
    public static final String ZOOKEEPER_WEIGHT_NODE = "weights";
    public static final Integer DEFAULT_WEIGHT = 5;
    public static final Integer MIN_WEIGHT = 1;
    public static final Integer MAX_WEIGHT = 10;

    // defaultRequest
    public static final Integer CALL_BACK_TYPE_REPLY = 1;
    public static final Integer CALL_BACK_TYPE_NOREPLY = 2;
    // invocationResponse messageType
    public static final Integer MESSAGE_TYPE_HEART = 1;
    public static final Integer MESSAGE_TYPE_SERVICE = 2;
    public static final Integer MESSAGE_TYPE_EXCEPTION = 3;
    public static final Integer MESSAGE_TYPE_SERVICE_EXCEPTION = 4;
    public static final Integer MESSAGE_TYPE_HEART_CHECK = 5;

    // SerializerFactory
    public static final byte SERIALIZER_PROTO = 1;

    public static final String PROTO = "proto";
    // ProxyBeanFactory
    public static final String CALL_SYNC = "sync";
    public static final String CALL_CALLBACK = "callback";
    public static final String CALL_ONEWAY = "oneway";
    public static final String CALL_FUTURE = "future";
    public static final String CLUSTER_FAILFAST = "failfast";
    public static final int CLIENT_RETRIES = 1;
    public static final boolean CLIENT_TIMEOUT_RETRY  = false;
    public static final int CLIENT_CALL_TIMEOUT = 3000;


    // nettyClient
    public static final int DEFAULT_WRITE_BUFFER_HIGH_WATER = 35 * 1024 * 1024;
    public static final int DEFAULT_WRITE_BUFFER_LOW_WATER = 25 * 1024 * 1024;

    public static final int DEFAULT_RESPONSE_PROCESSOR_CORE_SIZE = 10;
    public static final int DEFAULT_RESPONSE_PROCESSOR_MAx_SIZE = 40;
    public static final int DEFAULT_RESPONSE_PROCESSOR_QUEUE_SIZE = 380;

    // serverConfig
    public static final int DEFAULT_PROVIDER_COREPOOLSIZE = 60;
    public static final int DEFAULT_PROVIDER_MAXPOOLSIZE = 500;
    public static final int DEFAULT_PROVIDER_WORKQUEUESIZE = 1000;

    //  requestTimeOutThread
    public static final float DEFAULT_CANCEL_RADIO = 1f;

    //InvocationTimeoutThread mills
    public static final long DEFAULT_INVOCATION_CHECK_INTERVAL = 500;

    //defaultRequest
    public static final boolean REQUEST_LOG_PARAMETERS = true;

    // HeartBeatTask
    public static final int DEFAULT_HEARTBEAT_TIMEOUT = 3000;

    // providerUtils
    public static final int HEART_RESULT = 100;
}
