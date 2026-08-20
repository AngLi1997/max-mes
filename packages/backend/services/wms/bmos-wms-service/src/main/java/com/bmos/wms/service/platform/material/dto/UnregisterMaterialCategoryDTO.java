package com.bmos.wms.service.platform.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("物料分类取消注册DTO")
@AllArgsConstructor
public class UnregisterMaterialCategoryDTO {

    @ApiModelProperty("平台名称")
    @NotBlank
    private String platformName;

    @ApiModelProperty("子业务码")
    @NotNull
    private Integer childCode;

    @ApiModelProperty("物料分类id")
    @NotNull
    private Long categoryId;

}
