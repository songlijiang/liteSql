package com.wolf.exception;

/**
 * Created by slj on 2018-11-03
 */
public class StoreException extends RuntimeException{


    public StoreException (String message){
        super(message);
    }

    public StoreException (Exception e){
        super(e);
    }
}
