package com.bmos.platform.service.system.user.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 历史密码
 */
@Getter
@Setter
@Builder
@TableName("bp_password_history")
public class PasswordHistory extends BaseDO {

    /**
     * 历史密码
     */
    private String pwd;

    /**
     * 用户id
     */
    private String userId;

}
