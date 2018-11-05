package com.wolf;

import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Chars;
import com.wolf.parser.Row;
import com.wolf.parser.RowColumn;
import com.wolf.store.FileStoreEngine;
import com.wolf.store.StoreEngine;
import com.wolf.utils.Logger;
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
        List<String> contents = Lists.newArrayList("asasa","first","second","third","forth");
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
}
