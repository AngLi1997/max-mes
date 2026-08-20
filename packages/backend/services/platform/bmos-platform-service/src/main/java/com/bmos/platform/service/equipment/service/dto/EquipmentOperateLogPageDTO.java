package com.bmos.platform.service.equipment.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志分页查询入参
 */
@Getter
@Setter
@ApiModel("操作日志分页查询入参")
public class EquipmentOperateLogPageDTO extends BasePage {

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;

    @ApiModelProperty("设备编码")
    private String equipmentCode;

    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String batchNo;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 变更类型
     */
    @ApiModelProperty("变更类型")
    private String changeType;

    /**
     * 操作开始时间
     */
    @ApiModelProperty("操作开始时间")
    private String operateBeginTime;

    /**
     * 操作结束时间
     */
    @ApiModelProperty("操作结束时间")
    private String operateEndTime;

}
