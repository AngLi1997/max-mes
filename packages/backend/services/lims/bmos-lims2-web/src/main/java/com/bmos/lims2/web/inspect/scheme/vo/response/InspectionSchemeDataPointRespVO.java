package com.bmos.lims2.web.inspect.scheme.vo.response;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 检验方案数据点配置响应VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案数据点配置响应")
public class InspectionSchemeDataPointRespVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("原始数据点ID")
    private Long dataPointId;

    @ApiModelProperty("数据点名称")
    private String name;

    @ApiModelProperty("数据点类型：NUMBER-数值类型, TEXT-文本类型, OPTION-选项类型")
    private DataPointTypeEnum pointType;

    @ApiModelProperty("趋势线配置(JSON)")
    private String trendLineConfig;

    @ApiModelProperty("选项配置(JSON)")
    private String options;

    @ApiModelProperty("时间类型显示格式（仅当pointType为TIME时有效）")
    private String timeFormat;

    private String dateStyle;

    @ApiModelProperty("时间类型舍入：true向上，false向下")
    private Boolean roundingUp;

    @ApiModelProperty("最终判定表达式")
    private String finalExpression;

    @ApiModelProperty("判定配置列表")
    private List<InspectionSchemeJudgmentRespVO> judgments;

    @ApiModelProperty("是否被结论判定引用")
    private Boolean referencedByJudgment;

    @ApiModelProperty("记录id（绑定记录组件用）")
    private Long recordId;

    @ApiModelProperty("记录版本id（绑定记录组件用）")
    private Long recordVersionId;

    @ApiModelProperty("记录组件id（bm_batch_record_component.id）")
    private Long componentId;
} 