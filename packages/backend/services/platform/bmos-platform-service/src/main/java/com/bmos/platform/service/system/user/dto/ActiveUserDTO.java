package com.bmos.platform.service.system.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 账号激活
 */
@Getter
@Setter
@ApiModel("账号激活DTO")
public class ActiveUserDTO{

    /**
     * 登录账户
     */
    @ApiModelProperty("登录账户")
    @NotEmpty
    private String loginName;

    @ApiModelProperty(value = "新密码",required = true)
    @NotEmpty
    private String newPassword;

}
