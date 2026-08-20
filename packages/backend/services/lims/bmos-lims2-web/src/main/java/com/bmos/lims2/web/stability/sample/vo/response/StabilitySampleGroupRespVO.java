package com.bmos.lims2.web.stability.sample.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性样品管理分组响应VO（以考察计划为分组单元）
 */
@Data
@ApiModel("稳定性样品管理分组")
public class StabilitySampleGroupRespVO {

    @ApiModelProperty("考察计划ID")
    private Long planId;

    @ApiModelProperty("稳定性考察计划编号")
    private String planCode;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("考察计划创建人")
    private String planCreateBy;

    @ApiModelProperty("考察计划创建人姓名")
    private String planCreateByUserName;

    @ApiModelProperty("考察计划创建时间")
    private LocalDateTime planCreateTime;

    @ApiModelProperty("开始时间")
    private LocalDate startDate;

    @ApiModelProperty("计划结束时间")
    private LocalDate planEndDate;

    @ApiModelProperty("样品列表")
    private List<StabilitySampleRowRespVO> samples;
}
