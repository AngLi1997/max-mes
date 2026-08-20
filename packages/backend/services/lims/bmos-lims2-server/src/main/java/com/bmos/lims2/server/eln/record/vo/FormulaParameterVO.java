package com.bmos.lims2.server.eln.record.vo;

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
@ApiModel(value = "保存公式参数vo")
public class FormulaParameterVO {

    @ApiModelProperty(value = "参数")
    private String key;

    @ApiModelProperty(value = "参数简称")
    private String value;

    @ApiModelProperty(value = "关联组件id")
    private String fieldId;

    @ApiModelProperty(value = "关联详情")
    private String detail;

    @ApiModelProperty(value = "记录描述")
    private String describe;
}
