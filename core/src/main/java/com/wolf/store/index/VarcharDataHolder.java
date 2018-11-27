package com.wolf.store.index;

/**
 * Created by slj on 2018-11-15
 */
public class VarcharDataHolder extends DataHolder<String> {

    private String data;


    @Override DataHolder fromRow(String indexData) {
        VarcharDataHolder index = new VarcharDataHolder();
        index.setKey(indexData);
        return index;
    }

    @Override public int compareTo(String another) {
        return data.compareTo(another);
    }
}
