package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className: CodeNameVP
 * @author: yigaohui
 * @date: 2025/2/19 14:39
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("code name VO")
public class CodeNameVO {
    @ApiModelProperty(value = "code")
    private String code;

    @ApiModelProperty(value = "name")
    private String name;
}
