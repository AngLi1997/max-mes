package com.bmos.lims2.web.inspect.scheme.vo.request;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 检验方案数据点配置保存请求VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案数据点配置保存请求")
public class InspectionSchemeDataPointSaveReqVO {

    @ApiModelProperty("原始数据点ID")
    private Long dataPointId;

    @ApiModelProperty(value = "数据点名称", required = true)
    @NotBlank(message = "数据点名称不能为空")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9-_]{1,100}$", message = "数据点名称只能包含中文、字母、数字、下划线和中划线，长度不超过100")
    private String name;

    @ApiModelProperty(value = "数据点类型", required = true)
    @NotNull(message = "数据点类型不能为空")
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

    @ApiModelProperty("判定配置列表")
    @Valid
    private List<InspectionSchemeJudgmentSaveReqVO> judgments;
} 