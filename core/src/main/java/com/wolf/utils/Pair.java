package com.wolf.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by slj on 2018-11-18
 */
@Data
@AllArgsConstructor
public class Pair<T,V> {

    T key;

    V value;
}
