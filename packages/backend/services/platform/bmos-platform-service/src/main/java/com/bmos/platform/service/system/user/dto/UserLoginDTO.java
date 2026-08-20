package com.bmos.platform.service.system.user.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.platform.common.enums.ServiceTypeEnums;
import com.bmos.platform.common.enums.TerminalTypeEnums;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ApiModel("用户登录DTO")
@Getter
@Setter
@ToString
public class UserLoginDTO {

    @ApiModelProperty("账号")
    @NotBlank
    private String loginName;

    @ApiModelProperty("密码")
    @NotBlank
    private String password;

    @ApiModelEnumProperty(value = "终端类型",enumClass = TerminalTypeEnums.class)
    @EnumValidate(value = TerminalTypeEnums.class)
    private Integer terminalType;

    @ApiModelEnumProperty(value = "服务类型",enumClass = ServiceTypeEnums.class)
    @EnumValidate(value = ServiceTypeEnums.class)
    private String serviceType;

}
