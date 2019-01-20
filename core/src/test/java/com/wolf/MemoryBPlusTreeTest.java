package com.wolf;

import com.wolf.store.bplustree.MemoryBPlusTree;
import com.wolf.store.index.IntIndex;
import java.util.Arrays;
import org.junit.Test;

/**
 * Created by slj on 2018/12/11
 */
public class MemoryBPlusTreeTest {



    @Test
    public void test_init(){
        MemoryBPlusTree bPlusTree = new MemoryBPlusTree(new IntIndex(0),new IntIndex(0),10);
        bPlusTree.init();
        for (int i = 0; i <100000 ; i++) {

            bPlusTree.add(new IntIndex(i),new IntIndex(i*2));
        }
        System.out.println(bPlusTree.findByKey(new IntIndex(5999)));
    }
    @Test
    public void test_binary(){
        int[] datas = new int[3000000];
        for (int i = 0; i <3000000 ; i++) {
            datas[i]=i*2;
        }
        System.out.println( datas[Arrays.binarySearch(datas,1024)]);

    }


}
