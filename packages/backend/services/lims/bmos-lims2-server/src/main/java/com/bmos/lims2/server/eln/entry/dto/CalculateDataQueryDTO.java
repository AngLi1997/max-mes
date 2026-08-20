package com.bmos.lims2.server.eln.entry.dto;

import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class CalculateDataQueryDTO {
    @Tolerate
    public CalculateDataQueryDTO() {
    }

    private String batchNo;

    private Long inspectionOrderId;


    /**
     * 方案id
     */
    @ApiModelProperty(value = "方案id",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long schemeId;

    /**
     * 工艺版本
     */
    @ApiModelProperty(value = "方案版id",required = true)
    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class})
    private Long schemeVersionId;

    /**
     * 记录项id
     */
    @ApiModelProperty(value = "记录项id",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long recordItemId;

    @ApiModelProperty(value = "方法id",required = true)
    private Long recordId;

    @ApiModelProperty(value = "任务id",required = true)
    private Long taskId;

    @ApiModelProperty(value = "检验项目Id")
    private Long itemId;

    @ApiModelProperty(value = "检验项目配置id")
    private Long itemConfigId;

    @ApiModelProperty(value = "检验分析项id")
    private Long parameterId;

    @ApiModelProperty(value = "检验分析项配置id")
    private Long parameterConfigId;

    /**
     * 记录项版本id
     */
    @ApiModelProperty(value = "方法版本id",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long recordVersionId;
}
