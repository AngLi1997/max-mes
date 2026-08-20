package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("业务组件数据处理基础DTO")
@Data
public class BusinessDataHandleBaseDTO {

    @ApiModelProperty("组件id")
    @NotNull
    private Long componentId;

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long productPlanId;

    /**
     * 批号
     */
    @NotEmpty
    @ApiModelProperty(value = "批号",required = true)
    private String batchNo;

    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long processId;

    /**
     * 工艺版本
     */
    @ApiModelProperty(value = "工艺版本号",required = true)
    @NotEmpty
    private String processVersion;

    /**
     * 记录项id
     */
    @ApiModelProperty(value = "记录项id",required = true)
    @NotNull
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    @ApiModelProperty(value = "记录项版本id",required = true)
    @NotNull
    private Long recordVersionId;

    /**
     * 历史工序步骤id
     */
    @ApiModelProperty(value = "工序步骤id",required = true)
    @NotNull
    private Long procedureStepId;

    @ApiModelProperty(value = "工序步骤模型id", required = true)
    @NotNull
    private Long procedureStepModelId;

    /**
     * 是否复用
     */
    @ApiModelProperty(value = "是否复用",required = true)
    private Boolean reuse;

    /**
     * 复制版本（默认0）
     */
    @ApiModelProperty(value = "复制版本号",required = true)
    @NotNull
    private Long copyVersion;


}
