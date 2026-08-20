package com.bmos.mes.service.weigh.centre2.requirement.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工单需求组分页查询参数
 * @author liang
 * @version 1.0.0
 * @date 2025/5/20 10:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("工单需求组分页查询参数")
public class TicketRequirementGroupPageDTO extends BasePage {
    
    @ApiModelProperty(value = "批次号", example = "B20240520")
    private String batchNo;
    
    @ApiModelProperty(value = "物料名称", example = "阿莫西林")
    private String materialName;
    
    @ApiModelProperty(value = "合并编码", example = "M2024050001")
    private String mergeCode;
    
    @ApiModelProperty(value = "BOM名称", example = "阿莫西林标准配方")
    private String bomName;
} 