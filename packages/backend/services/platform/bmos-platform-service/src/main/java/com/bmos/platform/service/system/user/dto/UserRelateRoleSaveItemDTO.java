package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ApiModel("用户关联角色子数据DTO")
@Getter
@Setter
@ToString
public class UserRelateRoleSaveItemDTO {

    @ApiModelProperty("用户id")
    @NotBlank
    private String userId;

    @ApiModelProperty("角色id")
    private Long roleId;

}
