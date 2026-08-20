package com.bmos.mes.service.storage.log.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 货位日志
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 14:23
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_storage_material_position_log")
@Data
public class StorageMaterialPositionLog extends BaseDO {

    /**
     * 暂存间id
     */
    @ApiModelProperty(value = "暂存间id", example = "1")
    private Long storageId;

    /**
     * 暂存货位id
     */
    @ApiModelProperty(value = "暂存货位id", example = "1")
    private Long materialPositionId;


    /**
     * 暂存货位
     */
    @ApiModelProperty(value = "暂存货位", example = "培养室-盐酸组氨酸货位")
    private String materialPositionName;

    /**
     * 货位编码
     */
    @ApiModelProperty(value = "货位编码", example = "KQ-PY-101")
    private String materialPositionCode;

    /**
     * 所属位置
     */
    @ApiModelProperty(value = "所属位置", example = "狂犬病毒疫苗车间/生产区")
    private String materialPositionPath;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "盐酸组氨酸")
    private String materialName;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", example = "WH030102231001")
    private String materialCode;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001")
    private String materialBatchNo;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间", example = "入库")
    private LocalDateTime operateTime;

    /**
     * 操作类型
     */
    @ApiModelEnumProperty(value = "操作类型", enumClass = StorageOperateTypeEnum.class)
    @EnumValidate(value = StorageOperateTypeEnum.class)
    private StorageOperateTypeEnum operationType;

    /**
     * 具体操作
     */
    @ApiModelProperty(value = "具体操作", example = "物料出库-领用")
    private String operateDetail;

    /**
     * 操作人员
     */
    @ApiModelProperty(value = "操作人员", example = "张三")
    private String operatorName;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "000000002")
    private String materialNo;


    /**
     * 物料量
     */
    @ApiModelProperty(value = "物料量", example = "1.000")
    private BigDecimal quantity;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称", example = "人血白蛋白")
    private String productName;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码", example = "CX005")
    private String productCode;

    /**
     * 产品批号
     */
    @ApiModelProperty(value = "产品批号", example = "CX0052402001")
    private String productBatchNo;

    /**
     * 备注/来源去向说明
     */
    @ApiModelProperty(value = "备注/来源去向说明", example = "从A到B")
    private String remark;
}
