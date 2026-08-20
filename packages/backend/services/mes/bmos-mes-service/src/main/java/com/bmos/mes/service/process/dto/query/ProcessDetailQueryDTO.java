package com.bmos.mes.service.process.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@ApiModel("工艺详情")
public class ProcessDetailQueryDTO {

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺版本")
    @NotNull
    private String version;
}
