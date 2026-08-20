package com.bmos.mes.service.weigh.centre2.requirement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 配料信息物料查询请求DTO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:16
 */
@Data
@ApiModel(value = "配料信息物料查询请求DTO")
public class TicketRequirementQueryDTO {

    @ApiModelProperty(value = "配方物料ID", required = true)
    @NotNull
    private Long formulaMaterialId;
} 