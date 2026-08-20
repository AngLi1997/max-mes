package com.bmos.mes.service.weigh.centre.execute.model;

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
 * 称量执行称量记录
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 18:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_weigh_execute_weigh_record")
public class WeighExecuteWeighRecord extends BaseDO {

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 需求id
     */
    private Long requirementId;

    /**
     * 生产计划id
     */
    private Long productPlanId;

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
     * 称量产出物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 称量产出物料批次号
     */
    private String storageMaterialBatchNo;

    /**
     * 称量产出物料件id
     */
    private Long storageMaterialId;

    /**
     * 称量产出物料件编号
     */
    private String storageMaterialNo;

    /**
     * 称量方式
     */
    private WeighType weighType;

    /**
     * 称量模式
     */
    private WeighMode weighMode;

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

    /**
     * 容器名称
     */
    private String containerName;

    /**
     * 货位名称
     */
    private String materialPositionName;
}
