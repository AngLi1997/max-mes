package com.bmos.mes.service.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("产品下拉列表VO")
public class ProductListVO {

    @ApiModelProperty("产品ID")
    private Long id;

    @ApiModelProperty("产品名称")
    private String name;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("规格")
    private String specification;

    @ApiModelProperty("内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty("包装规格")
    private String packingSpecification;

    @ApiModelProperty("产品标识")
    private String productMark;
}
