package com.bmos.lims2.web.stability.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 稳定性方案版本复制请求VO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
@ApiModel("稳定性方案版本复制请求")
public class StabilitySchemeVersionCopyReqVO {

    @ApiModelProperty(value = "原版本ID", required = true)
    @NotNull(message = "原版本ID不能为空")
    private Long sourceVersionId;

    @ApiModelProperty(value = "新版本号", required = true)
    @NotBlank(message = "新版本号不能为空")
    private String newVersionNo;

    @ApiModelProperty("版本描述")
    private String description;
}
