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
public abstract class Node<K extends DataHolder<K>,V extends DataHolder<V>> {

    public static final int NULL_ID = -1;

    private int id;

    private K [] keys;

    private AtomicInteger allocated;

    private BPlusTree bPlusTree;


    protected   Node(BPlusTree bPlusTree){
        this.bPlusTree = bPlusTree;
        this.keys = (K[]) Array.newInstance(bPlusTree.kType,bPlusTree.getNODE_DEGREE()*2);
        this.id = bPlusTree.allocateId();
        allocated=new AtomicInteger(0);
    }


    public abstract boolean isLeafNode();

    /**
     *
     * @param key
     * @return
     */
    public int findSlotByKey(K key) {
        return findLessIndexByKey(keys,allocated.get(),key);
    }

    protected boolean isFull(){
        return allocated.get() >=keys.length;
    }

    public abstract  K splitShiftKeyLeft();

    public abstract Node<K, V> split();
}
