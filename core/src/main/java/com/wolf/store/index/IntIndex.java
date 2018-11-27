package com.wolf.store.index;

import lombok.Data;

/**
 * Created by slj on 2018-11-15
 */
@Data
class IntIndex extends DataHolder<Integer> {

    private Integer data;

    @Override DataHolder fromRow(String indexData) {
        IntIndex index = new IntIndex();
        index.setKey(Integer.parseInt(indexData));
        return index;
    }

    @Override public int compareTo(Integer another) {
        return data.compareTo(another);
    }
}
