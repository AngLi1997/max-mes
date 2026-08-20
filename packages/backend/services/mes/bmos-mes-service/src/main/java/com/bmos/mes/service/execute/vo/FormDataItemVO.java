package com.bmos.mes.service.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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

}
