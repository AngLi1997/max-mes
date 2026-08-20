package com.bmos.mes.service.signature.vo;

import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("签名校验VO")
public class SignatureValidateVO {

    private Boolean validated;

    private String signatureToken;

    public String getSignatureToken() {
        return RandomUtil.randomString(8);
    }
}
