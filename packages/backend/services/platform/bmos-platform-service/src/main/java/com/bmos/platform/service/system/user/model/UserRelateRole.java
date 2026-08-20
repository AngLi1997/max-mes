package com.bmos.platform.service.system.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("bp_user_role")
@Getter
@Setter
@ToString
public class UserRelateRole extends BaseDO{
    private String userId;
    private Long roleId;
}
