package com.wolf.parser;

import lombok.Data;

/**
 * Created by slj on 2018-11-03
 */
@Data
public class Column {

    private int id;

    private String name;

    private Class type;

    private int length;

    private int sort;

}
