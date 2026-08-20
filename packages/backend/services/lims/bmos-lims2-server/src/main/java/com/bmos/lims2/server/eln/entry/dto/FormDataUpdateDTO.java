package com.bmos.lims2.server.eln.entry.dto;

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
public class FormDataUpdateDTO {
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
    @NotNull
    private Long schemeId;

    /**
     * 工艺版本
     */
    @ApiModelProperty(value = "方案版id",required = true)
    @NotNull
    private Long schemeVersionId;

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

    @ApiModelProperty(value = "操作人",required = true)
    @NotEmpty
    private String operationUser;
}
