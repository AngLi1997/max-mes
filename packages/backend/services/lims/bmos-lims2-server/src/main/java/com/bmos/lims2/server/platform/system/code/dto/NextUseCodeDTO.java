package com.bmos.lims2.server.platform.system.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@ApiModel("NextUseCodeDTO:获取下一个使用的编码数据")
public class NextUseCodeDTO {

    @ApiModelProperty("编码规则code")
    private String code;

    @NotNull
    @ApiModelProperty("编码规则详情传参")
    private Map<String, String> fields;
}
