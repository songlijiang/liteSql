package com.wolf.store.bplustree;

import com.wolf.exception.IllegalParamException;
import com.wolf.store.index.DataHolder;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by slj on 2019-01-19
 */
public class NodeSerialize {

        private static char startChar ='l';

        private static char endChar ='s';


        public static  <K extends DataHolder<K>,V extends DataHolder<V>> Node<K,V> getNode(ByteBuffer byteBuffer,DataHolder<K> keyType,DataHolder<V> valueType,BPlusTree bPlusTree ){

            checkStart(byteBuffer);
            int id = byteBuffer.getInt();

            int keyLength = byteBuffer.getInt();

            int allocated = byteBuffer.getInt();

            if(keyLength<=0){
                throw new IllegalParamException();
            }
            byte isLeaf = byteBuffer.get();

            int keyDataSize = byteBuffer.getInt();
            int valueDataSize = byteBuffer.getInt();

            K[] keys =(K[]) Array.newInstance(keyType.getClass(),keyLength);

            for (int i = 0; i <keyLength ; i++) {
                keys[i] = keyType.deSerialize(byteBuffer);
            }

            //internalNode
            if(isLeaf==0){
                int[] children = new int[keyLength+1];
                InternalNode<K,V> internalNode = new InternalNode<>(bPlusTree);
                internalNode.setId(id);
                for (int i = 0; i <keyLength+1 ; i++) {
                    if(i<=allocated){
                        children[i] =byteBuffer.getInt();
                    }else {
                        byteBuffer.position(byteBuffer.position()+keyType.length());
                    }
                }
                byteBuffer.position(byteBuffer.position()+keyLength*valueDataSize);

                internalNode.setChildren(children);
                internalNode.setKeys(keys);
                internalNode.setAllocated(new AtomicInteger(allocated));
                byteBuffer.position(byteBuffer.position()+8);
                checkEnd(byteBuffer);
                return internalNode;
            }else {
                byteBuffer.position(byteBuffer.position()+(keyLength+1)*4);
                LeafNode<K,V> leafNode =new LeafNode<>(bPlusTree);
                leafNode.setId(id);

                V[] values =(V[]) Array.newInstance(valueType.getClass(),keyLength);
                for (int i = 0; i <keyLength ; i++) {
                    if(i<allocated){
                        values[i] = valueType.deSerialize(byteBuffer);
                    }else {
                        byteBuffer.position(byteBuffer.position()+valueType.length());
                    }
                }

                leafNode.setKeys(keys);
                leafNode.setValues(values);
                leafNode.setAllocated(new AtomicInteger(allocated));
                leafNode.setLeftId(byteBuffer.getInt());
                leafNode.setRightId(byteBuffer.getInt());
                checkEnd(byteBuffer);
                return leafNode;
            }

        }


        private static void checkStart(ByteBuffer byteBuffer){
            if(startChar!=byteBuffer.getChar()){
                throw new IllegalParamException();
            }
        }
        private static void checkEnd(ByteBuffer byteBuffer){
            if(endChar!= byteBuffer.getChar()){
                throw new IllegalParamException();
            }
        }


        public  static  void saveNode(ByteBuffer byteBuffer,Node node,DataHolder keyType,DataHolder valueType){
            int length = node.getKeys().length;


            //start
            byteBuffer.putChar(startChar);

            byteBuffer.putInt(node.getId());
            byteBuffer.putInt(length);
            byteBuffer.putInt(node.getAllocated().intValue());

            byteBuffer.put(node.isLeafNode()?(byte) 1:(byte) 0);
            byteBuffer.putInt(keyType.length());
            byteBuffer.putInt(valueType.length());

            //keys
            for (int i = 0; i < length; i++) {
                DataHolder keyTemp =   node.getKeys()[i];
                if(keyTemp==null){
                    byteBuffer.position(byteBuffer.position()+keyType.length());
                }else {
                    keyTemp.serialize(byteBuffer);
                }
            }

            //children
            if(!node.isLeafNode()){
                InternalNode internalNode = (InternalNode)node;
                for (int i = 0; i <internalNode.getChildren().length; i++) {
                    byteBuffer.putInt(internalNode.getChildren()[i]);
                }
            }else {
                byteBuffer.position(byteBuffer.position()+(length+1)*4);
            }

            //values
            if(node.isLeafNode()){
                LeafNode leafNode =(LeafNode)node;
                for (int i = 0; i < length; i++) {
                    DataHolder valueTemp =  leafNode.getValues()[i];
                    if(valueTemp==null){
                        byteBuffer.position(byteBuffer.position()+valueType.length());
                    }else {
                        valueTemp.serialize(byteBuffer);
                    }
                }
                byteBuffer.putInt(leafNode.getLeftId());
                byteBuffer.putInt(leafNode.getRightId());
            }else {
                byteBuffer.position(byteBuffer.position()+valueType.length()*length+8);
            }
            //end
            byteBuffer.putChar(endChar);

        }


        public static int nodeSize(int keyLength,DataHolder keyType,DataHolder valueType){

            int keyDataSize =keyType.length();
            int valueDataSize =valueType.length();
            int total = 2
                +4*5
                +1
                +keyLength*keyDataSize
                +(keyLength+1)*4
                +valueDataSize*keyLength
                +8
                +2;
            return  total;
        }


}
