package com.bmos.platform.service.system.role.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("bp_role")
@Getter
@Setter
@ToString
public class Role extends BaseDO {

    private String roleName;

    private Long roleTypeId;

    private String description;

}
