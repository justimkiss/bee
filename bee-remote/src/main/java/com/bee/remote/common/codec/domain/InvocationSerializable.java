package com.bee.remote.common.codec.domain;

import java.io.Serializable;

/**
 * Created by jeoy.zhou on 2/2/16.
 */
public interface InvocationSerializable extends Serializable{

    public int getMessageType();

    public void setMessageType(int messageType);

    public byte getSerialize();

    public void setSerialize(byte serialize);

    public long getSeq();

    public void setSeq(long seq);

    public int size();

    public void setSize(int size);

    public Object getContext();

    public void setContext(Object context);

}
