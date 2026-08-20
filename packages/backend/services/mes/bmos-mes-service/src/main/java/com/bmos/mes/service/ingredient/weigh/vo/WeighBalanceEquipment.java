package com.bmos.mes.service.ingredient.weigh.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 秤具信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:11
 */
@Data
@ApiModel("秤具信息")
public class WeighBalanceEquipment {

    /**
     * 秤具id
     */
    @ApiModelProperty(value = "秤具id", example = "1")
    private Long balanceId;

    /**
     * 工位id
     */
    @ApiModelProperty(value = "工位id")
    private List<Long> stationIdList;

    /**
     * 秤具名称
     */
    @ApiModelProperty(value = "秤具名称", example = "梅特勒电子秤")
    private String balanceName;

    /**
     * 秤具编号
     */
    @ApiModelProperty(value = "秤具编号", example = "ind-231-001")
    private String balanceCode;

    /**
     * 最大量程
     */
    @ApiModelProperty(value = "最大量程", example = "500")
    private BigDecimal maxRange;

    /**
     * 最小量程
     */
    @ApiModelProperty(value = "最小量程", example = "0.02")
    private BigDecimal minRange;

    /**
     * 精度
     */
    @ApiModelProperty(value = "精度", example = "0.02")
    private BigDecimal precision;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    /**
     * 是否已校准
     */
    @ApiModelProperty(value = "是否已校准", example = "true")
    private Boolean isCalibrated;

    /**
     * 校准有效期
     */
    @ApiModelProperty(value = "校准有效期", example = "2024-04-18")
    private LocalDate calibrateExpiredDate;

    /**
     * 通信地址
     */
    @ApiModelProperty(value = "通信地址", example = "ws://192.168.200.100:8000")
    private String websocketAddress;

    /**
     * 称具通信类型
     */
    @ApiModelProperty("称具通信类型")
    private String protocolType;

    /**
     * 是否空闲
     */
    @ApiModelProperty(value = "是否空闲", example = "true")
    private Boolean isIdle;
}
