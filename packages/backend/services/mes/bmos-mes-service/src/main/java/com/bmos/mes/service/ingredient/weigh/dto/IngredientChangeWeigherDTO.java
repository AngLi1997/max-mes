package com.bmos.mes.service.ingredient.weigh.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 切换称量人dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:02
 */
@ApiModel("配料称量切换称量人dto")
@Data
public class IngredientChangeWeigherDTO {

    /**
     * 配料称量流程id
     */
    @ApiModelProperty(value = "配料称量流程id", example = "1", required = true)
    @NotNull
    private Long ingredientWeighProcessId;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1", required = true)
    @NotBlank
    private String weigherId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1", required = true)
    @NotBlank
    private String reCheckerId;
}
