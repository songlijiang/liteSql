package com.wolf.store.bplustree;

import com.google.common.collect.Maps;
import com.wolf.exception.IllegalParamException;
import com.wolf.store.index.DataHolder;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by slj on 2018/12/11
 */
public class MemoryBPlusTree<K extends DataHolder<K>,V extends DataHolder<V>> extends BPlusTree<K,V>{

    Map<Integer,Node<K,V>> store = Maps.newConcurrentMap();

    private  AtomicInteger allocated;


    public MemoryBPlusTree(Class kType, Class vType, int degree) {
        super(kType, vType, degree);
        allocated=new AtomicInteger(0);
    }

    @Override public Node<K, V> getNodeById(int rootId) {
        return store.get(rootId);
    }

    @Override public int allocateId() {
        return allocated.incrementAndGet();
    }

    @Override public void save(Node<K, V> node) {
        if(store.containsKey(node.getId())){
            throw  new IllegalParamException();
        }
        store.put(node.getId(),node);
    }
}
