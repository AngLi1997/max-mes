package com.bmos.platform.service.system.dept.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("bp_user_dept")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"userId", "deptId"}, callSuper = false)
public class DeptRelateUser extends BaseDO {
    private String userId;
    private Long deptId;
}
