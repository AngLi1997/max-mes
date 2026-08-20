package com.bmos.platform.service.system.dept.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("部门保存DTO")
@Getter
@Setter
@ToString
public class DeptSaveDTO {

    @ApiModelProperty("上级部门id")
    @NotNull
    private Long parentId;

    @ApiModelProperty("上级部门编码")
    @NotBlank
    private String parentCode;

    @ApiModelProperty("部门名称")
    @NotBlank
    private String deptName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色集合")
    private List<Long> roleIds;

}
