package com.bee.remote.netty.invoker;


import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.invoker.Client;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/24/16.
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(NettyClientHandler.class);

    private Client client;

    public NettyClientHandler(Client client) {
        this.client = client;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        if (msg instanceof InvocationResponse) {
            this.client.processResponse((InvocationResponse) msg);
        }
    }
}
