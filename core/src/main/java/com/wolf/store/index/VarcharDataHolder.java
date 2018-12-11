package com.wolf.store.index;

import lombok.Data;

/**
 * Created by slj on 2018-11-15
 */
@Data
public class VarcharDataHolder extends DataHolder<VarcharDataHolder> {

    private String key;

    public VarcharDataHolder(String data){
        this.key =data;
    }


    @Override DataHolder fromRow(String indexData) {
        return new VarcharDataHolder(indexData);
    }

    @Override public int compareTo(VarcharDataHolder another) {
        return this.key.compareTo(another.getKey());
    }
}
