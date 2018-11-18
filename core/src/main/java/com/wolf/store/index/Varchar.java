package com.wolf.store.index;

import lombok.Data;

/**
 * Created by slj on 2018-11-18
 */
@Data
public class Varchar {

    public Varchar(int size){
        this.size = size;
    }

    private char[] data;

    private int size;

}
