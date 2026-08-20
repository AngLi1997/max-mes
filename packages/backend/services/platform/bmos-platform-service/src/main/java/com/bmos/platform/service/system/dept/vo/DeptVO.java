package com.bmos.platform.service.system.dept.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("部门VO")
@Getter
@Setter
@ToString
public class DeptVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("父级部门名称")
    private String parentName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否有子部门")
    private Boolean flag;

}
