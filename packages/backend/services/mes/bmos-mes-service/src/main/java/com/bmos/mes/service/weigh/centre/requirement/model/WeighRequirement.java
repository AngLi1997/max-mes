package com.bmos.mes.service.weigh.centre.requirement.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighProcess;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 称量需求
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 17:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_weigh_requirement")
public class WeighRequirement extends BaseDO {

    /**
     * 组件配置id
     */
    private Long procedureStepConfigId;

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 称量中心id
     */
    private Long weighCentreId;

    /**
     * 需求日期
     */
    private LocalDate requirementDate;

    /**
     * 失效日期
     */
    private LocalDate expiredDate;

    /**
     * 需求量
     */
    private BigDecimal requirementQuantity;

    /**
     * 生产批次id
     */
    private Long productPlanId;

    /**
     * 生产批次号
     */
    private String batchNo;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品合并码
     */
    private String productMergeCode;

    /**
     * 需求状态
     */
    private RequirementStatusEnum requirementStatus;

    /**
     * 规划称量任务id
     */
    private Long weighRequirementTaskId;

    /**
     * 规划时间
     */
    private LocalDateTime programTime;

    // 称量执行相关

    /**
     * 称量状态
     */
    private RequirementWeighStatusEnum weighStatus;

    /**
     * 添加物料的批次id
     */
    private Long storageMaterialBatchId;

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
     * 称量阶段
     */
    private RequirementWeighProcess weighProcess;

    /**
     * 组件实例id
     */
    private Long businessComponentInstanceId;

    /**
     * 流程是否处于进行中（签完名才算彻底结束）
     * @return
     */
    public Boolean isProcessing() {
        return !Objects.equals(weighStatus, RequirementWeighStatusEnum.FINISHED_SIGN) && !Objects.equals(requirementStatus, RequirementStatusEnum.EXPIRED);
    }
}
