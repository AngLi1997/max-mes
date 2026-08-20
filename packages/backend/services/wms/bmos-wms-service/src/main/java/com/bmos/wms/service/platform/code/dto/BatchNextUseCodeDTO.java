package com.bmos.wms.service.platform.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@ApiModel("BatchNextUseCodeDTO:获取下一个使用的编码数据")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchNextUseCodeDTO {

    @NotEmpty
    @ApiModelProperty("编码规则code")
    private String code;

    @NotNull
    @ApiModelProperty("编码规则详情传参")
    private Map<String, String> fields;

    @NotNull
    @ApiModelProperty("生成数量")
    private Integer num;
}
