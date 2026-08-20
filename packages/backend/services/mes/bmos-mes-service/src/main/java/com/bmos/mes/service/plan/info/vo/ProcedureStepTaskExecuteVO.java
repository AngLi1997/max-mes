package com.bmos.mes.service.plan.info.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("工步任务执行列表VO")
@Data
public class ProcedureStepTaskExecuteVO {

    @ApiModelProperty("工艺班次")
    private Integer processChangeNumber;

    @ApiModelProperty("工序班次")
    private Integer procedureChangeNumber;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("工步id")
    private Long procedureStepId;

    @ApiModelProperty("工序排序")
    private Integer procedureSort;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工序id")
    private Long procedureId;

    @ApiModelProperty("工步排序")
    private Integer procedureStepSort;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("记录版本id")
    private Long recordVersionId;

    @ApiModelProperty("是否复用")
    private Boolean reuse;

    public Integer getProcedureChangeNumber() {
        return procedureChangeNumber == null ? 0 : procedureChangeNumber + 1;
    }

    public Integer getProcessChangeNumber() {
        return processChangeNumber == null ? 0 : processChangeNumber + 1;
    }

}
