package com.bmos.platform.facade.system.role.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FeignRoleVO {

    private Long id;

    private String roleName;

    private Boolean isDeleted;
}
