package com.bmos.mes.service.plan.info.vo;

import com.bmos.mes.common.enums.plan.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("PlanPageVO:生产前确定")
public class PlanStartVO {

    @ApiModelProperty("当前生产前确定数据")
    private List<PlanPageVO> presentPlanStartVo;

    @ApiModelProperty("计划生产前确定数据")
    private List<PlanPageVO> futurePlanStartVo;

}
