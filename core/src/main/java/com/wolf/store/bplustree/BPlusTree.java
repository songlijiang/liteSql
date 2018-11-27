package com.wolf.store.bplustree;

import com.wolf.exception.DuplicateKeyException;
import com.wolf.exception.IllegalParamException;
import com.wolf.store.index.DataHolder;
import java.util.Arrays;
import lombok.Data;

/**
 * Created by slj on 2018-11-27
 * 1.每个节点最多包含 m 个子节点。
 * 2.除根节点外，每个非叶节点至少包含 m/2 个子节点。
 * 3.如果根节点包含子节点，则至少包含 2 个子节点。
 * 4.拥有 k 个子节点的非叶节点将包含 k - 1 个键值。
 * 5.所有叶节点都在同一层中。
 */
@Data
public abstract class BPlusTree<K extends DataHolder<K> ,V extends DataHolder<V>> {

    protected int rootId;

    /**
     * m/2
     */
    private final int INTERNAL_DEGREE;


    private final int LEAF_DEGREE;


    private int  blockSize =512;


    /**
     *
     *    1.leafNode not full  only insert to leafNode
     *    2.leafNode full  split leafNode by middle  parent add index
     *    3. index node full split  index node   by middle parent add index
     *
     * @param key
     * @param value
     * @return
     */
    private int insert(final  K key,final  V  value){

        if(key==null || value==null){
            throw new IllegalParamException();
        }

        LeafNode<K,V> leafNode = findLeafNode(key);

        if(leafNode==null){
            throw new NullPointerException("find key error");
        }

        int slot = leafNode.findSlotByKey(key);

        if(slot>0){
            throw new DuplicateKeyException();
        }
        int slotIndex =slot>0?slot: -slot;



        if(!leafNode.isFull()){
            if(slotIndex<leafNode.getAllocated().get()){
                //copy key
                System.arraycopy(leafNode.getKeys(),slotIndex+1,leafNode.getKeys(),slotIndex+1,
                    leafNode.getKeys().length-(slotIndex+1));


                //copy value
                System.arraycopy(leafNode.getValues(),slotIndex+1,leafNode.getValues(),slotIndex+1,
                    leafNode.getValues().length-(slotIndex+1));

                //insert key
                leafNode.getKeys()[slotIndex]= key;
                //insert value
                leafNode.getValues()[slotIndex]=value;
            }
        }



        leafNode.getAllocated().incrementAndGet();


    }


    private V findByKey(final K key){

        if(key==null){
            return null;
        }

        LeafNode<K,V> leafNode = findLeafNode(key);

        if(leafNode==null){
            return null;
        }
        int slot = Arrays.binarySearch(leafNode.getValues(),key);
        if(slot==-1){
            return null;
        }
        return leafNode.getValues()[slot];
    }

    private LeafNode<K,V> findLeafNode(K key) {

        Node<K,V> node = getNodeById(rootId);

        while (!node.isLeafNode()){
            InternalNode internalNode = (InternalNode) node;
            int slot =node.findSlotByKey(key);
            int slotId = slot>0 ? slot+1:-(slot+1);
            int childId = internalNode.getChilds()[slotId];
            node = getNodeById(childId);
        }

        return (LeafNode<K, V>) node;
    }

    protected abstract Node<K,V> getNodeById(int rootId);
}
