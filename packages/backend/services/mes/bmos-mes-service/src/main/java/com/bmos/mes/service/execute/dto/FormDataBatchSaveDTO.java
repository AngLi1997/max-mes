package com.bmos.mes.service.execute.dto;

import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class FormDataBatchSaveDTO {
    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long productPlanId;

    /**
     * 批号
     */
    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class})
    @ApiModelProperty(value = "批号",required = true)
    private String batchNo;

    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long processId;

    /**
     * 工艺版本
     */
    @ApiModelProperty(value = "工艺版本号",required = true)
    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class})
    private String processVersion;

    /**
     * 记录项id
     */
    @ApiModelProperty(value = "记录项id",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    @ApiModelProperty(value = "记录项版本id",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long recordVersionId;


    /**
     * 历史工序步骤id
     */
    @ApiModelProperty(value = "工序步骤id",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long procedureStepId;

    /**
     * 是否复用
     */
    @ApiModelProperty(value = "是否复用",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Boolean reuse;

    /**
     * 复制版本（默认0）
     */
    @ApiModelProperty(value = "复制版本号",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long copyVersion;

    @ApiModelProperty(value = "工序换班次数",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Integer procedureChangeNumber;

    @ApiModelProperty(value = "工艺换班次数",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Integer processChangeNumber;

    @ApiModelProperty(value = "工步模型id",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long procedureStepModelId;

    @ApiModelProperty(value = "数据集",required = true)
    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class})
    @Valid
    private List<FormDataBatchSaveItemDTO> items;
}
