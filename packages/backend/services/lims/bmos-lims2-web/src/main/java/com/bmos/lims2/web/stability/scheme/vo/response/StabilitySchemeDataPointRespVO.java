package com.bmos.lims2.web.stability.scheme.vo.response;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 稳定性方案数据点配置响应VO
 */
@Data
@ApiModel("稳定性方案数据点配置响应")
public class StabilitySchemeDataPointRespVO {

    @ApiModelProperty("数据点配置ID")
    private Long dataPointConfigId;

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("版本ID")
    private Long versionId;

    @ApiModelProperty("检验项目配置ID")
    private Long itemConfigId;

    @ApiModelProperty("分析项配置ID")
    private Long parameterConfigId;

    @ApiModelProperty("原始分析项ID")
    private Long parameterId;

    @ApiModelProperty("原始数据点ID")
    private Long dataPointId;

    @ApiModelProperty("数据点名称")
    private String name;

    @ApiModelProperty("数据点类型")
    private DataPointTypeEnum pointType;

    @ApiModelProperty("趋势线配置(JSON)")
    private String trendLineConfig;

    @ApiModelProperty("选项配置(JSON)")
    private String options;

    @ApiModelProperty("时间格式")
    private String timeFormat;

    @ApiModelProperty("日期样式")
    private String dateStyle;

    @ApiModelProperty("向上舍入")
    private Boolean roundingUp;

    @ApiModelProperty("是否报告显示")
    private Boolean reportDisplay;

    @ApiModelProperty("绑定记录ID")
    private Long recordId;

    @ApiModelProperty("绑定记录版本ID")
    private Long recordVersionId;

    @ApiModelProperty("绑定记录组件ID")
    private Long componentId;

    @ApiModelProperty("绑定记录项ID")
    private Long recordItemId;

    @ApiModelProperty("绑定字段ID")
    private Long fieldId;

    @ApiModelProperty("是否被判定引用")
    private Boolean referencedByJudgment;
}
