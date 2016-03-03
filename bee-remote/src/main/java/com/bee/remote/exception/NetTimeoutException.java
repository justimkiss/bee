package com.bee.remote.exception;

/**
 * Created by jeoy.zhou on 2/23/16.
 */
public class NetTimeoutException extends NetException{

    private static final long serialVersionUID = -2332288514499732812L;

    public NetTimeoutException(){
        super();
    }

    public NetTimeoutException(String message){
        super(message);
    }

    public NetTimeoutException(Throwable cause){
        super(cause);
    }

    public NetTimeoutException(String message, Throwable cause){
        super(message,cause);
    }

}
