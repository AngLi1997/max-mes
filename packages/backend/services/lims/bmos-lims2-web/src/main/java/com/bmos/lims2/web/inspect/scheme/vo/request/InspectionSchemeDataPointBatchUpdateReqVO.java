package com.bmos.lims2.web.inspect.scheme.vo.request;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 检验方案数据点批量更新请求VO
 *
 * @author yigaohui
 * @since 2025/01/29 20:00
 */
@Data
@ApiModel("检验方案数据点批量更新请求")
public class InspectionSchemeDataPointBatchUpdateReqVO {

    /**
     * 关联的方案ID
     */
    @ApiModelProperty("关联的方案ID")
    private Long schemeId;

    @ApiModelProperty("关联数据点ID")
    private Long dataPointConfigId;

    /**
     * 关联的版本ID
     */
    @ApiModelProperty("关联的版本ID")
    private Long versionId;

    @ApiModelProperty("关联的实验包ID")
    private Long packageId;

    @ApiModelProperty("关联的检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("关联的参数ID")
    private Long parameterId;

    /**
     * 关联的分析项配置ID
     */
    @ApiModelProperty("关联的分析项配置ID")
    private Long parameterConfigId;

    /**
     * 原始数据点ID
     */
    @ApiModelProperty("原始数据点ID")
    private Long dataPointId;

    /**
     * 数据点名称
     */
    @ApiModelProperty("数据点名称")
    private String name;

    /**
     * 数据点类型：NUMBER-数值类型, TEXT-文本类型, OPTION-选项类型
     */
    @ApiModelProperty("数据点类型：NUMBER-数值类型, TEXT-文本类型, OPTION-选项类型")
    private DataPointTypeEnum pointType;

    @ApiModelProperty("时间格式")
    private String timeFormat;

    private String dateStyle;

    @ApiModelProperty("时间类型舍入：true向上，false向下")
    private Boolean roundingUp;
    /**
     * 趋势线配置(JSON)
     */
    @ApiModelProperty("趋势线配置(JSON)")
    private String trendLineConfig;

    /**
     * 选项配置(JSON)
     */
    @ApiModelProperty("选项配置(JSON)")
    private String options;

    /**
     * 是否报告显示
     */
    @ApiModelProperty("是否报告显示")
    private Boolean reportDisplay;

    @ApiModelProperty("记录id（绑定记录组件用）")
    private Long recordId;

    @ApiModelProperty("记录版本id（绑定记录组件用）")
    private Long recordVersionId;

    @ApiModelProperty("记录组件id（bm_batch_record_component.id）")
    private Long componentId;

    @ApiModelProperty("记录项id（bm_batch_record_item.id）")
    private Long recordItemId;

    @ApiModelProperty("字段id（fieldId，对应记录组件字段）")
    private Long fieldId;
}
