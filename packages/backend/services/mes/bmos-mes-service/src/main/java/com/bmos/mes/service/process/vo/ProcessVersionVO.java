package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@ApiModel("工艺版本VO")
@Builder
public class ProcessVersionVO {

    @Tolerate
    public ProcessVersionVO(){}

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺版本号")
    private String version;

    @ApiModelProperty("工艺版本id")
    private Long processVersionId;
}
