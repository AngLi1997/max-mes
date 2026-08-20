package com.bmos.mes.service.preparation.measure.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("可用配液计划VO")
@Data
public class UnmeasuredPreparationPlanVO {

    @ApiModelProperty("配液计划id")
    private Long id;

    @ApiModelProperty("配液单名称")
    private String name;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long batchCount;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long measureCount;

}
