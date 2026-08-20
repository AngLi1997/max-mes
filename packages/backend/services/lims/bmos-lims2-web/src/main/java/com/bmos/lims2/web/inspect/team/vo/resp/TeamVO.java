package com.bmos.lims2.web.inspect.team.vo.resp;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @className: TeamVO
 * @author: yigaohui
 * @date: 2025/8/6 16:18
 * @Version: 1.0
 * @description:
 */

public class TeamVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "编码", required = true)
    @NotBlank
    private String code;
}
