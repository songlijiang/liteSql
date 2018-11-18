package com.wolf.store.index;

import lombok.Data;

/**
 * Created by slj on 2018-11-15
 */
@Data
class IntIndex extends IndexKey<Integer>{

    @Override public int compareKey(Integer i1, Integer i2) {
        return i1.compareTo(i2);
    }

    @Override IndexKey fromRow(String indexData) {
        IntIndex index = new IntIndex();
        index.setKey(Integer.parseInt(indexData));
        return index;
    }
}
