package com.bmos.mes.service.weigh.centre2.requirement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/5/21 09:42
 */
@Data
@ApiModel(value = "工单需求组信息查询参数")
public class TicketRequirementInfoQuery {

    @ApiModelProperty(value = "BOM版本ID")
    private Long bomVersionId;

    @ApiModelProperty(value = "需求组ID")
    private Long id;
}
