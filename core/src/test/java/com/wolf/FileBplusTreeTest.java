package com.wolf;

import com.wolf.store.bplustree.FileBPlusTree;
import com.wolf.store.index.IntIndex;
import org.junit.Test;

/**
 * Created by slj on 2019-01-19
 */
public class FileBplusTreeTest {



    @Test
    public void init(){

        FileBPlusTree bPlusTree = new FileBPlusTree(new IntIndex(0),new IntIndex(0),10,"sl");
        bPlusTree.init();
        for (int i = 0; i <100 ; i++) {

            bPlusTree.add(new IntIndex(i),new IntIndex(i*2));
        }
        System.out.println(bPlusTree.findByKey(new IntIndex(90)));
    }
}
