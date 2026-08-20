package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("可用于报告验证的检验单")
public class EligibleOrderDTO {
    @ApiModelProperty("检验单ID")
    private Long id;
    @ApiModelProperty("检验单号")
    private String orderNo;
    @ApiModelProperty("批次号")
    private String batchNo;
}


