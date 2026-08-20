package com.bmos.lims2.web.stability.scheme.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 稳定性方案检验计划响应VO
 */
@Data
@ApiModel("稳定性方案检验计划响应")
public class StabilitySchemePlanRespVO {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("版本ID")
    private Long versionId;

    @ApiModelProperty("试验类型")
    private String experimentType;

    @ApiModelProperty("试验储存条件")
    private String storageCondition;

    @ApiModelProperty("整体取样量")
    private String totalSampleAmount;

    @ApiModelProperty("整体取样量单位")
    private String totalSampleUnit;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("时间点列表")
    private List<StabilitySchemePlanTimepointRespVO> timepoints;
}
