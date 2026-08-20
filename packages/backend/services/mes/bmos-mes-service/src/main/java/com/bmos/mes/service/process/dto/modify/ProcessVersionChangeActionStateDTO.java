package com.bmos.mes.service.process.dto.modify;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.StateEnum;
import com.bmos.mes.common.enums.process.ActionStateEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("改变状态DTO")
public class ProcessVersionChangeActionStateDTO {

    @ApiModelProperty("版本id")
    @NotNull
    private Long id;

    @ApiModelEnumProperty(value = "启停状态",enumClass = ActionStateEnum.class,required = true)
    @NotNull
    @EnumValidate(value = ActionStateEnum.class)
    private String activeState;
}
