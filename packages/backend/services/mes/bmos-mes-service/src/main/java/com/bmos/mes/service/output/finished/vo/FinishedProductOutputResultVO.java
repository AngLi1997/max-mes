package com.bmos.mes.service.output.finished.vo;

import com.bmos.mes.service.output.finished.model.FinishedProductOutputResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 成品产出结果vo
 * @author liang
 * @version 1.0.0
 * @date 2024/12/4 11:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FinishedProductOutputResultVO extends FinishedProductOutputResult {

    /**
     * 生产计划id
     */
    private Long productPlanId;
}
