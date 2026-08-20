package com.bmos.platform.service.system.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 签名密码
 * @author liang
 * @version 1.0.0
 * @date 2024/11/26 13:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bp_user_signature_pwd")
public class UserSignaturePassword extends BaseDO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 签名密码
     */
    private String signaturePassword;
}
