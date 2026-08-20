package com.bmos.platform.service.system.dept.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("可分配人员查询DTO")
@Getter
@Setter
@ToString
public class DeptAssignQueryDTO {

    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("查询字样")
    private String name;
}
