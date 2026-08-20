package com.bmos.platform.service.system.dept.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@ApiModel("移除部门用户DTO")
public class DeptUserRemoveDTO {

    @ApiModelProperty(value = "用户id",required = true)
    @NotBlank
    private String userId;

    @ApiModelProperty(value = "部门id",required = true)
    @NonNull
    private Long deptId;
}
