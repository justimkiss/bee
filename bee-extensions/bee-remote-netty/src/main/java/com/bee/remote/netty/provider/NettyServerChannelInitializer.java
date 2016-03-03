package com.bee.remote.netty.provider;

import com.bee.remote.netty.provider.codec.ProviderDecoder;
import com.bee.remote.netty.provider.codec.ProviderEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


/**
 * Created by jeoy.zhou on 12/27/15.
 */
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private NettyServer nettyServer;

    public NettyServerChannelInitializer(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("serverDecoder", new ProviderDecoder());
        ch.pipeline().addLast("serverEncoder", new ProviderEncoder());
        ch.pipeline().addLast("serverHandler", new NettyServerHandler(this.nettyServer));
    }
}
