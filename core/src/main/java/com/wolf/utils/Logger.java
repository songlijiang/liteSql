package com.wolf.utils;

/**
 * Created by slj on 2018-11-03
 */
public class Logger {

    private String preFix ="";


    public Logger(String preFix){
        this.preFix=preFix;
    }

    public void log(String message){
        System.out.println(preFix+" "+message);
    }
    public void log(Object o){
        System.out.println(preFix+" "+o==null?"null":o);
    }
}
