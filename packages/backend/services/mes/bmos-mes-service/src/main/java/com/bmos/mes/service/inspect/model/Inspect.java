package com.bmos.mes.service.inspect.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.inpspect.InspectStatusEnum;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 请验单(BmInspect)实体类
 *
 * @author makejava
 * @since 2025-02-17 18:24:28
 */
@TableName("bm_inspect")
@Getter
@Setter
public class Inspect extends BaseDO {
    /**
     * 请验单号
     */
    private String inspectNo;
    /**
     * 请验状态 1-检验中 2-已完成 3-已退回
     */
    private InspectStatusEnum status;
    /**
     * 汇总检验结果
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private MaterialQualityStatusEnum inspectResult;
    /**
     * 退回原因（请验单被lims退回后，所需要填写的退回原因）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String reason;
    /**
     * 请验人id
     */
    private String inspectorId;
    /**
     * 请验人用户名称-登陆名称
     */
    private String inspector;
    /**
     * 请验时间
     */
    private LocalDateTime inspectTime;
    /**
     * 工序模型id  (plan_id + procedure_model_id能够获取到当前工序所发起的请验单)
     */
    private Long procedureModelId;

    /**
     * 工步模型id
     */
    private Long procedureStepModelId;

    /**
     * 工艺换班次数
     */
    private Long processChangeNumber;

    /**
     * 工序换班次数
     */
    private Long procedureChangeNumber;

    /**
     * 请验单id (当前物料当时所绑定的请验单配置id)
     */
    private Long inspectConfigId;
    /**
     * 请验的配方物料id bm_product_formula_material表的主键id
     */
    private Long formulaMaterialId;
    /**
     * 物料id
     */
    private Long materialId;
    /**
     * 请验的物料类型 bm_product_formula_material表的material_type
     */
    private CategoryInfoTypeEnum materialType;
    /**
     * 请验的物料合并编码
     */
    private String materialMergeCode;
    /**
     * 请验的物料名称
     */
    private String materialName;
    /**
     * 请验的物料批次号
     */
    private String materialBatchNo;
    /**
     * 生产指令单id bm_product_plan表的主键id
     */
    private Long planId;
    /**
     * 产品名称  bm_prodcut_plan内的冗余内容
     */
    private String productName;
    /**
     * 产品合并编码 bm_prodcut_plan内的冗余内容
     */
    private String productMergeCode;
    /**
     * 指令单编号 bm_prodcut_plan内的冗余内容
     */
    private String planNo;
    /**
     * 生产批号 bm_prodcut_plan内的冗余内容
     */
    private String batchNo;
    /**
     * 生产批量 bm_prodcut_plan内的冗余内容
     */
    private BigDecimal batchQuantity;
    /**
     * 单位id bm_prodcut_plan内的冗余内容
     */
    private Long unitId;

    /**
     * 检验方案id（自研LIMS路径）
     */
    private Long schemeId;

    /**
     * 检验方案版本id（自研LIMS路径）
     */
    private Long schemeVersionId;
}

