package com.bmos.mes.service.lotrelease.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批签发模板分页vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 14:40
 */
@Data
@ApiModel("批签发模板分页vo")
public class LotReleaseTemplatePageVO {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "批签发模板分类id", example = "1")
    private Long categoryId;

    @ApiModelProperty(value = "批签发模板名称", example = "模板")
    private String name;

    @ApiModelProperty(value = "批签发模板分类路径", example = "流感疫苗/单价病毒")
    private String categoryNamePath;
}
