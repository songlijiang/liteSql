package com.wolf.store.bplustree;

import com.wolf.store.index.DataHolder;
import java.lang.reflect.Array;
import lombok.Data;

/**
 * Created by slj on 2018-11-27
 */
@Data
public class LeafNode<K extends DataHolder<K>,V extends DataHolder<V>> extends Node<K ,V>{

    private  V values[];

    private int leftId;

    private int rightId;

    protected LeafNode (BPlusTree bPlusTree){
        super(bPlusTree);
        values = (V[])Array.newInstance(bPlusTree.getVType(),bPlusTree.getNODE_DEGREE()*2);
    }


}
