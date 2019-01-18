package com.wolf.store.index;

import java.nio.ByteBuffer;

/**
 * Created by slj on 2018-12-23
 */
public interface Serializable<T> {

    int length ();

    void serialize(ByteBuffer byteBuffer);

    T deSerialize(ByteBuffer byteBuffer);

}
