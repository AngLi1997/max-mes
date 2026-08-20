package com.bmos.mes.service.formula.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFormulaVersionAuditVO {

    @ApiModelProperty("配方版本id")
    private Long id;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("配方名称")
    private String name;

    @ApiModelProperty("配方版本号")
    private String versionNo;

    @ApiModelProperty("配方描述")
    private String description;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

}
