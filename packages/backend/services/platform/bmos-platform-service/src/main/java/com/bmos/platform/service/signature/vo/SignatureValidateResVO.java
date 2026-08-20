package com.bmos.platform.service.signature.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("签名校验V2返回VO")
@AllArgsConstructor
public class SignatureValidateResVO {

    @ApiModelProperty("未通过的校验index")
    private List<Integer> failedIndex = new ArrayList<>();

}
