package com.bmos.mes.service.lotrelease.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批签发生成批次引用预览DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 13:33
 */
@Data
@ApiModel(value = "批签发生成批次引用预览DTO")
public class LotReleaseGeneratePreviewDTO {

    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;
}
