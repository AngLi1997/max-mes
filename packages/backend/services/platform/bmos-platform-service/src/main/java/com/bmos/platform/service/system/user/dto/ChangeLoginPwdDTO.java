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
@ApiModel("修改当前登陆人密码DTO")
public class ChangeLoginPwdDTO {


    @ApiModelProperty(value = "旧密码",required = true)
    @NotEmpty
    private String oldPassword;

    @ApiModelProperty(value = "新密码",required = true)
    @NotEmpty
    private String newPassword;

}
