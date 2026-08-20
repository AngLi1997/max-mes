package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("用户关联角色DTO")
@Getter
@Setter
@ToString
public class UserRelateRoleSaveDTO {

    @ApiModelProperty("角色id")
    @NotNull
    private Long roleId;

    @ApiModelProperty("子数据的集合")
    private List<UserRelateRoleSaveItemDTO> items;

}
