package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel("设备使用日志填报DTO")
public class EquipmentOperateLogFillDTO {

    @ApiModelProperty("日志id,没有则不传")
    private Long id;

    /**
     * 设备id，关联到bp_equipment_info表中的id
     */
    @ApiModelProperty(value = "设备id，关联到bp_equipment_info表中的id", required = true)
    @NotNull
    private Long equipmentId;
    /**
     * 设备编码
     */
    @ApiModelProperty(value = "设备编码", required = true)
    @NotBlank
    private String code;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称", required = true)
    @NotBlank
    private String equipmentName;
    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String batchNo;

    /**
     * 变更类型
     */
    @ApiModelProperty("变更类型")
    private String changeType;
    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 使用开始时间
     */
    @ApiModelProperty("使用开始时间")
    private LocalDateTime beginTime;

    /**
     * 使用结束时间
     */
    @ApiModelProperty("使用结束时间")
    private LocalDateTime endTime;


    @ApiModelProperty("操作内容")
    private String operateContent;

    @ApiModelProperty("模板id")
    private Long templateId;

}
