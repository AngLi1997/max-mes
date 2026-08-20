package com.bmos.mes.service.lotrelease.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批签发动态字段查询DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 13:33
 */
@Data
@ApiModel(value = "批签发动态字段查询DTO")
public class LotReleaseQueryDynamicReportDTO {

    @ApiModelProperty(value = "批签发模板id", example = "1")
    private Long lotReleaseTemplateId;

    @ApiModelProperty(value = "批签发版本", example = "1")
    private String lotReleaseVersion;
}
