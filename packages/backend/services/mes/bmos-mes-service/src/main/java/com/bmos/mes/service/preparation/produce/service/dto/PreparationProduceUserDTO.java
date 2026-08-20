package com.bmos.mes.service.preparation.produce.service.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 确认配液产出的复核人员信息DTO
 */
@Getter
@Setter
@ApiModel("配液产出复核人员信息DTO")
public class PreparationProduceUserDTO {

    /**
     * 流程id
     */
    @ApiModelProperty(value = "流程id")
    private Long progressId;

    /**
     * 工序id
     */
    @ApiModelProperty(value = "工序id")
    private Long processId;

    /**
     * 工序版本
     */
    @ApiModelProperty(value = "工序版本")
    private String processVersion;

}
