package com.wolf.store.bplustree;

import com.wolf.store.index.DataHolder;
import lombok.Data;

/**
 * Created by slj on 2018-11-27
 */
@Data
public final class InternalNode<K extends DataHolder,V extends DataHolder> extends Node<K,V> {


    private int childs[];



}
