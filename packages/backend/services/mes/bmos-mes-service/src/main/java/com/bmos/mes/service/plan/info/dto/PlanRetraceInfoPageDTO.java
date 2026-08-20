package com.bmos.mes.service.plan.info.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 批次追溯信息分页
 */
@ApiModel("批次追溯信息分页")
@Data
public class PlanRetraceInfoPageDTO extends BasePage {

    @NotNull
    @ApiModelProperty("批次id")
    private Long planId;

}
