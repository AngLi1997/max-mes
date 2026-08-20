package com.bmos.lims2.server.eln.entry.dto;

import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class FormDataModifyDTO {
    /**
     * 请验单id
     */
    @ApiModelProperty(value = "请验单id",required = true)
    @NotNull
    private Long inspectionOrderId;

    /**
     * 批号
     */
    @NotEmpty
    @ApiModelProperty(value = "批号",required = true)
    private String batchNo;


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
    @NotNull
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
     * 复核时间
     */
    @NotNull
    @ApiModelProperty(value = "复核时间",required = true)
    private LocalDateTime reviewTime;

    /**
     * 备注
     */
    @NotEmpty
    @ApiModelProperty(value = "备注",required = true)
    private String remark;

    @ApiModelProperty(value = "操作人",required = true)
    @NotEmpty
    private String operationUser;

    @ApiModelProperty(value = "复核人",required = true)
    @NotEmpty
    private String reviewUser;

    @ApiModelProperty(value = "原值")
    private String originalValue;

    @ApiModelProperty("是否是空值")
    private Boolean emptyValue;
}
