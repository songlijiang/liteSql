package com.wolf.store.bplustree;

import com.wolf.store.index.DataHolder;
import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;

import static com.wolf.utils.ArrayUtils.findLessIndexByKey;

/**
 * Created by slj on 2018-11-27
 */
@Data
public abstract class Node<K extends DataHolder> {

    private int id;

    private K [] keys;

    private AtomicInteger allocated;


    protected   Node(BPlusTree bPlusTree){
        this.keys = (K[]) Array.newInstance(bPlusTree.kType,bPlusTree.getNODE_DEGREE()*2);
        this.id = bPlusTree.allcateId();
        allocated=new AtomicInteger(0);
    }


    public boolean isLeafNode(){
        return id<0;
    }

    /**
     *
     * @param key
     * @return
     */
    public int findSlotByKey(K key) {
        return findLessIndexByKey(keys,allocated.get(),key);
    }



    protected boolean isFull(){
        return keys.length>=allocated.get();
    }

}
