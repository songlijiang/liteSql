package com.wolf.store.bplustree;

import com.wolf.store.index.DataHolder;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;

/**
 * Created by slj on 2018-11-27
 */
@Data
public abstract class Node<K extends DataHolder,V extends DataHolder> {

    private int id;

    private K [] keys;

    private AtomicInteger allocated;


    public boolean isLeafNode(){
        return id<0;
    }

    /**
     *
     * @param key
     * @return
     */
    public int findSlotByKey(K key) {

        int minIndex=0;
        int maxIndex = allocated.get()-1;

        while (maxIndex>minIndex){
            int middleIndex = (maxIndex+minIndex) >>> 1;
            K middleKey  = keys[middleIndex];
            int result =middleKey.compareTo(key);
            if(result>0){
                maxIndex=middleIndex-1;
            }else if(result<0){
                minIndex=middleIndex+1;
            }else {
                return middleIndex;  //key find
            }
        }

        return -minIndex;   // key not find
    }

    protected boolean isFull(){
        return keys.length>=allocated.get();
    }

}
