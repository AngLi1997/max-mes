package com.bmos.mes.service.platform.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ApiModel("BatchConfirmNextUseCodeDTO:确认下一个使用的编码数据")
public class BatchConfirmNextUseCodeDTO {
    @Tolerate
    public BatchConfirmNextUseCodeDTO() {}
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
    private Map fields;
}
