package com.bmos.platform.service.system.role.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("角色集合查询")
public class RoleListQueryDTO {

    private List<Long> ids;
}
