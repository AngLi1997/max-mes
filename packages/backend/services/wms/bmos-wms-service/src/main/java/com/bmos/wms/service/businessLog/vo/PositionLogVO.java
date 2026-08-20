package com.bmos.wms.service.businessLog.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.wms.common.enums.inventory.PositionInventoryOperateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 货位日志
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:25
 */
@Data
@ApiModel("货位日志")
public class PositionLogVO {

    /**
     * 货品件号
     */
    @ApiModelProperty(value = "货品件号", example = "WH030102231001")
    private String inventoryNo;

    /**
     * 货品量
     */
    @ApiModelProperty(value = "货品量", example = "100")
    @PrecisionValue
    private BigDecimal quantity;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    @PrecisionUnitId
    private Long unitId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "g")
    private String unit;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间", example = "2024-04-07 18:16:00")
    private LocalDateTime operateTime;


    /**
     * 操作类型
     */
    @ApiModelEnumProperty(value = "操作类型", enumClass = PositionInventoryOperateType.class)
    @EnumValidate(PositionInventoryOperateType.class)
    private PositionInventoryOperateType operateType;

    /**
     * 操作信息
     */
    @ApiModelProperty(value = "操作信息", example = "货品出库-复核")
    private String operateInfo;

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id", example = "1")
    private String operatorId;

    /**
     * 操作人名称
     */
    @ApiModelProperty(value = "操作人名称", example = "张三")
    private String operatorName;

    /**
     * 货位名称
     */
    @ApiModelProperty(value = "货位名称", example = "不锈钢盆儿")
    private String position;

    /**
     * 货位编码
     */
    @ApiModelProperty(value = "货位编码", example = "KQ-PY-101")
    private String positionCode;

    /**
     * 所属位置
     */
    @ApiModelProperty(value = "所属位置", example = "狂犬病毒疫苗车间/生产区")
    private String positionPath;

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "苹果")
    private String cargoName;

    /**
     * 货品合并编码
     */
    @ApiModelProperty(value = "货品合并编码", example = "123456")
    private String mergeCode;

    /**
     * 货品批号
     */
    @ApiModelProperty(value = "货品批号", example = "1")
    private String inventoryBatchNo;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称", example = "人血白蛋白")
    private String productName;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码", example = "CX005")
    private String productMergeCode;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号", example = "CX0052402001")
    private String productBatchNo;

    /**
     * 工艺名称
     */
    @ApiModelProperty(value = "工艺名称", example = "人血白蛋白精制工艺")
    private String processName;

    /**
     * 领料单号
     */
    @ApiModelProperty(value = "领料单号", example = "WH030102231001")
    private String pullOrderNo;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;
}
