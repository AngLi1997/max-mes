package com.bmos.mes.service.inspect.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("请验单分页DTO")
public class InspectPageDTO extends BasePage {

    /**
     * 工序模型id
     */
    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @NotNull
    @ApiModelProperty("生产计划id")
    private Long planId;

    /**
     * 工步模型id
     */
    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    /**
     * 工艺换班次数
     */
    @ApiModelProperty("工艺换班次数")
    private Long processChangeNum;

    /**
     * 工序换班次数
     */
    @ApiModelProperty("工序换班次数")
    private Long procedureChangeNum;

}
