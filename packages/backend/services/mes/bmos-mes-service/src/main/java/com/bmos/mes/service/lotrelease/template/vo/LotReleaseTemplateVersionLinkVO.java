package com.bmos.mes.service.lotrelease.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批签发引用模板版本信息vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/28 16:01
 */
@ApiModel("批签发引用模板版本信息vo")
@Data
public class LotReleaseTemplateVersionLinkVO {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "模板版本", example = "模板版本")
    private String version;

    @ApiModelProperty(value = "文件路径", example = "文件路径")
    private String templateUrl;
}
