package com.bmos.platform.service.factory.service.dto;

import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 设备工位启停
 */
@Getter
@Setter
@ApiModel("房间停启用入参")
public class RoomEnableDTO {

    /**
     * 工位id
     */
    @ApiModelProperty(value = "房间id", required = true)
    @NotNull
    private Long id;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用；0：未启用；1：启用")
    @ApiModelEnumProperty(value = "启停",enumClass = StatusEnum.class,required = true)
    @NotNull
    private Boolean enable;

}
