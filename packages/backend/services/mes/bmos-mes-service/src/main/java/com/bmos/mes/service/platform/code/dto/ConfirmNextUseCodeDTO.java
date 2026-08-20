package com.bmos.mes.service.platform.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("ConfirmNextUseCodeDTO:确认下一个使用的编码数据")
public class ConfirmNextUseCodeDTO {

    @NotEmpty
    @ApiModelProperty("编码规则code")
    private String code;

    @NotEmpty
    @ApiModelProperty("完整标号")
    private String fullNo;

    @ApiModelProperty("公式申请时的日期 -- 规则中没有日期字段可不传")
    private LocalDate codeApplyTime;

    @NotNull
    @ApiModelProperty("编码规则详情传参")
    private Map<String, String> fields;
}
