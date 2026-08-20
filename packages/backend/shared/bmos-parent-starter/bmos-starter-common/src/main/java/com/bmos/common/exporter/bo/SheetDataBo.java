package com.bmos.common.exporter.bo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yigaohui
 * @date 2022/8/24 19:38
 */
@Data
public class SheetDataBo<T> {
    /**
     * sheet页实体
     */
    private Class<T> head;
    /**
     * sheet页名称
     */
    private String sheetName;
    /**
     * 数据
     */
    private List<T> data;
    /**
     * 需要锁定的列
     */
    private Set<Integer> lockColumns;
    /**
     * 下拉选择数据
     */
    private List<OptionBo> optionBos = new ArrayList<>();


    public SheetDataBo(String sheetName, Class<T> head,List<T> data, List<OptionBo> optionBos) {
        this.sheetName = sheetName;
        this.head = head;
        this.data = data;
        this.optionBos = optionBos;
    }
    public SheetDataBo(String sheetName, Class<T> head,List<T> data, List<OptionBo> optionBos,Set<Integer> lockColumns) {
        this.sheetName = sheetName;
        this.head = head;
        this.data = data;
        this.optionBos = optionBos;
        this.lockColumns = lockColumns;
    }
}
