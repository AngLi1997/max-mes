package com.bmos.mes.service.platform.role.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("平台角色VO")
public class PlatformRoleVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String roleName;

    @ApiModelProperty("数据是否删除")
    private Boolean disabled;
}
