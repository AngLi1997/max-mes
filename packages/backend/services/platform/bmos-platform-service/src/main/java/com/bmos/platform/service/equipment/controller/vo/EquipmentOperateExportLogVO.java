
package com.bmos.platform.service.equipment.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 设备操作日志返回参数
 */
@Getter
@Setter
@ApiModel("设备操作日志返回参数")
public class EquipmentOperateExportLogVO {

    /**
     * 设备名称
     */
    @ExcelProperty("设备名称")
    private String equipmentName;

    /**
     * 设备编码
     */
    @ExcelProperty("设备编码")
    private String equipmentCode;

    @ExcelProperty("操作内容")
    private String operateContent;


    @ExcelProperty("记录方式")
    private String changeType;

    /**
     * 生产批号
     */
    @ExcelProperty("生产批号")
    private String batchNo;

    /**
     * 产品名称
     */
    @ExcelProperty("产品名称")
    private String productName;

    /**
     * 使用开始时间
     */
    @ExcelProperty("使用开始时间")
    private LocalDateTime beginTime;

    /**
     * 使用结束时间
     */
    @ExcelProperty("使用结束时间")
    private LocalDateTime endTime;

    /**
     * 结束操作人
     */
    @ExcelProperty("操作人")
    private String endOperatorName;

    @ExcelProperty("复核人")
    private String reviewerName;

    @ExcelProperty("操作时间")
    private LocalDateTime createTime;
}

