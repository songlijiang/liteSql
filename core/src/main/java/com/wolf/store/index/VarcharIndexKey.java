package com.wolf.store.index;

/**
 * Created by slj on 2018-11-15
 */
public class VarcharIndexKey extends IndexKey<String> {

    @Override public int compareKey(String o1, String o2) {
        return  o1.compareTo(o2);
    }

    @Override IndexKey fromRow(String indexData) {
        VarcharIndexKey index = new VarcharIndexKey();
        index.setKey(indexData);
        return index;
    }
}
