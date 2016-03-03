package com.bee.remote.exception;

/**
 * Created by jeoy.zhou on 2/23/16.
 */
public class NetException extends RuntimeException{

    private static final long serialVersionUID = -2958396570529210829L;

    public NetException(){
        super();
    }

    public NetException(String message){
        super(message);
    }

    public NetException(Throwable cause){
        super(cause);
    }

    public NetException(String message, Throwable cause){
        super(message,cause);
    }
}
