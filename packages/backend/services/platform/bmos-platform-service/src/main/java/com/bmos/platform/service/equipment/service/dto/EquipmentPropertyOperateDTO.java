package com.bmos.platform.service.equipment.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备状态变更入参
 */
@Getter
@Setter
@ApiModel("设备状态变更入参")
public class EquipmentPropertyOperateDTO {

    @ApiModelProperty(value = "设备状态id", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "是否完成", required = true)
    @NotNull
    private Boolean finishStatus;

    @ApiModelProperty(value = "有效期", notes = "当finished为true时必传有效期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireDateTime;

}
