package com.bmos.lims2.web.stability.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性方案检验计划保存请求VO
 */
@Data
@ApiModel("稳定性方案检验计划保存请求")
public class StabilitySchemePlanSaveReqVO {

    @ApiModelProperty(value = "版本ID", required = true)
    @NotNull(message = "版本ID不能为空")
    private Long versionId;

    @ApiModelProperty("检验计划列表")
    @Valid
    private List<PlanVO> plans;

    @Data
    @ApiModel("检验计划")
    public static class PlanVO {

        @ApiModelProperty("检验计划记录ID（有值则更新，无值则新增）")
        private Long planId;

        @ApiModelProperty("试验类型")
        private String experimentType;

        @ApiModelProperty("试验储存条件")
        private String storageCondition;

        @ApiModelProperty("整体取样量")
        private String totalSampleAmount;

        @ApiModelProperty("整体取样量单位")
        private Long totalSampleUnit;

        @ApiModelProperty("排序")
        private Integer sort;

        @ApiModelProperty("时间点列表")
        @Valid
        private List<TimepointVO> timepoints;
    }

    @Data
    @ApiModel("时间点")
    public static class TimepointVO {

        @ApiModelProperty("时间点记录ID（有值则更新，无值则新增）")
        private Long timepointId;

        @ApiModelProperty("检测时间点数值")
        private Integer timeValue;

        @ApiModelProperty("时间单位（月/年/天/周）")
        private String timeUnit;

        @ApiModelProperty("取样量")
        private String sampleAmount;

        @ApiModelProperty("取样量单位")
        private String sampleUnit;

        @ApiModelProperty("是否全选方案所有分·析项（true=全选，不限于 paramRefs 中的列表）")
        private Boolean selectAll;

        @ApiModelProperty("分析项引用列表（selectAll=false 时有效）")
        private List<ParamRefVO> paramRefs;

        @ApiModelProperty("排序")
        private Integer sort;
    }

    @Data
    @ApiModel("时间点分析项引用")
    public static class ParamRefVO {

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

