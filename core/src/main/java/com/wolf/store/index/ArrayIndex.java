package com.wolf.store.index;

import com.wolf.exception.IndexException;
import com.wolf.parser.Row;
import com.wolf.parser.RowColumn;
import com.wolf.store.StoreEngine;
import com.wolf.utils.Pair;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * Created by slj on 2018-11-15
 */
@Data
public class ArrayIndex<T extends DataHolder<T>> {


    private IndexData<T>[] indexData;

    private StoreEngine storeEngine;

    private String tableName;




    public void init(DataHolder keyType, int columnId){
        if(storeEngine==null || tableName==null){
            throw new IndexException("storeEngine is null");
        }
        List<Pair<Long, Row>> datas;
        try {
            datas =storeEngine.queryAllWithPosition(tableName);
        }catch (IOException e){
            throw new IndexException(e);
        }

        List<IndexData> indexDataLists = datas.stream().map(e->
            transform(e,keyType,columnId)
        ).collect(Collectors.toList());

        indexData =indexDataLists.toArray(new IndexData[indexDataLists.size()]);

    }

    private IndexData transform(Pair<Long,Row> pair, DataHolder keyType,int columnId){

        IndexData data = new IndexData();
        data.setDataPosition(pair.getKey());
        data.setKey(keyType.fromRow(
            pair.getValue().getData().stream().filter(e->e.getColumnId()==columnId)
                .findFirst().map(RowColumn::getData).orElse("")
        ));
        return data;
    }

    public Row queryByKey(DataHolder key)throws IOException{
        int index = binarySearchLeft(indexData,key);
        if(index==-1){
            return null;
        }
        long position =  indexData[index].getDataPosition();
        return storeEngine.getByPosition(tableName,position);
    }

    public static int binarySearchLeft(IndexData [] arr, DataHolder target){
        if(arr==null||arr.length==0){
            return -1;
        }
        int left = 0;
        int right = arr.length-1;
        while(left<right){
            int mid = (left+right)/2;
            if(target.compareTo(arr[mid].getKey())<0){
                left = mid+1;
            }else {
                right = mid;
            }
        }
        if(target.compareTo(arr[right].getKey())==0){
            return right;
        }
        return -1;
    }



}
