package com.bmos.mes.service.ingredient.weigh.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.ingredient.WeighMode;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配料称重记录
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 19:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_ingredient_weigh_record")
public class IngredientWeighRecord extends BaseDO {

    /**
     * 生产计划id
     */
    private Long ingredientWeighBatchProcessId;

    /**
     * 配料单id
     */
    private Long ingredientPlanId;

    /**
     * 暂存物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 皮重
     */
    private BigDecimal tareWeight;

    /**
     * 毛重
     */
    private BigDecimal grossWeight;

    /**
     * 净重
     */
    private BigDecimal netWeight;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 容器id
     */
    private Long containerId;

    /**
     * 容器名称
     */
    private String containerName;

    /**
     * 货位id
     */
    private Long materialPositionId;

    /**
     * 称量方式
     */
    private WeighType weighType;

    /**
     * 称量模式
     */
    private WeighMode weighMode;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 签名状态
     */
    private WeighSignStatus signStatus;

    /**
     * 称量人id
     */
    private String weigherId;

    /**
     * 复核人id
     */
    private String reCheckerId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 称量时间
     */
    private LocalDateTime weighTime;
}
