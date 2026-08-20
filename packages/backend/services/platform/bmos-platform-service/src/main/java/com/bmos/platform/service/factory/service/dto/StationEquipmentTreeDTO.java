package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@ApiModel("工位设备树dto")
public class StationEquipmentTreeDTO {

    @ApiModelProperty("分类id")
    @NotNull
    private Long categoryId;

    @ApiModelProperty("设备名称")
    private String name;
}
