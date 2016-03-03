package com.bee.remote.netty.provider;

import com.bee.common.constants.Constants;
import com.bee.remote.common.codec.domain.InvocationRequest;
import com.bee.remote.provider.domain.DefaultProviderContext;
import com.bee.remote.provider.domain.ProviderContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * Created by jeoy.zhou on 2/29/16.
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(NettyServerHandler.class);

    private NettyServer nettyServer;

    public NettyServerHandler(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof InvocationRequest) {
            InvocationRequest request = (InvocationRequest) msg;
            ProviderContext providerContext = new DefaultProviderContext(request, new NettyChannel(ctx.channel()));
            try {
                this.nettyServer.processResponse(request, providerContext);
            } catch (Exception e) {
                String errorMsg = "process request failed:" + request;
                if (request.getCallType() == Constants.CALL_BACK_TYPE_REPLY
                        && request.getMessageType() != Constants.MESSAGE_TYPE_HEART) {
                    ctx.channel().writeAndFlush(errorMsg);
                }
                LOGGER.error(errorMsg, e);
            }
        } else {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("channelRead: msg not instanceof InvocationRequest");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        LOGGER.error(e.getMessage(), e);
    }
}
