package com.bmos.platform.service.system.user.vo;

import com.bmos.platform.common.enums.UserActiveEnums;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("用户登录VO")
@Getter
@Setter
@ToString
public class UserLoginVO {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("登录名")
    private String loginName;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelEnumProperty(value = "激活状态",enumClass = UserActiveEnums.class)
    private Integer activeStatus;

    @ApiModelProperty("还剩多少天密码过期")
    private Long expireDays;

    /**
     * 是否需要提醒密码剩余多少天过期
     */
    @ApiModelProperty("是否需要提醒密码剩余多少天过期")
    private Boolean remindExpire;
}
