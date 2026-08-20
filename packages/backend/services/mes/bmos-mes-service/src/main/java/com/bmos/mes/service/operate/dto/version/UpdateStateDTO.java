package com.bmos.mes.service.operate.dto.version;

import com.bmos.mes.common.enums.operate.OperateRuleVersionStateEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@ApiModel(value = "修改版本状态dto")
public class UpdateStateDTO {

    @ApiModelProperty("主键id")
    @NotNull
    private Long id;

    @ApiModelProperty("状态")
    @ApiModelEnumProperty(value = "状态",enumClass = OperateRuleVersionStateEnum.class,required = true)
    @NotBlank
    private String state;
}
