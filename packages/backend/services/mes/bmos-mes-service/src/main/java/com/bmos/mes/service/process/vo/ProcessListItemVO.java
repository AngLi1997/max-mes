package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

@Getter
@Setter
@ToString
@ApiModel("工艺集合VO")
@Builder
public class ProcessListItemVO {

    @Tolerate
    public ProcessListItemVO(){}

    @ApiModelProperty("版本id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("版本号")
    private String activeVersion;
}
