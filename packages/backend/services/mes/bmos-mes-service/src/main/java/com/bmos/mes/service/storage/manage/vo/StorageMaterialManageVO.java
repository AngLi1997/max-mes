package com.bmos.mes.service.storage.manage.vo;

import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/6 11:58
 */
@Data
@ApiModel("暂存物料批次信息")
public class StorageMaterialManageVO {

    /**
     * 物料件id
     */
    @ApiModelProperty(value = "物料件id", example = "1")
    private Long storageMaterialId;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "1")
    private String storageMaterialNo;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "B01-01")
    private String mergeCode;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001")
    private String materialBatchNo;

    /**
     * 是否用尽
     */
    @ApiModelProperty(value = "是否用尽", example = "true")
    private Boolean useUp;

    /**
     * 初始量
     */
    @ApiModelProperty(value = "初始量", example = "1.000")
    @PrecisionValue
    private BigDecimal initQuantity;

    /**
     * 可用量
     */
    @ApiModelProperty(value = "可用量", example = "1.000")
    @PrecisionValue
    private BigDecimal availableQuantity;

    /**
     * 预订量
     */
    @ApiModelProperty(value = "预订量", example = "1.000")
    @PrecisionValue
    private BigDecimal reserveQuantity;

    /**
     * 消耗量
     */
    @ApiModelProperty(value = "消耗量", example = "1.000")
    @PrecisionValue
    private BigDecimal consumeQuantity;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    @PrecisionUnitId
    private Long unitId;

    /**
     * 单位信息
     */
    @ApiModelProperty(value = "单位信息", example = "g")
    private String unit;

    /**
     * 容器名称
     */
    @ApiModelProperty(value = "容器名称", example = "不锈钢盆儿")
    private String containerName;

    /**
     * 货位全称
     */
    @ApiModelProperty(value = "货位全称", example = "KQ101-培养室-盐酸组氨酸货位")
    private String position;

    /**
     * 预定产品名称
     */
    @ApiModelProperty(value = "预定产品名称", example = "盐酸组氨酸")
    private String reserveProductName;

    /**
     * 预定批次号
     */
    @ApiModelProperty(value = "预定批次号", example = "WH030102231001")
    private String reserveBatchNo;

    /**
     * 预定人员id
     */
    @ApiModelProperty(value = "预定人员id", example = "1")
    private String reserveUserId;

    /**
     * 预定人员
     */
    @ApiModelProperty(value = "预定人员", example = "张三")
    private String reserveUserName;

    /**
     * 预定时间
     */
    @ApiModelProperty(value = "预定时间", example = "2024-02-06 11:58:00")
    private LocalDateTime reserveTime;
}
