package com.bee.remote.common.codec.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by jeoy.zhou on 1/31/16.
 */
public class DefaultResponse implements InvocationResponse{

    private static final long serialVersionUID = 4092100209956938207L;
    private int messageType;
    private String cause;
    private Object returnVal;
    private Object context;
    private transient int size;
    private long seq;
    private byte serialize;
    private Map<String, Serializable> responseValues = null;

    public DefaultResponse() {}

    public DefaultResponse(int messageType, byte serialize) {
        this.messageType = messageType;
        this.serialize = serialize;
    }

    public DefaultResponse(byte serialize, long seq, int messageType, Object returnVal) {
        this.serialize = serialize;
        this.seq = seq;
        this.messageType = messageType;
        this.returnVal = returnVal;
    }


    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public Object getContext() {
        return context;
    }

    @Override
    public void setContext(Object context) {
        this.context = context;
    }

    public int size() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    @Override
    public int getMessageType() {
        return this.messageType;
    }

    @Override
    public String getCause() {
        return this.cause;
    }

    @Override
    public Object getReturn() {
        return this.returnVal;
    }

    @Override
    public void setReturn(Object obj) {
        this.returnVal = obj;
    }

    @Override
    public Map<String, Serializable> getResponseValues() {
        return this.responseValues;
    }

    @Override
    public void setResponseValues(Map<String, Serializable> responseValues) {
        this.responseValues = responseValues;
    }

    @Override
    public long getSeq() {
        return seq;
    }

    @Override
    public void setSeq(long seq) {
        this.seq = seq;
    }

    @Override
    public byte getSerialize() {
        return serialize;
    }

    @Override
    public void setSerialize(byte serialize) {
        this.serialize = serialize;
    }
}
