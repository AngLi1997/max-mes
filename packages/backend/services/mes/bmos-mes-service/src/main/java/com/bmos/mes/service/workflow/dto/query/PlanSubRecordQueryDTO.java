package com.bmos.mes.service.workflow.dto.query;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("辅助记录查询DTO")
public class PlanSubRecordQueryDTO extends BasePage {

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("步骤/任务名称")
    private String procedureStepName;

}
