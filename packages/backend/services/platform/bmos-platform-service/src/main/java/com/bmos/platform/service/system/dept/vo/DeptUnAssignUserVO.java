package com.bmos.platform.service.system.dept.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("部门未分配用户VO")
@Getter
@Setter
@ToString
public class DeptUnAssignUserVO {
    private String userId;
    private String name;
    private String userName;
    private String loginName;
}
