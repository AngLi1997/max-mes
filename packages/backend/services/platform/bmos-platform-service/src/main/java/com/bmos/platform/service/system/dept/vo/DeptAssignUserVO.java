package com.bmos.platform.service.system.dept.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("部门已分配用户VO")
@Getter
@Setter
@ToString
public class DeptAssignUserVO {
    private String userId;
    private String userName;
    private String loginName;
    private Integer gender;
}
