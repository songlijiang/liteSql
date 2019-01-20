package com.wolf.store.bplustree;

import com.wolf.exception.IndexException;
import com.wolf.store.index.DataHolder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by slj on 2018-12-23
 */
public class FileBPlusTree<K extends DataHolder<K>,V extends DataHolder<V>> extends BPlusTree<K,V> {

    private int nodeSize;

    private String fileName;

    RandomAccessFile raf;

    FileChannel channel ;

    private AtomicInteger allocated;

    private File mdFile;

    public FileBPlusTree(DataHolder kType, DataHolder vType, int degree,String indexName) {
        super(kType, vType, degree);
        fileName=indexName+".data";
        mdFile = new File(indexName+".md");
        try {
            raf = new RandomAccessFile (new File(fileName), "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        channel = raf.getChannel();
        nodeSize = NodeSerialize.nodeSize(2*NODE_DEGREE,getKeyType(),getValueType());
        allocated =new AtomicInteger(0);
    }

    private void refreshRootId(){

        try {
            RandomAccessFile rm = new RandomAccessFile(mdFile,"rw");
            FileChannel rmChannel =rm.getChannel();
            try {
                MappedByteBuffer buff = rmChannel.map(FileChannel.MapMode.READ_WRITE,0,4);
                buff.putInt(rootId);
                buff.force();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override public void save(Node<K, V> node)  {

        int offset =(node.getId())*nodeSize;
        try {
            MappedByteBuffer buff = channel.map(FileChannel.MapMode.READ_WRITE,offset,nodeSize);
            NodeSerialize.saveNode(buff,node,getKeyType(),getValueType());
            buff.force();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public Node<K, V> getNodeById(int nodeId) {
        int startAddr = (nodeId)*nodeSize;
        try {
            MappedByteBuffer buff = channel.map(FileChannel.MapMode.READ_ONLY,startAddr,nodeSize);
            return NodeSerialize.getNode(buff,getKeyType(),getValueType(),this);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IndexException();
        }
    }

    @Override public int allocateId() {
        return allocated.incrementAndGet();
    }

    @Override
    protected void setRootId(int rootId){
        super.setRootId(rootId);
        refreshRootId();
    }



}
