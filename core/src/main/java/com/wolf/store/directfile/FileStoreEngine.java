package com.wolf.store.directfile;

import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import com.wolf.exception.StoreException;
import com.wolf.parser.Row;
import com.wolf.parser.RowColumn;
import com.wolf.parser.Schema;
import com.wolf.store.StoreEngine;
import com.wolf.utils.Logger;
import com.wolf.utils.Pair;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

/**
 * Created by slj on 2018-11-03
 */
public class FileStoreEngine implements StoreEngine {


    private static String dataDir ="/Users/lijiang.song/data/";
    private static String tableSubffix =".data";

    private Logger logger = new Logger(this.getClass().getName());

    @Override public int insert(List<Row> datas,String tableName,List<Schema> schemas) throws IOException{

        File file = new File(dataDir+tableName+tableSubffix);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.log(e.getMessage());
                throw new StoreException(e);
            }
        }
        OutputStream outputStream;
        try {
            outputStream=new FileOutputStream(file,true);
        } catch (IOException e) {
            logger.log(e.getMessage());
            throw new StoreException(e);
        }

        check(datas,schemas);
        datas.forEach(data->{
            List<String> rowDatas = data.getData().stream().sorted(Comparator.comparingInt(RowColumn::getSort))
                .map(RowColumn::getData).collect(Collectors.toList());
            try {
                outputStream.write(new Data(rowDatas).getBytes());
            } catch (IOException e) {
                logger.log(e.getMessage());
                throw new StoreException(e);
            }finally {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    logger.log(e.getMessage());
                    throw new StoreException(e);
                }
            }
        });

        return datas.size();
    }

    @Override public List<Row> queryAll(String tableName) throws IOException {
       return queryAllWithPosition(tableName).stream().map(Pair::getValue).collect(Collectors.toList());
    }

    public List<Pair<Long,Row>> queryAllWithPosition(String tableName) throws IOException {
        RandomAccessFile file = new RandomAccessFile(dataDir+tableName+tableSubffix,"rw");
        InputStream inputStream= Channels.newInputStream(file.getChannel());
        List<Pair<Long,Row>> rows = Lists.newArrayList();
        long point =file.getFilePointer();
        int dataLength = inputStream.read();
        while (dataLength!=-1){
            byte[] splits =  new byte[dataLength];
            inputStream.read(splits);

            List<List<Byte>> tempDatas =Bytes.asList(splits).stream()
                .map(e-> {
                    byte[] temp =new byte[e];
                    try {
                        inputStream.read(temp);
                    } catch (IOException e1) {
                        throw new StoreException(e1);
                    }
                    return Bytes.asList(temp);
                }).collect(Collectors.toList());
            rows.add(new Pair<>(point,transform(tempDatas)));
            point =file.getFilePointer();
            dataLength = inputStream.read();
        }
        return rows;
    }

    public Row getByPosition(String tableName , long position) throws IOException {

        RandomAccessFile file = new RandomAccessFile(dataDir+tableName+tableSubffix,"rw");
        file.seek(position);
        InputStream inputStream= Channels.newInputStream(file.getChannel());
        int dataLength = inputStream.read();
        Row result =null;
        if(dataLength!=-1){
            byte[] splits =  new byte[dataLength];
            inputStream.read(splits);

            List<List<Byte>> tempDatas =Bytes.asList(splits).stream()
                .map(e-> {
                    byte[] temp =new byte[e];
                    try {
                        inputStream.read(temp);
                    } catch (IOException e1) {
                        throw new StoreException(e1);
                    }
                    return Bytes.asList(temp);
                }).collect(Collectors.toList());
            result = transform(tempDatas);
        }
       return result;
    }


    private Row transform(List<List<Byte>> rowData){
        List<RowColumn> rowColumns = Lists.newArrayList();
        for (int i = 0; i < rowData.size(); i++) {
            List<Byte> valueBytes = rowData.get(i);
           rowColumns.add(new RowColumn(i,i,new String(Bytes.toArray(valueBytes))));
        }
        return new Row(rowColumns);
    }

    @Override public List<Row> queryByConditions(List<Predicate> predicates) {
        return null;
    }




    @lombok.Data
    public static class Data{

        private Header header;

        private Body body;

        public Data(List<String> contents){
            List<List<Byte>> contentBytes = contents.stream().map(e-> Bytes.asList(e.getBytes())).collect(Collectors.toList());
            this.body= new FileStoreEngine.Body(contentBytes);
            this .header = new FileStoreEngine.Header(body);
        }

        public byte[]  getBytes(){
            return Bytes.concat(header.getSaveContent(),body.getSaveContent());
        }


    }

    @lombok.Data
    @AllArgsConstructor
    public static class Header{

        private List<Integer> splitLength;

        public Header (Body body){
            this.splitLength = body.getDatas().stream().map(List::size).collect(Collectors.toList());
        }

        public byte[] getSaveContent(){
            byte lengthByte = Byte.parseByte(String.valueOf(splitLength.size()));
            List<Byte> results =  Lists.newArrayList(lengthByte);
            results.addAll(splitLength.stream().map(length-> Byte.parseByte(String.valueOf(length)))
                .collect(Collectors.toList()));
            return Bytes.toArray(results);
        }
    }

    @lombok.Data
    @AllArgsConstructor
    public static class Body{

        private List<List<Byte>> datas;

        public byte[] getSaveContent(){
            return Bytes.toArray(datas.stream().flatMap(Collection::stream).collect(Collectors.toList()));
        }

    }

    private void check(List<Row> datas, List<Schema> schemas) {
    }
}
