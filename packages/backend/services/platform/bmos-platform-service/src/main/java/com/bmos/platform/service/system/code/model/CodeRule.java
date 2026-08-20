package com.bmos.platform.service.system.code.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.enums.BooleanEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* 编码规则主表
*/
@Getter
@Setter
@ToString
@TableName(value = "bp_code_rule")
public class CodeRule extends BaseDO {
    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("是否支持修改")
    private BooleanEnum canUpdate;
}
