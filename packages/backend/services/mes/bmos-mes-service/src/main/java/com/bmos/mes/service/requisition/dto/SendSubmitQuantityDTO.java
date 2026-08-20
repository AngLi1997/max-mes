package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 待发料批次/货品
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 19:03
 */
@Data
@ApiModel("待发料批次/货品")
public class SendSubmitQuantityDTO {

    /**
     * 批次/货品id
     */
    @ApiModelProperty(value = "批次/货品id", example = "1")
    private Long businessId;

    /**
     * 待发量
     */
    @ApiModelProperty(value = "待发量", example = "1.000")
    private BigDecimal targetQuantity;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;
}
