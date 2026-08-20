package com.bmos.platform.service.system.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户详情VO
 */
@ApiModel("用户详情VO")
@Getter
@Setter
public class UserDetailInfoVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("登录名")
    private String loginName;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("备注")
    private String remark;

    /**
     * 账户锁定后的解锁时间
     * 若是永久锁定，当前值为永久锁定，而非时间
     */
    @ApiModelProperty("账户锁定后的解锁时间 若是永久锁定，当前值为[永久锁定]，而非时间")
    private String unLockTime;

    /**
     * 用户所属角色
     */
    @ApiModelProperty("用户所属角色")
    private List<String> roleNameList;

    /**
     * 用户所属部门
     */
    @ApiModelProperty("用户所属部门")
    private List<String> deptNameList;

    /**
     * 用户所属工位
     */
    @ApiModelProperty("用户所属工位")
    private List<String> stationNameList;

}
