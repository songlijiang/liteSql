package com.wolf.utils;

import java.util.Date;

/**
 * Created by slj on 2018-11-03
 */
public class Logger {

    private String preFix ="";


    public Logger(String preFix){
        this.preFix=preFix;
    }

    public void log(String message){
        System.out.println(new Date().getTime()+preFix+" "+message);
    }
    public void log(Object o){
        System.out.println(o);
    }
}
