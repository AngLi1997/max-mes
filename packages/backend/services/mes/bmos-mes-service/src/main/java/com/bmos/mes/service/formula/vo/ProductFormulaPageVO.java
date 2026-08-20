package com.bmos.mes.service.formula.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("配方分页vo")
@Data
public class ProductFormulaPageVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("配方名称")
    private String name;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("规格")
    private String productSpecification;

    @ApiModelProperty("启用版本")
    private String enableVersion;

}
