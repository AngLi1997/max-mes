package com.bmos.mes.service.preparation.produce.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 切换产出人dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:02
 */
@ApiModel("成品产出切换产出人dto")
@Data
public class ProduceChangeUserDTO {

    /**
     * 产出产出流程id
     */
    @ApiModelProperty(value = "产出流程id", example = "1", required = true)
    @NotNull
    private Long progressId;

    /**
     * 产出人id
     */
    @ApiModelProperty(value = "产出人id", example = "1", required = true)
    @NotBlank
    private String producerId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1", required = true)
    @NotBlank
    private String reCheckerId;
}
