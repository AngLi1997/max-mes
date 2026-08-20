package com.bmos.platform.service.execute.parameter.vo;

import com.bmos.platform.common.enums.execute.parameter.ValueTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("BusinessParameterDetailVO:参数配置详情VO")
public class BusinessParameterDetailVO {
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
