package com.wolf.exception;

/**
 * Created by slj on 2018-11-03
 */
public class IndexException extends RuntimeException{

    public IndexException(){
        super();
    }

    public IndexException(String message){
        super(message);
    }

    public IndexException(Exception e){
        super(e);
    }
}
