package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 方案数据点与记录字段绑定更新请求VO
 * @Author: yigaohui
 * @Date: 2025/11/11 10:35
 */
@Data
@ApiModel("方案数据点与记录字段绑定更新请求")
public class InspectionSchemeDataPointBindingUpdateReqVO {

    @ApiModelProperty(value = "数据点配置ID（lm_inspection_scheme_data_point.id）", required = true)
    @NotNull(message = "数据点配置ID不能为空")
    private Long dataPointConfigId;

    @ApiModelProperty("记录id")
    private Long recordId;

    @ApiModelProperty("记录版本id")
    private Long recordVersionId;

    @ApiModelProperty("记录组件id（bm_batch_record_component.id）")
    private Long componentId;

    @ApiModelProperty("记录项id（bm_batch_record_item.id）")
    private Long recordItemId;

    @ApiModelProperty("字段id（fieldId，对应记录组件字段）")
    private Long fieldId;
}

