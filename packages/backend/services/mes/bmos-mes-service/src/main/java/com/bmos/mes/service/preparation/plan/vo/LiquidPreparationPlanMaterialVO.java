package com.bmos.mes.service.preparation.plan.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("配液计划物料列表VO")
@Data
public class LiquidPreparationPlanMaterialVO {

    @ApiModelProperty("配方物料id")
    private Long id;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("目标浓度")
    private String targetConcentration;

    @ApiModelProperty("浓度参数编码")
    private String consistenceParamCode;

    @ApiModelProperty("浓度参数名称")
    private String consistenceParamName;

    @ApiModelProperty("单位名称")
    private String unitName;

}
