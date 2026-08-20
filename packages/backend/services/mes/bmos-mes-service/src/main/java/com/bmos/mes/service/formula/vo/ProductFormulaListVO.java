package com.bmos.mes.service.formula.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("产品配方列表VO")
public class ProductFormulaListVO {

    @ApiModelProperty("产品配方名称")
    private String productFormulaName;

    @ApiModelProperty("产品配方版本id")
    private Long productFormulaVersionId;

    @ApiModelProperty("产品配方版本号")
    private String productFormulaVersionNo;

    @ApiModelProperty("是否为删除数据")
    private Boolean disabled;

    @ApiModelProperty("启停状态")
    private Boolean enable;

    @ApiModelProperty("删除状态")
    private Boolean isDeleted;

}
