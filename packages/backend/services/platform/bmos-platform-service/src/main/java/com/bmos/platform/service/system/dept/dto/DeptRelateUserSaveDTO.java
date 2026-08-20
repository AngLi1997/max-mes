package com.bmos.platform.service.system.dept.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("部门关联用户DTO")
@Getter
@Setter
@ToString
public class DeptRelateUserSaveDTO {

    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("用户id")
    private String userId;

}
