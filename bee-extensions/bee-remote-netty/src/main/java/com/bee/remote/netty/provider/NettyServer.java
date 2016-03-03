package com.bee.remote.netty.provider;


import com.bee.common.constants.Constants;
import com.bee.common.thread.NamedThreadFactory;
import com.bee.common.util.NetUtils;
import com.bee.remote.provider.config.ProviderConfig;
import com.bee.remote.provider.config.ServiceConfig;
import com.bee.remote.provider.server.AbstractServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by jeoy.zhou on 12/14/15.
 */
public class NettyServer extends AbstractServer{

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() + 2;
    private String ip;
    private int port;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;
    private EventLoopGroup boss;
    private EventLoopGroup workers;
    private volatile boolean isStart = false;

    public NettyServer() {
        this.serverBootstrap = new ServerBootstrap();
        boss = new NioEventLoopGroup(THREAD_COUNT, new NamedThreadFactory());
        workers = new NioEventLoopGroup(THREAD_COUNT, new NamedThreadFactory());
        this.serverBootstrap.group(boss, workers)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerChannelInitializer(this))
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
    }

    @Override
    public void doStart(ServiceConfig serviceConfig) {
        if(!isStart) {
            if(NetUtils.isPortOnUse(serviceConfig.getPort())) {
                LOGGER.error(String.format("NettyServer: port[%d] is used", serviceConfig.getPort()));
                System.exit(0);
            }
            InetSocketAddress address = new InetSocketAddress(serviceConfig.getPort());
            try {
                channelFuture = this.serverBootstrap.bind(serviceConfig.getPort()).sync();
            } catch (InterruptedException e) {
                LOGGER.error(String.format("NettyServer: bind port[%d] error", this.port), e);
            } catch (Exception e) {
                LOGGER.error(String.format("NettyServer: bind port[%d] error", this.port), e);
            }
            isStart = true;
        }
    }

    @Override
    public void doStop() {
        if(!isStart) {
            LOGGER.error(String.format("NettyServer: port[%d] is not start", this.port));
            return;
        }
        doClose();
        isStart = false;
    }

    protected void doClose() {
        if(channelFuture != null) {
            try {
                channelFuture.channel().close().sync();
            } catch (InterruptedException e) {
                LOGGER.error(String.format("NettyServer: port[%d] close error", e));
            }
        }
        if(boss != null) {
            boss.shutdownGracefully();
        }
        if(workers != null) {
            workers.shutdownGracefully();
        }
    }

    @Override
    public <T> void doAddService(ProviderConfig<T> providerConfig) {

    }

    @Override
    public <T> void doRemoteService(ProviderConfig<T> providerConfig) {

    }

    @Override
    public boolean isStart() {
        return isStart;
    }

    @Override
    public String Protocol() {
        return Constants.PROTOCOL_DEFAULT;
    }

    @Override
    public String toString() {
        return "netty server-" + this.port;
    }
}
