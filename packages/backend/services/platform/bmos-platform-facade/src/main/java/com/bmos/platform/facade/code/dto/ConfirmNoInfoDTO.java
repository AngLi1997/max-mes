package com.bmos.platform.facade.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

@ApiModel("批量确认信息DTO")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmNoInfoDTO {

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
