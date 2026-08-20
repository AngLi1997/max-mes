package com.bmos.mes.service.trace.material.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 15:12
 */
@Data
public class MaterialTraceTemplatePageVO {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "模板名称", example = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "产品名称", example = "产品名称")
    private String productName;

    @ApiModelProperty(value = "产品编码", example = "产品编码")
    private String mergeCode;

    @ApiModelProperty(value = "工艺名称", example = "工艺名称")
    private String processName;

    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean enabled;
}
