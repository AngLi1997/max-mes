package com.bmos.platform.facade.equipment.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备状态
 */
@Getter
@Setter
public class EquipmentStatusFeignVO {

    /**
     * 设备状态编码
     */
    @ApiModelProperty("设备状态编码")
    private String code;

    /**
     * 有效期
     */
    @ApiModelProperty("有效期")
    private LocalDateTime expireDateTime;

    /**
     * 当前设备状态是否完成
     * true：已完成 false：未完成
     */
    @ApiModelProperty("当前设备状态是否完成")
    private Boolean finishStatus;

}
