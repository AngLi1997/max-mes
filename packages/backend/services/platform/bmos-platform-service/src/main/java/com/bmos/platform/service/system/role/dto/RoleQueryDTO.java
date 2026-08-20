package com.bmos.platform.service.system.role.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ApiModel("角色查询DTO")
@Getter
@Setter
@ToString
public class RoleQueryDTO extends BasePage {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色类型id")
    private Long roleTypeId;

    private List<Long> roleTypeIdList;
}
