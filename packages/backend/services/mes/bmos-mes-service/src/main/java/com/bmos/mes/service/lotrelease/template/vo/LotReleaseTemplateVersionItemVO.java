package com.bmos.mes.service.lotrelease.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/29 15:38
 */
@Data
@ApiModel(value = "批签发模板版本列表item")
public class LotReleaseTemplateVersionItemVO {

    @ApiModelProperty(value = "模板id", example = "1")
    private Long id;

    @ApiModelProperty(value = "模板版本", example = "模板版本")
    private String version;

    @ApiModelProperty(value = "是否默认", example = "true")
    private Boolean isDefault;
}
