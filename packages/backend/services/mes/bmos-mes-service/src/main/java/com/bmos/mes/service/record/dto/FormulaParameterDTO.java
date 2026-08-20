package com.bmos.mes.service.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "保存公式参数dto")
public class FormulaParameterDTO {

    @ApiModelProperty(value = "参数")
    private String key;

    @ApiModelProperty(value = "参数简称")
    private String value;

    @ApiModelProperty(value = "关联组件id")
    private Long fieldId;

    @ApiModelProperty(value = "版本id")
    private Long recordVersionId;

    @ApiModelProperty(value = "关联详情")
    private String detail;

    @ApiModelProperty(value = "记录描述")
    private String describe;
}
