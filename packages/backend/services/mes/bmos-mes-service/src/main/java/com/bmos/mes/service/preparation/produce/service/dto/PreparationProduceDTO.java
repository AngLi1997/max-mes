package com.bmos.mes.service.preparation.produce.service.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 配液产出DTO
 */
@Getter
@Setter
@ApiModel("配液产出DTO")
public class PreparationProduceDTO{

    /**
     * 配液流程id
     */
    @ApiModelProperty(value = "配液流程id",required = true)
    private Long progressId;

    /**
     * 投入设备id
     */
    @ApiModelProperty(value = "产出设备code", example = "1")
    @NotNull
    private String deviceCode;

    /**
     * 产出暂存货位id
     */
    @ApiModelProperty(value = "产出暂存货位Code", example = "1")
    private String cargoPositionCode;

    /**
     * 产出量
     */
    @ApiModelProperty(value = "产出量", example = "1", required = true)
    @NotNull
    private BigDecimal quantity;

    /**
     * 产出人id
     */
    @ApiModelProperty(value = "产出人id", example = "1", required = true)
    @NotEmpty
    private String producerId;

}
