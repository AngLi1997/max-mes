package com.bmos.platform.service.system.user.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("用户列表项VO")
public class UserListItemVO {

    private String userId;

    private String userName;

    private String loginName;

}
