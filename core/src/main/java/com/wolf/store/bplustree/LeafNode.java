package com.wolf.store.bplustree;

import com.wolf.store.index.DataHolder;
import com.wolf.utils.ArrayUtils;
import java.lang.reflect.Array;
import lombok.Data;
import lombok.ToString;

/**
 * Created by slj on 2018-11-27
 */
@Data
@ToString(callSuper=true,exclude={"bPlusTree"})
public class LeafNode<K extends DataHolder<K>,V extends DataHolder<V>> extends Node<K ,V>{

    private  V values[];

    private int leftId;

    private int rightId;

    protected LeafNode (BPlusTree bPlusTree){
        super(bPlusTree);
        values = (V[])Array.newInstance(bPlusTree.getVType(),bPlusTree.getNODE_DEGREE()*2);
    }

    private LeafNode<K,V> newLeafNode(){
        return new LeafNode(this.getBPlusTree());
    }

    public void add(int slot, K key, V value,Class keyType,Class valueType){

        //move old to next
        setKeys(ArrayUtils.insertSorted(getKeys(),getAllocated().get(),key,slot,keyType));
        setValues(ArrayUtils.insertSorted(getValues(),getAllocated().get(),value,slot,valueType));

        getKeys()[slot]=key;
        getValues()[slot]=value;
        getAllocated().incrementAndGet();


    }

    public  LeafNode<K,V> split(){
        LeafNode<K,V> newNode = newLeafNode();
        int size  = this.getAllocated().get()>>>1;
        int newSize = this.getAllocated().get()-size;
        System.arraycopy(getKeys(),size,newNode.getKeys(),0,newSize);
        System.arraycopy(getValues(),size,newNode.getValues(),0,newSize);
        this.getAllocated().set(size);
        newNode.getAllocated().set(newSize);
        for (int i = 0; i < newSize; i++) {
            this.getKeys()[i+size]=null;
            this.getValues()[i+size]=null;
        }
        newNode.leftId=getId();
        newNode.rightId=rightId;

        rightId=newNode.getId();
        return newNode;

    }

    public   K splitShiftKeyLeft(){
        return getKeys()[0];
    }

    @Override public boolean isLeafNode() {
        return true;
    }
}
