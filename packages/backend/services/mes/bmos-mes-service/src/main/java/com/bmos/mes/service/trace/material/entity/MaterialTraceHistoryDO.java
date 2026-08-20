package com.bmos.mes.service.trace.material.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.service.trace.material.enums.MaterialTraceOperateType;
import com.bmos.mes.service.trace.material.enums.MaterialTraceType;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物料追溯历史记录表
 * 范围：消耗：生产投料、配料投入、配液投入、物料投入
 *      产出：中间品产出、配液产出
 * @author liang
 * @version 1.0.0
 * @date 2024/11/21 10:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_material_trace_history")
public class MaterialTraceHistoryDO extends BaseDO {

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料分类id
     */
    private Long materialCategoryId;

    /**
     * 物料分类名称
     */
    private String materialCategoryName;

    /**
     * 物料分类类型
     */
    private CategoryInfoTypeEnum materialCategoryType;

    /**
     * 合并编码
     */
    private String mergeCode;

    /**
     * 物料规格
     */
    private String materialSpecification;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 物料件号
     */
    private String storageMaterialNo;

    /**
     * 物料件批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 物料件批次号
     */
    private String storageMaterialBatchNo;

    /**
     * 物料量
     */
    private BigDecimal quantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺版本号
     */
    private String processVersion;

    /**
     * 工序id
     */
    private Long procedureId;

    /**
     * 工序步骤id
     */
    private Long procedureStepId;

    /**
     * 操作类型
     */
    private MaterialTraceOperateType operateType;

    /**
     * 操作人id
     */
    private String operateUserId;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 物料追溯类型
     */
    private MaterialTraceType traceType;

    /**
     * 来源生产id
     */
    private Long sourceProductPlanId;

    /**
     * 来源批次号
     */
    private String sourceBatchNo;

    /**
     * 工序名称
     */
    @TableField(exist = false)
    private String procedureName;

    /**
     * 工序步骤名称
     */
    @TableField(exist = false)
    private String procedureStepName;

    /**
     * 工艺名称
     */
    @TableField(exist = false)
    private String processName;
}
