package com.bmos.platform.service.execute.parameter.vo;

import com.bmos.platform.common.enums.execute.parameter.BusinessTypeEnum;
import com.bmos.platform.common.enums.execute.parameter.ValueTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("BusinessParameterPageVO:参数配置VO")
public class BusinessParameterPageVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("值类型 1STRING 2 NUMBER 3BOOLEAN 4 ENUM 5 json")
    private ValueTypeEnum valueType;

    @ApiModelProperty("业务类型 1 业务 2 系统")
    private BusinessTypeEnum businessType;

    @ApiModelProperty("所属应用")
    private String belong;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("取值范围")
    private String valueRange;

    @ApiModelProperty("参数名称")
    private String name;
}
