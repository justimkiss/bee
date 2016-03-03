package com.bee.remote.exception;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public class BeeTimeoutException extends Exception {

    private static final long serialVersionUID = -3166328907439161734L;

    public BeeTimeoutException(String msg){
        super(msg);
    }

}
