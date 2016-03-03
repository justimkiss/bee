package com.bee.remote.netty.provider;

import com.bee.remote.common.codec.domain.InvocationResponse;
import com.bee.remote.provider.domain.ProviderChannel;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * Created by jeoy.zhou on 3/1/16.
 */
public class NettyChannel implements ProviderChannel{

    private static final Logger LOGGER = Logger.getLogger(NettyChannel.class);

    private Channel channel;

    public NettyChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String getRemoteAddress() {
        InetSocketAddress address = (InetSocketAddress) this.channel.remoteAddress();
        return address.getAddress().getHostAddress();
    }

    @Override
    public void write(InvocationResponse response) {
        if (this.channel == null) {
            LOGGER.error("channel is null");
            return;
        }
        this.channel.writeAndFlush(response);
    }
}
