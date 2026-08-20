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
 * 保存稳定性方案版本检验项目-分析项请求VO
 * 前端平铺提交，后台按 inspectItemId 分组保存到检验项目表和分析项表
 */
@Data
@ApiModel("保存稳定性方案版本检验项目-分析项请求（平铺）")
public class StabilitySchemeVersionSaveItemsReqVO {

    @ApiModelProperty(value = "版本ID", required = true)
    @NotNull(message = "版本ID不能为空")
    private Long versionId;

    @ApiModelProperty("分析项列表（平铺，含所属检验项目信息）")
    @Valid
    private List<ParameterItemVO> parameters;

    @Data
    @ApiModel("分析项条目（含检验项目信息）")
    public static class ParameterItemVO {

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

        @ApiModelProperty(value = "分析项ID", required = true)
        @NotNull(message = "分析项ID不能为空")
        private Long parameterId;

        @ApiModelProperty("标准规定")
        private String standardRule;

        @ApiModelProperty("是否可执行项")
        private Boolean isExecutable;

        @ApiModelProperty("是否报告项")
        private Boolean isReportable;

        @ApiModelProperty("执行方式（LIMS/ELN）")
        private ExecuteMethodEnum executeMethod;

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
