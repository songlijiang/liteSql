package com.wolf.store.bplustree;

import com.wolf.exception.IllegalParamException;
import com.wolf.store.index.DataHolder;
import com.wolf.utils.Logger;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;
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

    public Class<?> kType;

    public Class<?> vType;

    protected int rootId;

    /**
     * m/2
     */
    private final int NODE_DEGREE;


    private int  blockSize =512;

    private Logger logger = new Logger(this.getClass().getName());

    public BPlusTree (Class kType,Class vType,int degree){

        this.kType=kType;
        this.vType=vType;
        this.NODE_DEGREE = degree;

    }

    public void init(){
        LeafNode<K,V> rootNode = new LeafNode<K,V>(this);
        this.rootId = rootNode.getId();
        save(rootNode);
    }


    /**
     * Trace Internal Nodes (put/remove)
     */
    private final Stack<InternalNode<K,V>> stackNodes = new Stack<>();
    /**
     * Trace slots (put/remove)
     */
    private final Stack<Integer> stackSlots = new Stack<>();


    public boolean add(K key,V value){

        if(key==null || value==null){
            return false;
        }
        try {
            Node<K,V> newNode =addIterator(key,value);
            //root  be split
            if(newNode!=null){
                InternalNode<K,V> newRoot = new InternalNode(this);
                K firstKey = newNode.splitShiftKeyLeft();
                newRoot.getKeys()[0]=firstKey;
                newRoot.getChilds()[0]=rootId;
                newRoot.getChilds()[1]=newNode.getId();
                newRoot.getAllocated().set(1);
                this.rootId=newRoot.getId();
                save(newRoot);
            }
        }  finally {
            this.stackNodes.clear();
            this.stackSlots.clear();
        }
        return true;
    }

    protected Node<K,V> addIterator(K key,V value){

        LeafNode<K,V> leafNode = findLeafNode(key,true,false);

        int slot = leafNode.findSlotByKey(key,kType);
        if(slot>0){
            throw new IllegalParamException();
        }
        slot =Math.abs(slot);
        leafNode.add(slot,key,value,kType,vType);

        Node<K,V> newNode = leafNode.isFull()?leafNode.split():null;
        while (!stackSlots.isEmpty()){
            slot = stackSlots.pop();
            InternalNode<K,V> stackInternalNode = stackNodes.pop();
            if(newNode!=null){
                save(newNode);
                K splitShiftKeyLeft = newNode.splitShiftKeyLeft();
                stackInternalNode.add(slot,splitShiftKeyLeft,newNode.getId(),kType);
            }
            newNode = stackInternalNode.isFull()?stackInternalNode.split():null;
        }
        if(newNode!=null){
            save(newNode);
        }

        return newNode;

    }


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
    //private <K extends DataHolder<K> ,V extends DataHolder<V>> int insert(final  K key,final  V  value){
    //
    //    if(key==null || value==null){
    //        throw new IllegalParamException();
    //    }
    //
    //    LeafNode<K,V> leafNode = findLeafNode(key,false);
    //
    //    if(leafNode==null){
    //        throw new NullPointerException("find key error");
    //    }
    //
    //    K oldLeafNodeOnInternalNodeKey = leafNode.getKeys()[0];
    //
    //    int slot = leafNode.findSlotByKey(key);
    //
    //    if(slot>0){
    //        throw new DuplicateKeyException();
    //    }
    //    int slotIndex =Math.abs(slot);
    //
    //
    //
    //    if(!leafNode.isFull()){
    //        //copy key
    //        System.arraycopy(leafNode.getKeys(),slotIndex+1,leafNode.getKeys(),slotIndex+1,
    //            leafNode.getKeys().length-(slotIndex+1));
    //
    //        //copy value
    //        System.arraycopy(leafNode.getValues(),slotIndex+1,leafNode.getValues(),slotIndex+1,
    //            leafNode.getValues().length-(slotIndex+1));
    //
    //        //insert key
    //        leafNode.getKeys()[slotIndex]= key;
    //        //insert value
    //        leafNode.getValues()[slotIndex]=value;
    //        leafNode.getAllocated().incrementAndGet();
    //
    //    }else {
    //
    //        int index =ArrayUtils.findLessIndexByKey(leafNode.getKeys(),leafNode.getAllocated().get(),key);
    //        K[] allKeys = ArrayUtils.insertSorted(leafNode.getKeys(),leafNode.getAllocated().get(),key,index,kType);
    //        V[] allValues = ArrayUtils.insertSorted(leafNode.getValues(),leafNode.getAllocated().get(),value,index,vType);
    //
    //        int splitIndex = allKeys.length/2;
    //        Pair<K[],K[]> oldAndNewKeyPair = ArrayUtils.split(allKeys,splitIndex,kType);
    //        Pair<V[],V[]> oldAndNewValuePair = ArrayUtils.split(allValues,splitIndex,vType);
    //
    //        //set old
    //        leafNode.setKeys(oldAndNewKeyPair.getLeft());
    //        leafNode.setValues(oldAndNewValuePair.getLeft());
    //        leafNode.setAllocated(new AtomicInteger(oldAndNewKeyPair.getLeft().length));
    //
    //        //set new
    //        LeafNode newLeafNode = newLeafNode();
    //        newLeafNode.setKeys(oldAndNewKeyPair.getValue());
    //        newLeafNode.setValues(oldAndNewValuePair.getValue());
    //        newLeafNode.setAllocated(new AtomicInteger(oldAndNewValuePair.getRight().length));
    //
    //        InternalNode internalNode = findParentNode(key);
    //        if(!internalNode.isFull()){
    //            //remove old add new 2 leaf refresh internalNode key and childs
    //            int oldInternalNodeKeyIndex = internalNode.findSlotByKey(oldLeafNodeOnInternalNodeKey);
    //            if(oldInternalNodeKeyIndex<0){
    //                throw new IllegalParamException();
    //            }
    //
    //
    //            internalNode.getKeys()[oldInternalNodeKeyIndex] = leafNode.getKeys()[0];
    //            internalNode.getChilds()[oldInternalNodeKeyIndex+1] = leafNode.getId();
    //
    //
    //            internalNode.setKeys(ArrayUtils.insertSorted(internalNode.getKeys(),internalNode.getAllocated().get(),
    //                newLeafNode.getKeys()[0],oldInternalNodeKeyIndex+1,kType));
    //
    //            internalNode.setChilds(Arrays.asList(ArrayUtils.insertSorted(
    //                IntStream.of( internalNode.getChilds() ).boxed().toArray( Integer[]::new ),
    //                internalNode.getAllocated().get()+1
    //                ,newLeafNode.getId(),oldInternalNodeKeyIndex+2,Integer.class)
    //            ).stream().mapToInt(Integer::intValue).toArray());
    //
    //
    //        }else {
    //
    //            //internalNode is full , iterator split internalNode increase parentNode
    //
    //            InternalNode parent =findParentNode(internalNode.getKeys()[0]);
    //            while (parent.isFull()){
    //                //split
    //
    //
    //                //update parent value
    //                parent =findParentNode(parent.getKeys()[0]);
    //            }
    //
    //
    //
    //
    //        }
    //    }
    //
    //}
    //
    //
    //private  void splitInternalNode(InternalNode internalNode,K newNodeKey,int newNodeId){
    //    if(!internalNode.isFull()){
    //        return;
    //    }
    //    int index =ArrayUtils.findLessIndexByKey(internalNode.getKeys(),internalNode.getAllocated().get(),newNodeKey);
    //    K[] allKeys = ArrayUtils.insertSorted(internalNode.getKeys(),internalNode.getAllocated().get(),key,index,kType);
    //    V[] allValues = ArrayUtils.insertSorted(leafNode.getValues(),leafNode.getAllocated().get(),value,index,vType);
    //
    //    int splitIndex = allKeys.length/2;
    //    Pair<K[],K[]> oldAndNewKeyPair = ArrayUtils.split(allKeys,splitIndex,kType);
    //    Pair<V[],V[]> oldAndNewValuePair = ArrayUtils.split(allValues,splitIndex,vType);
    //
    //    //set old
    //    leafNode.setKeys(oldAndNewKeyPair.getLeft());
    //    leafNode.setValues(oldAndNewValuePair.getLeft());
    //    leafNode.setAllocated(new AtomicInteger(oldAndNewKeyPair.getLeft().length));
    //
    //    //set new
    //    LeafNode newLeafNode = newLeafNode();
    //    newLeafNode.setKeys(oldAndNewKeyPair.getValue());
    //    newLeafNode.setValues(oldAndNewValuePair.getValue());
    //    newLeafNode.setAllocated(new AtomicInteger(oldAndNewValuePair.getRight().length));
    //
    //
    //}

    private LeafNode newLeafNode(){
       return new LeafNode<K,V>(this);
    }


    public V findByKey(final K key){

        if(key==null){
            return null;
        }

        LeafNode<K,V> leafNode = findLeafNode(key,false,true);

        if(leafNode==null){
            return null;
        }

        int slot = Arrays.binarySearch(Arrays.stream(
            leafNode.getKeys()).filter(Objects::nonNull).collect(Collectors.toList()).toArray()
            ,key
        );
        if(slot==-1){
            return null;
        }
        return leafNode.getValues()[slot];
    }

    private  LeafNode<K,V> findLeafNode(K key,boolean trancePath,boolean logTrance) {

        Node<K,V> node = getNodeById(rootId);
        if(logTrance){
            logger.log(node);
        }
        if(trancePath){
            stackSlots.clear();
            stackNodes.clear();
        }

        while (!node.isLeafNode()){
            InternalNode<K,V> internalNode = (InternalNode<K,V>) node;
            int slot =node.findSlotByKey(key,kType);
            int slotId = slot>0 ? slot+1:-slot;
            if(trancePath){
                stackSlots.push(slotId);
                stackNodes.push(internalNode);
            }
            int childId = internalNode.getChilds()[slotId];
            node = getNodeById(childId);
            if(logTrance){
                logger.log(node);
            }
        }

        return (LeafNode<K, V>) node;
    }

    private  InternalNode<K,V> findParentNode(K key){
        Node<K,V> node = getNodeById(rootId);
        Node<K,V> parent = node;
        boolean find =false;
        while (!node.isLeafNode() && !find){
            InternalNode internalNode = (InternalNode) node;
            int slot =node.findSlotByKey(key,kType);
            find = slot>0;
            int slotId = slot>0 ? slot+1:-slot;
            int childId = internalNode.getChilds()[slotId];
            parent =node;
            node = getNodeById(childId);
        }
        return find?(InternalNode<K, V>) parent:null;
    }



    public abstract void save(Node<K,V> node);

    public  abstract  Node<K,V> getNodeById(int rootId);

    public abstract int allocateId();

}
