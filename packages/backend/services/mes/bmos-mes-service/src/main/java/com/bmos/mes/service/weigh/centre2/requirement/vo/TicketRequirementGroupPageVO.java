package com.bmos.mes.service.weigh.centre2.requirement.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 工单需求组分页查询结果
 * @author liang
 * @version 1.0.0
 * @date 2025/5/20 10:10
 */
@Data
@ApiModel("工单需求组分页查询结果")
public class TicketRequirementGroupPageVO {
    
    @ApiModelProperty(value = "ID", example = "1001")
    private Long id;
    
    @ApiModelProperty(value = "物料名称", example = "阿莫西林")
    private String materialName;
    
    @ApiModelProperty(value = "合并编码", example = "M2024050001")
    private String mergeCode;
    
    @ApiModelProperty(value = "批次号", example = "B20240520")
    private String batchNo;
    
    @ApiModelProperty(value = "BOM名称", example = "阿莫西林标准配方")
    private String bomName;
    
    @ApiModelProperty(value = "计划日期", example = "2024-05-20")
    private LocalDate planDate;
    
    @ApiModelProperty(value = "称量中心名称", example = "A区称量中心")
    private String weighCentreName;
    
    @ApiModelProperty(value = "发布状态", example = "1")
    private Integer releaseStatus;

    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;
} 