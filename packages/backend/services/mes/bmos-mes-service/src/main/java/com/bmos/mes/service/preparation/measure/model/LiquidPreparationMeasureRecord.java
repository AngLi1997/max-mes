package com.bmos.mes.service.preparation.measure.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.preparation.MeasureModeEnum;
import com.bmos.mes.common.enums.preparation.MeasureTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配液量取记录
 */
@Data
@TableName("bm_liquid_preparation_measure_record")
public class LiquidPreparationMeasureRecord extends BaseDO {

    /**
     * 量取组件实例id
     */
    private Long measureInstanceId;

    /**
     * 配液计划id
     */
    private Long liquidPreparationPlanId;

    /**
     * 量取批次id
     */
    private Long measureBatchId;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 物料编码
     */
    private String materialMergeCode;

    /**
     * 量取量 根据配方修约后的值
     */
    private BigDecimal quantity;

    /**
     * 配方单位id
     */
    private Long unitId;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 物料件号
     */
    private String storageMaterialNo;

    /**
     * 物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 物料批次编码
     */
    private String storageMaterialBatchNo;

    /**
     * 货位id
     */
    private Long materialPositionId;

    /**
     * 货位 货位编码-货位名称
     */
    private String materialPosition;

    /**
     * 容器id
     */
    private Long containerId;

    /**
     * 容器名称
     */
    private String containerName;

    /**
     * 签名状态
     */
    private WeighSignStatus signStatus;

    /**
     * 量取人id
     */
    private String measurerId;

    /**
     * 复核人id
     */
    private String reCheckerId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 量取时间
     */
    private LocalDateTime measureTime;

    /**
     * 量取类型 配液量取、余液量取
     */
    private MeasureTypeEnum measureType;

    /**
     * 量取模式 设备量取、手动量取
     */
    private MeasureModeEnum measureMode;


}
