package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@ApiModel("用户修改密码DTO")
@Getter
@Setter
@ToString
public class UserResetPwdDTO {

    @ApiModelProperty("用户id")
    @NotEmpty
    private String userId;

    @ApiModelProperty("新密码")
    @NotEmpty
    private String password;
}
