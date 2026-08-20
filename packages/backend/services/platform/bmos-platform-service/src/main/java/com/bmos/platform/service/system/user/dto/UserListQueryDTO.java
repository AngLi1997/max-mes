package com.bmos.platform.service.system.user.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.platform.service.system.user.enums.UserStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("用户列表项VO")
public class UserListQueryDTO {


    @ApiModelEnumProperty(value = "启用状态111",enumClass = UserStatusEnum.class,required = true)
    @EnumValidate(value = UserStatusEnum.class)
    private Integer state;

}
