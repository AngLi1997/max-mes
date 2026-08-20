package com.bmos.lims2.web.inspect.scheme.vo.response;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 检验方案分析项配置响应VO
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案分析项配置响应")
public class InspectionSchemeParameterRespVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("分析项ID")
    private Long parameterId;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("分析项编码")
    private String parameterCode;

    @ApiModelProperty("分析方法ID")
    private Long recordId;

    @ApiModelProperty("分析方法编码")
    private String recordCode;

    @ApiModelProperty("分析方法版本ID")
    private Long recordVersionId;

    @ApiModelProperty("记录项ID（bm_batch_record_item.id）")
    private Long recordItemId;

    @ApiModelProperty("分析方法名称")
    private String recordName;

    @ApiModelProperty("标准规定")
    private String standardRule;

    @ApiModelProperty("是否报告项")
    private Boolean isReportable;

    @ApiModelProperty("是否可执行")
    private Boolean isExecutable;

    @ApiModelProperty("数据点配置列表")
    private List<InspectionSchemeDataPointRespVO> dataPoints;

    @ApiModelProperty("执行方式：LIMS/ELN")
    private ExecuteMethodEnum executeMethod;
}