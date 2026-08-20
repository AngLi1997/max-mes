package com.bmos.mes.service.weigh.centre2.requirement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 创建称量工单需求DTO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 20:15
 */
@Data
@ApiModel(value = "创建称量需求组DTO")
public class TicketRequirementGroupDTO {

    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "产品id不能为空")
    private Long productId;

    @ApiModelProperty(value = "生产BOM版本id", required = true)
    @NotNull(message = "生产BOM版本id不能为空")
    private Long bomVersionId;

    @ApiModelProperty(value = "生产批号", required = true)
    @NotEmpty(message = "生产批号不能为空")
    private String batchNo;

    @ApiModelProperty(value = "称量中心", required = true)
    @NotNull(message = "称量中心不能为空")
    private Long centreWeighId;

    @ApiModelProperty(value = "计划生产时间", required = true)
    @NotNull(message = "计划生产时间不能为空")
    private LocalDate planDate;

    @ApiModelProperty(value = "备注")
    private String remark;
} 