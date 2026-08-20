package com.bmos.platform.service.signature.vo;

import com.bmos.logging.util.LogTranslateUtil;
import com.bmos.platform.common.enums.signature.SignatureActionEnum;
import com.bmos.platform.common.enums.signature.SignatureTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("签名追溯分页VO")
public class SignaturePageVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("系统名称")
    private String systemName;

    @ApiModelProperty("系统编码")
    private String systemCode;

    @ApiModelProperty("账户")
    private String loginName;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("签名动作")
    private SignatureActionEnum signatureAction;

    @ApiModelProperty("签名操作对象")
    private String signatureData;

    @ApiModelProperty("签名类型")
    @NotNull
    private SignatureTypeEnum signatureType;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否成功")
    private Boolean success;

    @ApiModelProperty("签名时间")
    private LocalDateTime createTime;

    @ApiModelProperty("签名对象详情")
    private String signatureDataDetail;

    @ApiModelProperty("搜索游标")
    private Long searchAfter;

    public String getSignatureDataDetail() {
        return LogTranslateUtil.translateJson(signatureData);
    }

}
