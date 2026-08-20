package com.bmos.platform.service.execute.parameter.vo;

import com.bmos.platform.common.enums.execute.parameter.ValueTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("参数配置VO")
public class BusinessParameterVO {

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("取值范围")
    private String valueRange;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("值类型")
    private ValueTypeEnum valueType;

    @ApiModelProperty("参数名称")
    private String name;

}
