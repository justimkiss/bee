package com.bee.remote.netty.invoker;

import com.bee.common.constants.Constants;
import com.bee.common.exception.NetworkException;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.AbstractClient;
import com.bee.remote.invoker.callback.CallBack;
import com.bee.remote.invoker.domain.ConnectInfo;
import com.bee.remote.invoker.thread.HeartBeatTask;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by jeoy.zhou on 12/27/15.
 */
public class NettyClient extends AbstractClient {

    private static final Logger LOGGER = Logger.getLogger(NettyClient.class);
    private static final int CONNECT_TIMEOUT = 15000;

    private ConnectInfo connectInfo;
    private String host;
    private int port;
    private String address;

    private Bootstrap bootstrap;
    private EventLoopGroup workGroup;
    private Channel channel;
    private volatile boolean connected = false;
    private volatile boolean active = true;



    public NettyClient(ConnectInfo connectInfo) {
        this.connectInfo = connectInfo;
        this.host = connectInfo.getHost();
        this.port = connectInfo.getPort();
        this.address = this.host + Constants.COLON_SYMBOL + this.port;

        this.bootstrap = new Bootstrap();
        this.workGroup = new NioEventLoopGroup();
        this.bootstrap.group(this.workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, Constants.DEFAULT_WRITE_BUFFER_HIGH_WATER)
                .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, Constants.DEFAULT_WRITE_BUFFER_LOW_WATER)
                .handler(new NettyClientChannelInitializer(this));

    }


    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public ConnectInfo getConnectInfo() {
        return connectInfo;
    }

    @Override
    public synchronized void connect() {
        if(isConnected()) return;
        if(LOGGER.isInfoEnabled())
            LOGGER.info("client is connecting to " + this.address);
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect(this.host, this.port);
            if(channelFuture.awaitUninterruptibly(CONNECT_TIMEOUT, TimeUnit.SECONDS)) {
                if(channelFuture.isSuccess()) {
                    Channel newChannel = channelFuture.channel();
                    try {
                        Channel oldChannel = this.channel;
                        if (oldChannel != null) {
                            if(LOGGER.isInfoEnabled())
                                LOGGER.info("close old netty channel " + oldChannel);
                            oldChannel.close();
                        }
                    } finally {
                        this.channel = newChannel;
                    }
                    if(LOGGER.isInfoEnabled())
                        LOGGER.info("client is connected to " + this.address);
                    this.connected = true;
                } else {
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("client is not connected to " + this.address);
                }
            } else {
                if(LOGGER.isInfoEnabled())
                    LOGGER.info("timeout while connecting to " + this.address);
            }
        } catch (Exception e) {
            LOGGER.error("error while connecting to " + this.address, e);
        }
    }

    @Override
    public void close() {
        if(LOGGER.isInfoEnabled())
            LOGGER.info("close client: " + this.address);
        this.active = false;
        this.connected = false;
        channel.close();
        workGroup.shutdownGracefully();
    }

    @Override
    public boolean isActive() {
        return this.active && HeartBeatTask.isActiveAddress(this.address);
    }

    @Override
    public void setActive(boolean isActive) {
        this.active = isActive;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NettyClient) {
            NettyClient nc = (NettyClient) obj;
            return this.address.equals(nc.getAddress());
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @Override
    public String toString() {
        return this.getAddress() + ", connected:" + this.isConnected() + ", active:" + this.isActive();
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public InvocationResponse doSendRequest(InvocationRequest request, CallBack callback) throws NetworkException {
        if (channel == null) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("client channel is null");
            return null;
        }
        ChannelFuture channelFuture = null;
        try {
            channelFuture = channel.writeAndFlush(request);
        } catch (Exception e) {
            throw new NetworkException("remote call failed:" + request, e);
        }
        return null;
    }

}
