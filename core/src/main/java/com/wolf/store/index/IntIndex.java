package com.wolf.store.index;

import lombok.Data;

/**
 * Created by slj on 2018-11-15
 */
@Data
public class IntIndex extends DataHolder<IntIndex> {

    private Integer key;

    public IntIndex(int key){
        this.key = key;
    }

    @Override DataHolder fromRow(String indexData) {
        IntIndex index = new IntIndex(Integer.parseInt(indexData));
        return index;
    }

    @Override public int compareTo(IntIndex another) {
        return key.compareTo(another.key);
    }
}
