package com.bmos.mes.service.execute.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("生产计划修订查询DTO")
public class PlanFieldModifyQueryDTO extends BasePage {

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("工序节点名称")
    private String procedureName;

    @ApiModelProperty("步骤任务名称")
    private String procedureStepName;


}
