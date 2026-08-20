package com.bmos.platform.service.system.user.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ApiModel("用户分页查询VO")
@Getter
@Setter
@ToString
public class UserPageVO {
    private Long id;
    private String userId;
    private String userName;
    private String loginName;
    private Integer gender;
    private String phone;
    private String email;
    private Integer status;
    private Integer state;
    private String remark;
}
