package com.bmos.lims2.server.stability.statistics.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性方案下拉选项DTO
 */
@Getter
@Setter
public class StabilitySchemeOptionDTO {

    @ApiModelProperty("方案ID")
    private Long id;

    @ApiModelProperty("方案版本ID")
    private Long versionId;

    @ApiModelProperty("方案名称")
    private String name;

    @ApiModelProperty("方案编码")
    private String code;

    @ApiModelProperty("方案版本号")
    private String versionNo;
}
