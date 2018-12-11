package com.wolf;

import com.wolf.store.bplustree.BPlusTree;
import com.wolf.store.bplustree.MemoryBPlusTree;
import com.wolf.store.index.IntIndex;
import com.wolf.store.index.VarcharDataHolder;
import org.junit.Test;

/**
 * Created by slj on 2018/12/11
 */
public class MemoryBPlusTreeTest {



    @Test
    public void test_init(){
        BPlusTree bPlusTree = new MemoryBPlusTree(IntIndex.class,IntIndex.class,10);
        bPlusTree.init();
        for (int i = 0; i <30 ; i++) {
            bPlusTree.add(new IntIndex(i),new IntIndex(i*2));
        }
        System.out.println(bPlusTree.findByKey(new IntIndex(4)));
    }


}
