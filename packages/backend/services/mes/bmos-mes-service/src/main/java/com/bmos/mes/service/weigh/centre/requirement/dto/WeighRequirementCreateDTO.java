package com.bmos.mes.service.weigh.centre.requirement.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 创建称量需求的组件参数
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 17:53
 */
@Data
public class WeighRequirementCreateDTO {

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

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
     * 组件实例id
     */
    private Long componentInstanceId;

    /**
     * 组件配置id
     */
    private Long procedureStepConfigId;
}
