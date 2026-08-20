package com.bmos.mes.service.process.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("工艺集合查询DTO")
public class ProcessListQueryDTO {

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("是否已启用")
    private Boolean active;
}
