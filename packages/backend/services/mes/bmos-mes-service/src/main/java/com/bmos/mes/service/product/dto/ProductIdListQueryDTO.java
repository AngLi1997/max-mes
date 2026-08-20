package com.bmos.mes.service.product.dto;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("产品id列表查询DTO")
public class ProductIdListQueryDTO {

    @ApiModelProperty("产品分类")
    private CategoryInfoTypeEnum categoryInfoType;

    @ApiModelProperty("分类id")
    private Long categoryId;

}
