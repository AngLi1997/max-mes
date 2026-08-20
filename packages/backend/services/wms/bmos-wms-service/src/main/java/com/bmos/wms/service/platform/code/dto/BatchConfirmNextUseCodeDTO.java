package com.bmos.wms.service.platform.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("BatchConfirmNextUseCodeDTO:确认下一个使用的编码数据")
public class BatchConfirmNextUseCodeDTO {
    @NotEmpty
    @ApiModelProperty("编码规则code")
    private String code;

    @NotEmpty
    @ApiModelProperty("完整标号")
    private List<String> fullNos;

    @ApiModelProperty("公式申请时的日期 -- 规则中没有日期字段可不传")
    private LocalDate codeApplyTime;

    @NotNull
    @ApiModelProperty("编码规则详情传参")
    private Map<String, String> fields;
}
