package com.bmos.lims2.web.material.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 物料自定义字段类型VO
 */
@Getter
@Setter
@ApiModel(value = "物料自定义字段类型VO")
public class MaterialFieldTypeVO {

    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    /**
     * 字段类型名称
     */
    @ApiModelProperty(value = "字段类型名称")
    private String fieldTypeName;

    /**
     * 当前字段类型下的字段列表
     */
    @ApiModelProperty(value = "当前字段类型下的字段列表")
    private List<MaterialFieldVO> fieldList;

}
