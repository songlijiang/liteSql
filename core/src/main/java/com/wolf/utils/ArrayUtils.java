package com.wolf.utils;

import com.wolf.exception.IllegalParamException;
import java.lang.reflect.Array;

/**
 * Created by slj on 2018-11-29
 */
public class ArrayUtils {


    public  static <K extends Comparable> int findLessIndexByKey(K[] keys,int length,K key){
        int minIndex=0;
        int maxIndex =length-1;

        while (maxIndex>minIndex){
            int middleIndex = (maxIndex+minIndex) >>> 1;
            K middleKey  = keys[middleIndex];
            int result =middleKey.compareTo(key);
            if(result>0){
                maxIndex=middleIndex+1;
            }else if(result<0){
                minIndex=middleIndex-1;
            }else {
                return middleIndex;  //key find
            }
        }

        return -minIndex;   // key not find

    }


    public static <K extends Comparable>  K[] insertSorted(K[] keys,int length,K key,int index,Class keyType){

        if(index>0){
            throw new IllegalParamException();
        }

        K[] leafAllKeys = (K[]) Array.newInstance(keyType,length+1);

        System.arraycopy(keys,0,leafAllKeys,0,(-index)+1);
        leafAllKeys[(-index)+1]=key;
        System.arraycopy(keys,(-index)+1,leafAllKeys,(-index)+2
            ,length-((-index)+1));
        return leafAllKeys;
    }

    public static <K extends  Comparable> Pair<K[],K[]> split(K[] keys,int split,Class type){

        int leftSize = split+1;
        int rightSize = keys.length-(split+1);
        K[] leftResult = (K[])Array.newInstance(type,leftSize);
        K[] rightResult = (K[])Array.newInstance(type,rightSize);
        System.arraycopy(keys,0,leftResult,0,leftSize);
        System.arraycopy(keys,leftSize,rightResult,0,rightSize);
        return new Pair<>(leftResult,rightResult);
    }

}
