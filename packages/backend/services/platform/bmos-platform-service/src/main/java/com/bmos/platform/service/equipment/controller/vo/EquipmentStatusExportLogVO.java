package com.bmos.platform.service.equipment.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.bmos.common.convert.ExcelEnumConvert;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusLogEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusOperateEnum;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备状态VO
 */
@Getter
@Setter
@ApiModel("设备状态日志返回参数")
public class EquipmentStatusExportLogVO {

    @ExcelProperty("设备名称")
    private String equipmentName;

    /**
     * 设备编码
     */
    @ExcelProperty("设备编码")
    private String equipmentCode;

    /**
     * 状态名称
     */
    @ExcelProperty(value = "状态名称", converter = ExcelEnumConvert.class)
    private EquipmentStatusOperateEnum operateName;

    /**
     * 变更类型
     */
    @ExcelProperty(value = "变更类型",converter = ExcelEnumConvert.class)
    private EquipmentStatusLogChangeType changeType;

    /**
     * 变更前的状态名称
     */
    @ExcelProperty(value = "变更前状态", converter = ExcelEnumConvert.class)
    private EquipmentStatusLogEnum preStatusName;

    /**
     * 变更后的状态
     */
    @ExcelProperty(value = "变更后状态", converter = ExcelEnumConvert.class)
    private EquipmentStatusLogEnum statusName;

    /**
     * 过期时间
     */
    @ExcelProperty("状态效期")
    private LocalDateTime expireDateTime;

    /**
     * 操作时间
     */
    @ExcelProperty(value = "变更时间")
    private LocalDateTime operateTime;

    /**
     * 操作人
     */
    @ExcelProperty("操作人")
    private String operatorName;

}
