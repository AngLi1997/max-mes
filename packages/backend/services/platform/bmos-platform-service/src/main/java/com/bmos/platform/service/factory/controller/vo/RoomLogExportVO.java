package com.bmos.platform.service.factory.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.bmos.common.convert.ExcelEnumConvert;
import com.bmos.platform.facade.factory.enums.RoomStatusOperateTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 房间清场日志分页返回参数
 */
@Getter
@Setter
public class RoomLogExportVO {


    /**
     * 房间编码
     */
    @ExcelProperty("房间编码")
    private String roomCode;

    /**
     * 房间名称
     */
    @ExcelProperty("房间名称")
    private String roomName;

    /**
     * 清洁类型
     */
    @ExcelProperty(value = "清场类型", converter = ExcelEnumConvert.class)
    private RoomStatusOperateTypeEnum type;

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
     *工序名称
     */
    @ExcelProperty("清场工序")
    private String procedureName;

    /**
     * 开始时间
     */
    @ExcelProperty("清场开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ExcelProperty("清场结束时间")
    private String endTime;

    /**
     * 过期时间
     */
    @ExcelProperty("清场有效期至")
    private String expireTime;

    /**
     * 清场人
     */
    @ExcelProperty("清场人")
    private String operator;

    /**
     * 复合人
     */
    @ExcelProperty("复核人")
    private String verifier;

    /**
     * 描述
     */
    @ExcelProperty("备注")
    private String description;
}
