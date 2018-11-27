package com.wolf;

import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import com.wolf.parser.Row;
import com.wolf.parser.RowColumn;
import com.wolf.store.directfile.FileStoreEngine;
import com.wolf.store.StoreEngine;
import com.wolf.store.index.ArrayIndex;
import com.wolf.store.index.VarcharDataHolder;
import com.wolf.utils.Logger;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * Created by slj on 2018-11-03
 */
public class FileStoreEngineTest {

    Logger logger= new Logger(this.getClass().getName());

    @Test
    public void testContent(){

        List<String> contents = Lists.newArrayList("asasa","1","ft34t34t","你好中国");
        FileStoreEngine.Data data = new FileStoreEngine.Data(contents);
        new Logger(this.getClass().getName()).log(new String(data.getBytes()));
        new Logger(this.getClass().getName()).log(Bytes.asList(data.getBytes()).toString());
    }

    @Test
    public void testWrite()throws Exception{
        List<String> contents = Lists.newArrayList("w","22","f","1","2","3");
        List<RowColumn> rowColumns = contents.stream().map(e->new RowColumn(1,1,e)).collect(Collectors.toList());
        StoreEngine storeEngine = new FileStoreEngine();
        storeEngine.insert(Lists.newArrayList(new Row(rowColumns)),"user",Lists.newArrayList());
    }

    @Test
    public void testqueryAll()throws Exception{
        StoreEngine storeEngine = new FileStoreEngine();
        List<Row> rows = storeEngine.queryAll("user");
        rows.forEach(e->logger.log(e.toString()));
    }

    @Test
    public void test_randomAccessFile() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("/Users/lijiang.song/data/raf.a","rw");
        //randomAccessFile.seek(0);
        randomAccessFile.writeUTF("helloworld");
        randomAccessFile.writeUTF("1");

        //randomAccessFile.setLength(1024);
        long pointer = randomAccessFile.getFilePointer();
        logger.log(""+pointer);
    }

    @Test
    public void  testIndex() {
        ArrayIndex index =new ArrayIndex();
        index.setStoreEngine(new FileStoreEngine());
        index.setTableName("user");
        index.init(new VarcharDataHolder(),1);
        System.out.println(Arrays.asList(index.getIndexData()));
    }
    @Test
    public void test_getRow()throws IOException{
        FileStoreEngine storeEngine = new FileStoreEngine();
        logger.log(storeEngine.getByPosition("user",64).toString());
    }
    @Test
    public void testIndexFind()throws IOException{
        ArrayIndex index =new ArrayIndex();
        index.setStoreEngine(new FileStoreEngine());
        index.setTableName("user");
        index.init(new VarcharDataHolder(),1);
        VarcharDataHolder indexKey = new VarcharDataHolder();
        indexKey.setKey("22");
        logger.log(index.queryByKey(indexKey));
    }
}
