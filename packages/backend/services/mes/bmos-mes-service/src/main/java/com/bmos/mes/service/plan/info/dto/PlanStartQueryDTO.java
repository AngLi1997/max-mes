package com.bmos.mes.service.plan.info.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 查询正在执行中的生产计划入参
 */
@Getter
@Setter
@ApiModel("查询正在执行中的生产计划入参")
public class PlanStartQueryDTO {

    /**
     * 生产计划id
     */
    private Long productId;

}
