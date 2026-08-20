package com.bmos.platform.service.system.role.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("角色分配用户VO")
@Getter
@Setter
@ToString
public class RoleAssignUserVO {
    private String userId;
    private String userName;
}
