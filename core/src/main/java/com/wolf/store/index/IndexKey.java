package com.wolf.store.index;

import lombok.Data;

/**
 * Created by slj on 2018-11-15
 */
@Data
public  abstract class IndexKey<T> {

    T key;

    abstract IndexKey fromRow(String indexData);


    abstract int compareKey(T o1, T o2);
}
