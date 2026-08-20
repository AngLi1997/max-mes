package com.bmos.platform.facade.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ApiModel("BatchNextUseCodeDTO:获取下一个使用的编码数据")
public class BatchCodeNoDTO {
    @NotEmpty
    @ApiModelProperty("编码规则code")
    private String code;

    @NotEmpty
    @ApiModelProperty("编码规则详情传参列表")
    private List<Map<String, String>> fields;
}
