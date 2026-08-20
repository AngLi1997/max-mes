package com.bmos.platform.service.system.dept.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 部门与角色之间的绑定关系
 */
@TableName("bp_dept_role")
@Getter
@Setter
@ToString
public class DeptRole {

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 角色id
     */
    private Long roleId;

}
