package com.wolf.parser;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by slj on 2018-11-03
 */
@Data
@AllArgsConstructor
public class Row {

    List<RowColumn> data;

}
