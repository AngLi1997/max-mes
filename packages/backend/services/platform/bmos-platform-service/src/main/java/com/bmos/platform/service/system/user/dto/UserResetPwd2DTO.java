package com.bmos.platform.service.system.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@ApiModel("用户修改密码DTO2")
@Getter
@Setter
@ToString
public class UserResetPwd2DTO {

    @ApiModelProperty("用户登录名")
    @NotEmpty
    private String userName;

    @ApiModelProperty("新密码")
    @NotEmpty
    private String newPassword;

    @ApiModelProperty("旧密码")
    @NotEmpty
    private String password;
}
