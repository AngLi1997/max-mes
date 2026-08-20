package com.bmos.platform.service.system.role.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@ToString
@TableName("bp_auth_role_menu")
@Data
public class AuthRoleMenu {

    private Long roleId;

    private Long menuId;
}
