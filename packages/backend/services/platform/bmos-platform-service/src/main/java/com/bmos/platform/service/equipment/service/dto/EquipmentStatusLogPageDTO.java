package com.bmos.platform.service.equipment.service.dto;

import com.bmos.mybatis.page.BasePage;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备状态日志入参
 */
@Getter
@Setter
@ApiModel("设备状态日志")
public class EquipmentStatusLogPageDTO extends BasePage {

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
     * 操作名称
     * {@link com.bmos.platform.facade.equipment.enums.EquipmentStatusOperateEnum}
     */
    @ApiModelProperty("状态名称")
    private String operateName;

    /**
     * 变更类型
     * {@link com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType}
     */
    @ApiModelProperty("变更类型")
    private String changeType;

    /**
     * 变更开始时间
     */
    @ApiModelProperty("变更开始时间")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private String operateBeginTime;

    /**
     * 变更结束时间
     */
    @ApiModelProperty("变更结束时间")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private String operateEndTime;

}
