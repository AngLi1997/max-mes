package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/10/28 14:50
 */
@Data
@ApiModel("扫描皮重标签DTO")
public class ScanTareWeighTagDTO {

    /**
     * 皮重标签id
     */
    @NotNull
    @ApiModelProperty(value = "皮重标签id", example = "1")
    private Long tareWeighId;

    /**
     * 单位id
     */
    @NotNull
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;
}
