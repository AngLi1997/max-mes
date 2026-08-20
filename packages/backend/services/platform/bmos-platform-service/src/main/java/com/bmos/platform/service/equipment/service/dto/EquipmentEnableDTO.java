package com.bmos.platform.service.equipment.service.dto;

import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 设备启停
 */
@Getter
@Setter
@ApiModel("设备启停入参")
public class EquipmentEnableDTO {

    /**
     * 工位id
     */
    @ApiModelProperty(value = "设备id", required = true)
    @NotNull
    private Long id;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用", required = true)
    @ApiModelEnumProperty(value = "启停",enumClass = StatusEnum.class)
    @NotNull
    private Boolean enable;

}
