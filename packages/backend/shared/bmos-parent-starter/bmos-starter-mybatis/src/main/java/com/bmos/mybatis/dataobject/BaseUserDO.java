package com.bmos.mybatis.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bmos.common.base.user.SysUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseUserDO extends BaseDO implements Serializable, SysUser {
    public BaseUserDO() {}

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;


    private String userName;

    private Integer activeStatus;

    private Integer state;

    /**
     * 登录成功后返回 token
     */
    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private Integer terminalType;

    @TableField(exist = false)
    private String serviceType;

    @TableField(exist = false)
    private Long loginTime;

    @TableField(exist = false)
    private Boolean activated;
}
