package com.bmos.platform.service.unit.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName(value = "bp_unit_extend")
public class UnitExtend extends BaseDO {

    @ApiModelProperty(value = "扩展单位名称")
    private String extendUnitName;

    @ApiModelProperty(value = "标准单位标识")
    private Long unitId;

    @ApiModelProperty(value = "表达式值")
    private String expressionValue;

    @ApiModelProperty(value = "是否启用；0：未启用；1：启用")
    private Boolean state;

    @ApiModelProperty(value = "扩展单位精度")
    private Long extendPrecision;

    @ApiModelProperty(value = "备注")
    private String remark;
}
