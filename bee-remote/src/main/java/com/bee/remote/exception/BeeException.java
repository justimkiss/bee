package com.bee.remote.exception;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public class BeeException extends RuntimeException{

    private static final long serialVersionUID = -885679544994513104L;

    private String serviceName;
    private String address;

    public BeeException(){
        super();
    }

    public BeeException(String serviceName, String address, String msg, Throwable e) {
        super(msg, e);
        this.serviceName = serviceName;
        this.address = address;
    }

    public BeeException(String message){
        super(message);
    }

    public BeeException(Throwable cause){
        super(cause);
    }

    public BeeException(String message, Throwable cause){
        super(message,cause);
    }
}
