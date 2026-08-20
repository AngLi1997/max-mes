package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@ApiModel("校验密码DTO")
public class ValidatePwdDTO {

    @ApiModelProperty("登录名")
    @NotEmpty
    private String loginName;

    @ApiModelProperty("密码")
    @NotEmpty
    private String password;
}
