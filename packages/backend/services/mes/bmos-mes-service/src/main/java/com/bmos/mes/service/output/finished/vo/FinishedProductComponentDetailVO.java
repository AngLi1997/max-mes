package com.bmos.mes.service.output.finished.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("成品产出组件详情")
public class FinishedProductComponentDetailVO {

    @ApiModelProperty("组件id")
    private Long id;

    @ApiModelProperty("成品编码")
    private String productMergeCode;

    @ApiModelProperty("成品名称")
    private String productName;

    @ApiModelProperty("成品批号")
    private String productBatchNo;

    @ApiModelProperty("成品规格")
    private String specification;

    @ApiModelProperty("产品标准单位")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("产品id")
    private Long productId;


}
