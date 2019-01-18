package com.wolf.store.bplustree;

import com.wolf.store.index.DataHolder;

/**
 * Created by slj on 2018-12-23
 */
public class FileBPlusTree<K extends DataHolder<K>,V extends DataHolder<V>> extends BPlusTree<K,V> {

    private int nodeSize;

    private String fileName;


    public FileBPlusTree(Class kType, Class vType, int degree,String indexName) {
        super(kType, vType, degree);
        fileName=indexName+".data";

    }

    @Override public void save(Node<K, V> node) {
    }

    @Override public Node<K, V> getNodeById(int rootId) {
        return null;
    }

    @Override public int allocateId() {
        return 0;
    }


}
