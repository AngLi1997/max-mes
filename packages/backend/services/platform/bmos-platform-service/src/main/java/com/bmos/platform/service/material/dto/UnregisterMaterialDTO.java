package com.bmos.platform.service.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("物料取消注册DTO")
@Getter
@Setter
public class UnregisterMaterialDTO {

    @ApiModelProperty("平台名称")
    @NotBlank
    private String platformName;

    @ApiModelProperty("子业务码")
    @NotNull
    private Integer childCode;

    @ApiModelProperty("物料id")
    @NotNull
    private Long materialId;


}
