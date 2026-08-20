package com.bmos.lims2.server.inspect.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 实验包下检验项目
 */
@Getter
@Setter
@ApiModel("实验包下检验项目DTO")
public class InspectPackageItemDTO {

    @ApiModelProperty(value = "检验项id", required = true)
    @NotNull
    private Long inspectItemId;

    @ApiModelProperty(value = "检验项目编码")
    private String  code;

    @ApiModelProperty(value = "检验项目名称")
    private String name;

}
