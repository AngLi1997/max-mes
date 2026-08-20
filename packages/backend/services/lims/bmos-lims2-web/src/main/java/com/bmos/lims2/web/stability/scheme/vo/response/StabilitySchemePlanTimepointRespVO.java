package com.bmos.lims2.web.stability.scheme.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 稳定性方案检验计划时间点响应VO
 */
@Data
@ApiModel("稳定性方案检验计划时间点响应")
public class StabilitySchemePlanTimepointRespVO {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("计划ID")
    private Long planId;

    @ApiModelProperty("版本ID")
    private Long versionId;

    @ApiModelProperty("检测时间点数值")
    private Integer timeValue;

    @ApiModelProperty("时间单位（月/年/天/周）")
    private String timeUnit;

    @ApiModelProperty("取样量")
    private String sampleAmount;

    @ApiModelProperty("取样量单位")
    private String sampleUnit;

    @ApiModelProperty("是否全选方案所有分析项")
    private Boolean selectAll;

    @ApiModelProperty("分析项引用列表（含配置ID、原始ID/编码、检验项目信息）")
    private List<ParamRefRespVO> paramRefs;

    @ApiModelProperty("排序")
    private Integer sort;

    @Data
    @ApiModel("时间点分析项引用响应")
    public static class ParamRefRespVO {

        @ApiModelProperty("ID")
        private Long id;

        @ApiModelProperty("分析项配置ID")
        private Long parameterConfigId;

        @ApiModelProperty("原始分析项ID")
        private Long parameterId;

        @ApiModelProperty("分析项编码")
        private String parameterCode;

        @ApiModelProperty("检验项目配置ID")
        private Long itemConfigId;

        @ApiModelProperty("检验项目ID")
        private Long inspectItemId;

        @ApiModelProperty("检验项目编码")
        private String inspectItemCode;
    }
}
