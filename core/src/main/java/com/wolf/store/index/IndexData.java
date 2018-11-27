package com.wolf.store.index;

import lombok.Data;

/**
 * Created by slj on 2018-11-15
 */
@Data
public class IndexData<T extends DataHolder> {

    private long dataPosition;

    private T key;

}
