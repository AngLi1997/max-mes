package com.bmos.mes.service.process.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel("关联工艺查询DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessRelationQueryDTO {

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;
}
