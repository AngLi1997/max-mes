package com.bmos.mes.service.output.weigh.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 17:20
 */
@Data
@ApiModel("校验产出称量组件物料件签名")
public class OutputWeighValidateSignDTO {

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 工序步骤模型id
     */
    private Long procedureStepModelId;
}
