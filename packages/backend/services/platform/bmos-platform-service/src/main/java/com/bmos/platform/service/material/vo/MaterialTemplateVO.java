package com.bmos.platform.service.material.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.bmos.platform.common.enums.material.IsSubMaterialEnum;
import com.bmos.platform.service.material.converter.IsSubMaterialConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Font;

/**
 * @ClassName MaterialTemplateVO
 * @Description TODO
 * @Author Ren Jin Guang
 * @Date 2024/12/24 15:02
 */
@Setter
@Getter
@ToString
public class MaterialTemplateVO {

    @ExcelIgnore
    private Long id;

    @ExcelProperty(value = "所属分类合并编码(必填)",index = 0)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String categoryMergeCode;

    @ExcelProperty(value = "物料名称(必填)",index = 1)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String name;

    @ExcelProperty(value = "物料编码(必填)",index = 2)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String code;

    @ExcelProperty(value = "物料规格(必填)",index = 3)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String specification;

    @ExcelProperty(value = "物料单位名称(必填)",index = 4)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String unitName;

    @ExcelProperty(value = "成员物料(必填)",index = 5,converter = IsSubMaterialConverter.class)
    @HeadFontStyle(color = Font.COLOR_RED)
    private IsSubMaterialEnum isSubMaterial;

    @ExcelProperty(value = "所属物料合并编码",index = 6)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String principalMergeCode;

    @ExcelIgnore
    @ApiModelProperty("分类id")
    private Long categoryId;

    @ExcelIgnore
    @ApiModelProperty("单位id")
    private Long unitId;

    @ExcelIgnore
    @ApiModelProperty("所属成员物料id")
    private Long principalMaterialId;

    @ExcelIgnore
    @ApiModelProperty("合并编码")
    private String mergeCode;

}
