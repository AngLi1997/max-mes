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
public class PreparationProduceCheckUserDTO extends PreparationProduceComponentDTO {

    /**
     * 权限码
     */
    @ApiModelProperty(value = "权限码")
    private String authCode;

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
