package com.bmos.wms.service.businessLog.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.wms.common.enums.inventory.PositionInventoryOperateType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 货位日志
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bw_position_log")
public class PositionLog extends BaseDO {

    /**
     * 货品件号
     */
    private String inventoryNo;

    /**
     * 货品量
     */
    private BigDecimal quantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;


    /**
     * 操作类型
     */
    private PositionInventoryOperateType operateType;

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
     * 货位id
     */
    private Long positionId;

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
     * 备注
     */
    private String remark;
}
