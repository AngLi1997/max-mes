package com.bmos.lims2.web.inspect.scheme.vo.request;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 检验方案分析项配置保存请求VO
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案分析项配置保存请求")
public class InspectionSchemeParameterSaveReqVO {

    @ApiModelProperty(value = "分析项ID", required = true)
    @NotNull(message = "分析项ID不能为空")
    private Long parameterId;

    @ApiModelProperty("标准规定")
    private String standardRule;

    @ApiModelProperty("分析方法ID")
    private Long recordId;

    @ApiModelProperty("分析方法编码")
    private String recordCode;

    @ApiModelProperty("分析方法版本ID")
    private Long recordVersionId;

    @ApiModelProperty("是否报告项")
    private Boolean isReportable;

    @ApiModelProperty("是否可执行")
    private Boolean isExecutable;

    @ApiModelProperty("数据点配置列表")
    @Valid
    private List<InspectionSchemeDataPointSaveReqVO> dataPoints;

    @ApiModelProperty("执行方式：LIMS/ELN")
    private ExecuteMethodEnum executeMethod;
}