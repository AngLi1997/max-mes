package com.bmos.lims2.web.stability.scheme.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 稳定性方案基础信息保存响应VO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
@ApiModel("稳定性方案基础信息保存响应")
public class StabilitySchemeBasicSaveRespVO {

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("版本ID")
    private Long versionId;

    @ApiModelProperty("方案名称")
    private String schemeName;

    @ApiModelProperty("方案编码")
    private String schemeCode;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("是否新增")
    private Boolean isNew;
}
