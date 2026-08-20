package com.bmos.mes.service.weigh.centre2.ticket.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2023/5/19 19:13
 */
@Data
@ApiModel("编辑工单请求参数")
public class TicketEditDTO {

    @NotNull
    @ApiModelProperty(value = "工单ID", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value = "计划执行时间", example = "2023-05-20")
    private LocalDate planDate;

    @ApiModelProperty(value = "删除的需求ID列表", example = "[1, 2, 3]")
    private List<Long> deleteRequirementIds;

    @ApiModelProperty(value = "新增的需求ID列表", example = "[4, 5, 6]")
    private List<Long> addRequirementIds;
} 