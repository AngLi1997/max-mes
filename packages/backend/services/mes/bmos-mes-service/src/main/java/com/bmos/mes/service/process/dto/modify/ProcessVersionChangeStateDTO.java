package com.bmos.mes.service.process.dto.modify;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.process.ActionStateEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("改变状态DTO")
public class ProcessVersionChangeStateDTO {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

    @ApiModelEnumProperty(value = "修改工艺版本状态",enumClass = ActionStateEnum.class,required = true)
    @NotBlank
    @EnumValidate(value = ActionStateEnum.class)
    private String actionState;
}
