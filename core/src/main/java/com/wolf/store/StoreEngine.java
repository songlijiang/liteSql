package com.wolf.store;

import com.wolf.parser.Row;
import com.wolf.parser.Schema;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by slj on 2018-11-03
 */
public interface StoreEngine {

    int insert(List<Row> datas,String tableName,List<Schema> schemas)  throws IOException;

    List<Row> queryAll(String table) throws IOException;

    List<Row> queryByConditions(List<Predicate> predicates);
}
