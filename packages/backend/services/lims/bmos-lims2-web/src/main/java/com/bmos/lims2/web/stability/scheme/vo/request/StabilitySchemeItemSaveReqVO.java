package com.bmos.lims2.web.stability.scheme.vo.request;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性方案检验项目配置保存请求VO
 */
@Data
@ApiModel("稳定性方案检验项目配置保存请求")
public class StabilitySchemeItemSaveReqVO {

    @ApiModelProperty(value = "版本ID", required = true)
    @NotNull(message = "版本ID不能为空")
    private Long versionId;

    @ApiModelProperty("检验项目配置列表")
    @Valid
    private List<ItemVO> items;

    @Data
    @ApiModel("检验项目配置")
    public static class ItemVO {

        @ApiModelProperty("检验项目配置记录ID（有值则更新，无值则新增）")
        private Long itemConfigId;

        @ApiModelProperty(value = "检验项目ID", required = true)
        @NotNull(message = "检验项目ID不能为空")
        private Long inspectItemId;

        @ApiModelProperty("检验工时数量")
        private Integer duration;

        @ApiModelProperty("检验工时单位")
        private ItemDurationUnitEnum timeUnit;

        @ApiModelProperty("检验班组ID列表")
        private List<Long> teams;

        @ApiModelProperty("是否必检项")
        private Boolean isRequired;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("分析项配置列表")
        @Valid
        private List<ParameterVO> inspectionParameters;
    }

    @Data
    @ApiModel("分析项配置")
    public static class ParameterVO {

        @ApiModelProperty("分析项配置记录ID（有值则更新，无值则新增）")
        private Long parameterConfigId;

        @ApiModelProperty(value = "分析项ID", required = true)
        @NotNull(message = "分析项ID不能为空")
        private Long parameterId;

        @ApiModelProperty("标准规定")
        private String standardRule;

        @ApiModelProperty("是否可执行项")
        private Boolean isExecutable;

        @ApiModelProperty("是否报告项")
        private Boolean isReportable;

        @ApiModelProperty("最终判定表达式")
        private String finalExpression;

        @ApiModelProperty("分析方法记录ID")
        private Long recordId;

        @ApiModelProperty("分析方法编码")
        private String recordCode;

        @ApiModelProperty("分析方法版本ID")
        private Long recordVersionId;

        @ApiModelProperty("分析方法记录项ID")
        private Long recordItemId;
    }
}
