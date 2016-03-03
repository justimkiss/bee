package com.bee.remote.netty.invoker;

import com.bee.remote.invoker.Client;
import com.bee.remote.netty.invoker.codec.InvokerDecoder;
import com.bee.remote.netty.invoker.codec.InvokerEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by jeoy.zhou on 2/16/16.
 */
public class NettyClientChannelInitializer  extends ChannelInitializer<SocketChannel> {

    private Client client;

    public NettyClientChannelInitializer(Client client) {
        this.client = client;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("clientEncoder", new InvokerEncoder());
        ch.pipeline().addLast("clientDecoder", new InvokerDecoder());
        ch.pipeline().addLast("clientHandler", new NettyClientHandler(client));
    }
}
