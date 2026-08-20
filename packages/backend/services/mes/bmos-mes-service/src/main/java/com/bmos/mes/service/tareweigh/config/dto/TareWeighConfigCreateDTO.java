package com.bmos.mes.service.tareweigh.config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 皮重配置新增DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:36
 */
@Data
@ApiModel("皮重配置新增DTO")
public class TareWeighConfigCreateDTO {

    @ApiModelProperty(value = "毛重", example = "10.0")
    @NotNull
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    private BigDecimal tareWeigh;

    @ApiModelProperty(value = "单位id", example = "1")
    @NotNull
    private Long unitId;

    @ApiModelProperty(value = "描述", example = "皮重配置")
    @Length(max = 200)
    private String describeInfo;
}
