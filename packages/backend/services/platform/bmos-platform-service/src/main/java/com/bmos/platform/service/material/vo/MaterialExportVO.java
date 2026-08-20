package com.bmos.platform.service.material.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.bmos.platform.common.enums.material.IsSubMaterialEnum;
import com.bmos.platform.service.material.converter.IsSubMaterialConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName MaterialExportVO
 * @Description TODO
 * @Author Ren Jin Guang
 * @Date 2024/12/25 14:12
 */
@Setter
@Getter
@ToString
public class MaterialExportVO {

    @ExcelProperty(value = "物料名称", index = 0)
    private String name;

    @ExcelProperty(value = "物料编码",index = 1)
    private String code;

    @ExcelProperty(value = "物料分类",index = 2)
    private String category;

    @ExcelProperty(value = "物料分类编码",index = 3)
    private String categoryMergeCode;

    @ExcelProperty(value = "规格",index = 4)
    private String specification;

    @ExcelProperty(value = "成员物料",index = 5,converter = IsSubMaterialConverter.class)
    private IsSubMaterialEnum subMaterialName;

    @ExcelProperty(value = "单位",index = 6)
    private String unitName;

    @ExcelProperty(value = "所属物料名称",index = 7)
    private String principalMaterialName;

    @ExcelProperty(value = "所属物料编码",index = 8)
    private String principalMaterialCode;

    @ExcelProperty(value = "备注",index = 9)
    private String remark;

    @ExcelIgnore
    private Long materialCategoryId;

    @ExcelIgnore
    private Boolean subMaterial;

    @ExcelIgnore
    private Long principalMaterialId;
}
