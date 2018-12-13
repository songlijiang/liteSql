package com.wolf;

import com.wolf.store.bplustree.BPlusTree;
import com.wolf.store.bplustree.MemoryBPlusTree;
import com.wolf.store.index.IntIndex;
import org.junit.Test;

/**
 * Created by slj on 2018/12/11
 */
public class MemoryBPlusTreeTest {



    @Test
    public void test_init(){
        MemoryBPlusTree bPlusTree = new MemoryBPlusTree(IntIndex.class,IntIndex.class,10);
        bPlusTree.init();
        for (int i = 0; i <300000 ; i++) {
            if(i==210){
                i=210;
            }
            bPlusTree.add(new IntIndex(i),new IntIndex(i*2));
        }
        System.out.println(bPlusTree.findByKey(new IntIndex(1212)));
       // System.out.println(bPlusTree.getStore().size());
    }


}
