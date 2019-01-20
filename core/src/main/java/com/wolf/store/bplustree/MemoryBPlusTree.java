package com.wolf.store.bplustree;

import com.google.common.collect.Maps;
import com.wolf.exception.IllegalParamException;
import com.wolf.store.index.DataHolder;
import com.wolf.utils.Logger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;

/**
 * Created by slj on 2018/12/11
 */
@Data
public class MemoryBPlusTree<K extends DataHolder<K>,V extends DataHolder<V>> extends BPlusTree<K,V>{

    Map<Integer,Node<K,V>> store = Maps.newConcurrentMap();

    private  AtomicInteger allocated;


    public MemoryBPlusTree(DataHolder kType, DataHolder vType, int degree) {
        super(kType, vType, degree);
        allocated=new AtomicInteger(0);
    }

    @Override public Node<K, V> getNodeById(int rootId) {
        Node<K,V> node = store.get(rootId);
        return node;
    }

    @Override public int allocateId() {
        return allocated.incrementAndGet();
    }

    @Override public void save(Node<K, V> node) {
        //if(store.containsKey(node.getId())){
        //    throw  new IllegalParamException();
        //}
        store.put(node.getId(),node);
    }
}
