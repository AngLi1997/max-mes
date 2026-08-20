package com.bmos.platform.service.signature.dto;

import com.bmos.platform.common.enums.signature.SignatureTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("签名DTO")
public class SignatureDTO {

    @ApiModelProperty("校验用户列表")
    @NotEmpty
    @Valid
    private List<SignatureValidateDTO> validates;

    @ApiModelProperty("签名类型")
    @NotNull
    private SignatureTypeEnum signatureType;

    @ApiModelProperty("系统编码")
    @NotNull
    private Integer systemCode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("签名操作对象")
    private String signatureData;
}
