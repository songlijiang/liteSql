package com.wolf.store.bplustree;

import com.wolf.store.index.DataHolder;
import com.wolf.utils.ArrayUtils;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Data;
import lombok.ToString;

/**
 * Created by slj on 2018-11-27
 */
@Data
@ToString(callSuper=true,exclude={"bPlusTree"})
public final class InternalNode<K extends DataHolder<K>,V extends DataHolder<V>> extends Node<K,V> {


    private int childs[];

    protected InternalNode (BPlusTree bPlusTree){
        super(bPlusTree);
        childs = new int[bPlusTree.getNODE_DEGREE()*2+1];
    }

    private InternalNode<K,V> newInternalNode(){
        return new InternalNode(this.getBPlusTree());
    }


    //todo
    @Override public  K splitShiftKeyLeft() {
        K removed = getKeys()[0];
        System.arraycopy(getKeys(),1,getKeys(),0,getAllocated().get()-1);
        System.arraycopy(getChilds(),1,getChilds(),0,getAllocated().get());
        getKeys()[getAllocated().get()-1]=null;
        getChilds()[getAllocated().get()]=NULL_ID;
        getAllocated().decrementAndGet();
        return removed;
    }

    public void add(int slot, K splitShiftKeyLeft, int id,Class keyType) {
        setKeys(ArrayUtils.insertSorted(getKeys(),getAllocated().get(),splitShiftKeyLeft,slot,keyType));
        List<Integer> childsList =   Arrays.asList(ArrayUtils.insertSorted(
            IntStream.of( getChilds() ).boxed().toArray( Integer[]::new ),
            getAllocated().get()+1,id,slot+1,Integer.class));
        if(childsList.get(20)==30599){
            childsList.size();
        }
        setChilds(childsList.stream().mapToInt(Integer::intValue).toArray());
        getAllocated().incrementAndGet();
    }

    @Override public Node<K, V> split() {
        InternalNode<K,V> newNode = newInternalNode();
        int size  = this.getAllocated().get()>>>1;
        int newSize = this.getAllocated().get()-size;
        System.arraycopy(getKeys(),size,newNode.getKeys(),0,newSize);
        System.arraycopy(getChilds(),size,newNode.getChilds(),0,newSize+1);
        this.getAllocated().set(size);
        newNode.getAllocated().set(newSize);
        for (int i = 0; i < newSize; i++) {
            this.getKeys()[i+size]=null;
            if(i!=0){
                this.getChilds()[i+size]=NULL_ID;
            }
        }
        this.getChilds()[size+newSize]=NULL_ID;
        return newNode;
    }

    @Override public boolean isLeafNode() {
        return false;
    }

    @Override public void serialize(ByteBuffer byteBuffer) {

    }
}
