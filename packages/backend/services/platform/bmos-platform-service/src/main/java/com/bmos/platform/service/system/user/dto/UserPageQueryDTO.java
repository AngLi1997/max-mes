package com.bmos.platform.service.system.user.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("用户分页查询DTO")
@Getter
@Setter
@ToString
@Builder
public class UserPageQueryDTO extends BasePage {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户名称（模糊查询）")
    private String userName;

    @ApiModelProperty("用户账号（模糊查询）")
    private String loginName;

    @ApiModelProperty("手机号（模糊查询）")
    private String phone;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("启停")
    private Integer state;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("是否导出所有数据：true-是,false-否")
    private Boolean isFlay;
}
