package com.bmos.platform.service.unit.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
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
@TableName(value = "bp_unit")
public class Unit extends BaseDO {

    @ApiModelProperty(value = "标准单位名称")
    private String unitName;

    @ApiModelProperty(value = "精度")
    private Long unitPrecision;

    @ApiModelProperty(value = "修约规则id")
    private String roundCode;

    @ApiModelProperty(value = "是否启用：0：未启用，1：启用")
    private Boolean state;

    @ApiModelProperty(value = "备注")
    private String remark;
}
