package com.wolf.store.bplustree;

import com.wolf.store.index.DataHolder;
import lombok.Data;

/**
 * Created by slj on 2018-11-27
 */
@Data
public class LeafNode<K extends DataHolder<K>,V extends DataHolder<V>> extends Node<K ,V>{

    private final V values[];

    private int leftId;

    private int rightId;

}
