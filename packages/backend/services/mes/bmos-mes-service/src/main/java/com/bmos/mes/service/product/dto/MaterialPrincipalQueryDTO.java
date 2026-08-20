package com.bmos.mes.service.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("关联物料查询DTO")
public class MaterialPrincipalQueryDTO {

    @ApiModelProperty(value = "物料分类id",required = true)
    @NonNull
    private Long materialCategoryId;

    @ApiModelProperty(value = "是否过滤",required = true)
    private Boolean filter;

}
