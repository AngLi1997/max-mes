package com.bmos.mes.service.plan.production.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@ApiModel("直接创建指令单VO")
@Data
public class DirectlyCreateBuildNoVO {

    @ApiModelProperty("指令单编号规则")
    private String planNoCode;

    @ApiModelProperty("指令单编号")
    private String planNo;

    @ApiModelProperty("生产批号编号规则")
    private String batchNoCode;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("编号生成日期")
    private LocalDate codeApplyTime;


}
