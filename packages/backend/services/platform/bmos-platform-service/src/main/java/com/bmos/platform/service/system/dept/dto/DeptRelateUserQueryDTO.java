package com.bmos.platform.service.system.dept.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("部门关联用户查找DTO")
@Getter
@Setter
@ToString
public class DeptRelateUserQueryDTO extends BasePage {

    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("用户名称")
    private String userName;

}
