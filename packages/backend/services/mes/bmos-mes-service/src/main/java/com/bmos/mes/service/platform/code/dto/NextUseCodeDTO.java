package com.bmos.mes.service.platform.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("NextUseCodeDTO:获取下一个使用的编码数据")
@Data
public class NextUseCodeDTO {

    @ApiModelProperty("编码规则code")
    private String code;

    @ApiModelProperty("编码规则详情传参")
    private Map<String, String> fields;
}
