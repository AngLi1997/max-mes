package com.bmos.mes.service.plan.info.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 生产计划信息(简易)
 * @author liang
 * @version 1.0.0
 * @date 2024/7/12 18:01
 */
@Data
@ApiModel("生产计划信息(简易)")
public class PlanSimpleVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("生产批号")
    private String batchNo;
}
