package com.bmos.platform.service.system.role.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("bp_role_type")
@Getter
@Setter
@ToString
public class RoleType extends BaseDO {

    private String roleTypeName;

    private Long parentId;

}
