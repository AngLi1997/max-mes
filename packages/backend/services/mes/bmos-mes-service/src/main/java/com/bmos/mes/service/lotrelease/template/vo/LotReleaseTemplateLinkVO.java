package com.bmos.mes.service.lotrelease.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 批签发引用模板信息vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/28 15:53
 */
@Data
@ApiModel("批签发引用模板信息vo")
public class LotReleaseTemplateLinkVO {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "模板名称", example = "模板")
    private String name;

    @ApiModelProperty(value = "模板版本信息")
    private List<LotReleaseTemplateVersionLinkVO> list;
}
