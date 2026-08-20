package com.bmos.platform.service.system.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("用户表信息VO")
public class UserInfoVO {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("登录名")
    private String loginName;

    @ApiModelProperty("用户名")
    private String userName;

}
