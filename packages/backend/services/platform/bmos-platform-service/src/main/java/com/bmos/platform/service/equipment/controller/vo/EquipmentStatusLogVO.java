package com.bmos.platform.service.equipment.controller.vo;

import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusLogEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusOperateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备状态VO
 */
@Getter
@Setter
@ApiModel("设备状态VO")
public class EquipmentStatusLogVO {

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;

    /**
     * 设备编码
     */
    @ApiModelProperty("设备编码")
    private String equipmentCode;

    /**
     * 设备地点
     */
    @ApiModelProperty("设备地点")
    private String position;

    /**
     * 状态名称
     */
    @ApiModelProperty("状态名称")
    private EquipmentStatusOperateEnum operateName;

    /**
     * 变更类型
     */
    @ApiModelProperty("变更类型")
    private EquipmentStatusLogChangeType changeType;

    /**
     * 变更前的状态名称
     */
    @ApiModelProperty("变更前的状态名称")
    private EquipmentStatusLogEnum preStatusName;

    /**
     * 变更后的状态
     */
    @ApiModelProperty("变更后的状态")
    private EquipmentStatusLogEnum statusName;

    /**
     * 过期时间
     */
    @ApiModelProperty("过期时间")
    private LocalDate expireDateTime;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private LocalDateTime operateTime;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String operatorName;

}
