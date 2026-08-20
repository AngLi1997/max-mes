package com.bmos.lims2.server.inspect.parameter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 分析方法更新DTO
 * @Author: yigaohui
 * @Date: 2025/10/27 00:00
 */
@Getter
@Setter
@ApiModel("分析方法更新DTO")
public class InspectMethodUpdateDTO {

    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(message = "主键ID不能为空")
    private Long id;

    @ApiModelProperty(value = "分析项ID", required = true)
    @NotNull(message = "分析项ID不能为空")
    private Long parameterId;

    @ApiModelProperty(value = "分析项编码(冗余)")
    private String parameterCode;

    @ApiModelProperty(value = "方法编码", required = true)
    @NotBlank(message = "方法编码不能为空")
    private String code;

    @ApiModelProperty(value = "方法版本")
    private String version;

    @ApiModelProperty(value = "方法标准/描述")
    private String standard;

    @ApiModelProperty(value = "备注")
    private String remark;
}


