package com.bmos.platform.service.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * 手写签名表(BpUserSign)实体类
 *
 * @author makejava
 * @since 2024-07-03 11:08:04
 */
@Getter
@Setter
@TableName("bp_user_sign")
public class UserSign extends BaseDO {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 签名url
     */
    private String signUrl;
    /**
     * 签名时间
     */
    private LocalDateTime signTime;
    /**
     * 终端类型 0-pc 1-pad
     */
    private String terminalType;

}

