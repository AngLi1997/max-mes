package com.bmos.platform.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 标签打印预览参数
 * @author liang
 * @version 1.0.0
 * @date 2024/3/14 18:19
 */
@Data
@ApiModel("标签打印预览参数")
public class PrintPreviewDTO {

    /**
     * 场景id
     */
    @ApiModelProperty(value = "场景id", example = "1")
    private Long sceneId;

    /**
     * 参数
     */
    @ApiModelProperty("参数")
    private Map<String, Object> body;
}
