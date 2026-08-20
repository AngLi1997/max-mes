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
@ApiModel("过期账户修改密码DTO")
public class ExpireUserChangePwdDTO {

    /**
     * 当前登陆人
     */
    @ApiModelProperty(value = "登录账户名",required = true)
    @NotEmpty
    private String loginName;

    @ApiModelProperty(value = "密码",required = true)
    @NotEmpty
    private String newPassword;
}
