package com.bmos.platform.service.system.role.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("bp_role_menu")
@Getter
@Setter
@ToString
public class RoleRelateMenu {
    private Long roleId;
    private Long menuId;
}
