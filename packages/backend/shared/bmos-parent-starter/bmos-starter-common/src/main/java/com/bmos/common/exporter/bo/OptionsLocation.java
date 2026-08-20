package com.bmos.common.exporter.bo;

import lombok.Data;

/**
 * 选项所在坐标
 * @author : yigaohui
 * @version : 1.0
 */
@Data
public class OptionsLocation {

    /**
     * 选项所在所属 sheet页
     */
    private String optionSheetName;
    /**
     * 选项值
     */
    private OptionBo optionBo;
    /**
     * sheet页的数据行数
     */
    private int dataRows;
    /**
     * 选项字典再字典 sheet页所在列
     */
    private int dicSheetCols;

    public OptionsLocation() {
    }

    public OptionsLocation(String optionSheetName, OptionBo optionBo, int dicSheetCols) {
        this.optionBo = optionBo;
        this.optionSheetName = optionSheetName;
        this.dicSheetCols=dicSheetCols;
    }
}
