package com.bmos.wms.service.businessLog.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.wms.common.enums.inventory.CargoInventoryOperateType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 货品日志
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bw_cargo_log")
public class CargoLog extends BaseDO {

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 操作类型
     */
    private CargoInventoryOperateType operateType;

    /**
     * 操作信息
     */
    private String operateInfo;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 货品id
     */
    private Long cargoId;

    /**
     * 货品名称
     */
    private String cargoName;

    /**
     * 货品合并编码
     */
    private String mergeCode;

    /**
     * 货品批号
     */
    private String inventoryBatchNo;

    /**
     * 货品件编号
     */
    private String inventoryNo;

    /**
     * 预定量
     */
    private BigDecimal reserveQuantity;

    /**
     * 可用量
     */
    private BigDecimal availableQuantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 是否可用
     */
    private Boolean available;

    /**
     * 有效期
     */
    private LocalDate effectiveDate;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productMergeCode;

    /**
     * 生产批号
     */
    private String productBatchNo;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 领料单号
     */
    private String pullOrderNo;

    /**
     * 货位名称
     */
    private String position;

    /**
     * 货位编码
     */
    private String positionCode;

    /**
     * 所属位置
     */
    private String positionPath;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 生产商
     */
    private String producer;

    /**
     * 原厂批号
     */
    private String factoryBatchNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 请验单号
     */
    private String validateOrderNo;

    /**
     * 报告单编号
     */
    private String reportOrderNo;

    /**
     * 放行单编号
     */
    private String licenseOrderNo;

    /**
     * 检验信息
     */
    private String checkInfo;
}
