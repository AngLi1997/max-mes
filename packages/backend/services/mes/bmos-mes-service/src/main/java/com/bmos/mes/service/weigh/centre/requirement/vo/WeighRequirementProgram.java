package com.bmos.mes.service.weigh.centre.requirement.vo;

import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import lombok.Data;

/**
 * 称量需求规划
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 17:34
 */
@Data
public class WeighRequirementProgram {

    /**
     * 称量需求id
     */
    private Long id;

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 配方单位id
     */
    private Long formulaUnitId;

    /**
     * 称量中心id
     */
    private Long weighCentreId;

    /**
     * 需求日期
     */
    private String requirementDate;

    /**
     * 需求量
     */
    private String requirementQuantity;

    /**
     * 生产批次id
     */
    private Long productPlanId;

    /**
     * 规划状态
     */
    private RequirementStatusEnum requirementStatus;
}
