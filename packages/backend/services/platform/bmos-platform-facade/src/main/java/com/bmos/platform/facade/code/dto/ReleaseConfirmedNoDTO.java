package com.bmos.platform.facade.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@ApiModel("释放已确认的编号")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseConfirmedNoDTO {

    @ApiModelProperty("规则编码")
    @NotBlank
    private String code;

    @ApiModelProperty("编号")
    @NotBlank
    private String no;

    @ApiModelProperty("编码规则详情传参")
    @NotNull
    private Map<String, String> fields;
}
