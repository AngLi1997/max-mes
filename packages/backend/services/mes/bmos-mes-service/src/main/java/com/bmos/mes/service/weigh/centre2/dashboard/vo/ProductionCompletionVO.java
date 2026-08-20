package com.bmos.mes.service.weigh.centre2.dashboard.vo;

import com.bmos.mes.service.weigh.centre2.dashboard.enums.DashboardWeighStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 生产批次配料完成情况VO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/27 17:45
 */
@Data
@ApiModel("生产批次配料完成情况VO")
public class ProductionCompletionVO {

    private Long id;
    
    @ApiModelProperty(value = "生产批号", example = "250527001")
    private String batchNo;
    
    @ApiModelProperty(value = "产品名称", example = "氯化钠")
    private String productName;
    
    @ApiModelProperty(value = "产品编码", example = "WH030101")
    private String productMergeCode;
    
    @ApiModelProperty(value = "称量中心", example = "C3-3车间称量中心")
    private String weighCentreName;
    
    @ApiModelProperty(value = "计划生产时间", example = "2025-05-24")
    private LocalDate planProductionDate;
    
    @ApiModelProperty(value = "需求完成率", example = "20%")
    private String completionRate;
    
    @ApiModelEnumProperty(value = "状态", enumClass = DashboardWeighStatusEnum.class)
    private DashboardWeighStatusEnum status;
} 