package com.bmos.platform.facade.system.dept.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ApiModel("部门树用户VO")
@Getter
@Setter
@ToString
public class DeptTreeUserVO {
    private String userId;
    private String name;
    private String userName;
    private String loginName;
}
