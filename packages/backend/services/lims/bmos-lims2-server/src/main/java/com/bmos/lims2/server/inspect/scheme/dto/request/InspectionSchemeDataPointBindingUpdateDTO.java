package com.bmos.lims2.server.inspect.scheme.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 方案数据点与记录字段绑定更新DTO
 * @Author: yigaohui
 * @Date: 2025/11/11 10:30
 */
@Data
public class InspectionSchemeDataPointBindingUpdateDTO {

    @ApiModelProperty("数据点配置ID（lm_inspection_scheme_data_point.id）")
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

