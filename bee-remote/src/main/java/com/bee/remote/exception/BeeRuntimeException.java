package com.bee.remote.exception;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public class BeeRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 3658093814167110930L;

    public BeeRuntimeException(){
        super();
    }

    public BeeRuntimeException(String message){
        super(message);
    }

    public BeeRuntimeException(Throwable cause){
        super(cause);
    }

    public BeeRuntimeException(String message, Throwable cause){
        super(message,cause);
    }
}
