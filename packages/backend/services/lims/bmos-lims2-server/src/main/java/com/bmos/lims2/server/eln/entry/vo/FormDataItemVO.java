package com.bmos.lims2.server.eln.entry.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("填报数据项VO")
public class FormDataItemVO {

    @ApiModelProperty("组件id")
    private Long fieldId;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("值")
    private String valueExtension;

    @ApiModelProperty("操作类型")
    private String operationType;

    /**
     * 组件类型
     */
    @ApiModelProperty("组件类型")
    private String componentType;

    @ApiModelProperty("是否是空值")
    private Boolean emptyValue;

    @ApiModelProperty("任务id")
    private Long taskId;

    @ApiModelProperty("是否存在异常批注")
    private Boolean hasAbnormalAnnotation;

}
