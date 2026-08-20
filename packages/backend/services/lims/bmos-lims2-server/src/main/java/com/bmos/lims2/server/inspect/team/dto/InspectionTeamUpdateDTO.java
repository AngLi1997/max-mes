package com.bmos.lims2.server.inspect.team.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 检验班组更新DTO
 * @Author: yigaohui
 * @Date: 2025/07/22 11:26
 */
@Data
@ApiModel("更新班组DTO")
public class InspectionTeamUpdateDTO {

    @ApiModelProperty(value = "ID", required = true)
    @NotNull(message = "ID不能为空")
    private Long id;

    @ApiModelProperty(value = "班组名称", required = true)
    @NotBlank(message = "班组名称不能为空")
    private String name;

    @ApiModelProperty(value = "班组编码", required = true)
    @NotBlank(message = "班组编码不能为空")
    private String code;

    @ApiModelProperty("班组描述")
    private String description;
} 