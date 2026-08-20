package com.bmos.platform.service.execute.parameter.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.common.enums.execute.parameter.ValueTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("BusinessParameterPageDTO:分页查询DTO")
public class BusinessParameterPageDTO extends BasePage {
    @ApiModelProperty("编码")
    private String code;

    @ApiModelEnumProperty(value = "值类型", enumClass = ValueTypeEnum.class)
    private String valueType;

    @ApiModelEnumProperty(value = "业务类型", enumClass = ValueTypeEnum.class)
    private String businessType;

    @ApiModelProperty("所属应用")
    private String belong;

    @ApiModelProperty("参数名称")
    private String name;
}
