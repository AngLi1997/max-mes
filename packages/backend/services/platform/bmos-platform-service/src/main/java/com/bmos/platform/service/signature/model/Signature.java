package com.bmos.platform.service.signature.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.enums.signature.SignatureActionEnum;
import com.bmos.platform.common.enums.signature.SignatureTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("bp_signature_log")
public class Signature extends BaseDO {

    /**
     * 签名类型
     */
    private SignatureTypeEnum signatureType;

    /**
     * 操作对象
     */
    private String signatureData;

    /**
     * 签名动作
     */
    private SignatureActionEnum signatureAction;

    /**
     * 系统编码
     */
    private Integer systemCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 登录账户
     */
    private String loginName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 是否成功
     */
    private Boolean success;

}
