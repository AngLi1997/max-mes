package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 新增方案版本-从源版本复制 请求VO
 * @Author: yigaohui
 * @Date: 2025/09/03 10:35
 */
@Data
@ApiModel("新增方案版本-从源版本复制 请求")
public class InspectionSchemeVersionCopyReqVO {

    @NotNull
    @ApiModelProperty(value = "源版本ID", required = true)
    private Long sourceVersionId;

    @NotBlank
    @ApiModelProperty(value = "新版本号", required = true)
    private String newVersionNo;

    @ApiModelProperty(value = "版本描述")
    private String description;
}


