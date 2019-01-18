package com.wolf.store.index;

import lombok.Data;

/**
 * Created by slj on 2018-11-15
 */
@Data
public  abstract class DataHolder<T> implements Comparable<T> ,Serializable<T>{

    abstract DataHolder fromRow(String indexData);

    @Override
    abstract public int compareTo(final T another);

}
