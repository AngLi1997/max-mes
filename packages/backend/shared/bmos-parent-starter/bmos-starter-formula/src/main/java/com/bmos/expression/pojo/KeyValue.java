package com.bmos.expression.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyValue<K, V> {
    private K key;

    private V value;
}
