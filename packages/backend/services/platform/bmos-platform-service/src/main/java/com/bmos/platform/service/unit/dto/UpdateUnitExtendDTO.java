package com.bmos.platform.service.unit.dto;

import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "更新扩展单位dto")
public class UpdateUnitExtendDTO {

    @ApiModelProperty(value = "扩展单位id")
    private Long id;

    @ApiModelProperty(value = "标准单位id")
    private Long unitId;

    @ApiModelProperty(value = "扩展单位名称")
    private String extendUnitName;

    @ApiModelProperty(value = "表达式值")
    private String expressionValue;

    @ApiModelProperty(value = "是否启用；0：未启用；1：启用")
    @ApiModelEnumProperty(value = "启停",enumClass = StatusEnum.class,required = true)
    private Boolean state;

    @ApiModelProperty(value = "扩展单位精度")
    private Long extendPrecision;

    @ApiModelProperty(value = "备注")
    private String remark;
}
