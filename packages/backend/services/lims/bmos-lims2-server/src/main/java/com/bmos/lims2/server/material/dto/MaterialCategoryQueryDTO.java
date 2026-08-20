package com.bmos.lims2.server.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 生产物料分类查询DTO
 */
@Getter
@Setter
@ApiModel("")
public class MaterialCategoryQueryDTO {

    /**
     * 类别信息类型
     * {@link com.bmos.lims2.common.enums.CategoryInfoTypeEnum}
     */
    @ApiModelProperty("类别信息类型")
    private Integer categoryType;

    /**
     * 关键字 模糊查询
     */
    @ApiModelProperty("关键字")
    private String keyword;

}
