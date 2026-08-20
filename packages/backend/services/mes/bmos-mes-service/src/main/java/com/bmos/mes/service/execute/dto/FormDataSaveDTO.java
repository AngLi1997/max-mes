package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class FormDataSaveDTO {
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

    /**
     * 是否复用
     */
    @ApiModelProperty(value = "是否复用",required = true)
    @NotNull
    private Boolean reuse;

    /**
     * 复制版本（默认0）
     */
    @ApiModelProperty(value = "复制版本号",required = true)
    @NotNull
    private Long copyVersion;


    /**
     * 数据值
     */
    @NotEmpty
    @ApiModelProperty(value = "数据值",required = true)
    private String value;

    /**
     * 数据值扩展
     */
    @ApiModelProperty(value = "数据值扩展（如checkbox的所有值）",required = true)
    private String valueExtension;

    /**
     * 组件id
     */
    @ApiModelProperty(value = "组件id",required = true)
    @NotNull
    private Long fieldId;

    /**
     * 组件类型
     */
    @NotEmpty
    @ApiModelProperty(value = "组件类型",required = true)
    private String componentType;

    /**
     * 操作时间
     */
    @NotNull
    @ApiModelProperty(value = "操作时间",required = true)
    private LocalDateTime operationTime;

    /**
     * 操作人
     */
    @NotEmpty
    @ApiModelProperty(value = "操作人",required = true)
    private String operationUser;

    /**
     * 复核人
     */
    @ApiModelProperty(value = "复核人")
    private String reviewUser;

    /**
     * 复核时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime reviewTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
