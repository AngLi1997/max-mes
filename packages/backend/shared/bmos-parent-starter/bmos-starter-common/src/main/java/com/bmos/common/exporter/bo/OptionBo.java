package com.bmos.common.exporter.bo;

import lombok.Data;

import java.util.List;

/**
 * @author yigaohui
 * @date 2022/8/24 13:45
 */
@Data
public class OptionBo {

    /**
     * 字典值描述
     */
    private String optionName;
    /**
     * 列，excel的哪一列做下拉框，第一列从0算起。
     */
    private int optionCol;
    /**
     * 选项集合
     */
    private List<String> options;

    public OptionBo(String optionName, int optionCol, List<String> options) {
        this.optionName = optionName;
        this.optionCol = optionCol;
        this.options = options;
    }
}
