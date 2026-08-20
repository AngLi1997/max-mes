package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("工序列表项VO")
public class ProcedureListItemVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    /**
     * 时长
     */
    @ApiModelProperty("时长")
    private Long duration;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String timeUnit;
}
