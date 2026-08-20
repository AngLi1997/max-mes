package com.bmos.platform.facade.system.user.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FeignUserVO {

    private String userId;

    private String userName;

    private String loginName;
}
