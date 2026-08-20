package com.bmos.common.util.collection;

import lombok.Data;

/**
 * @Author yigaohui
 * @Description 字典bean
 * 只有code和text，可用于展示下拉框
 * @Date 2023/7/21 10:22
 */
@Data
public class DictBean<T> implements IDict<T> {
    private final T code;
    private final String text;
}
