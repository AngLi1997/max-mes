package com.bmos.mes.service.process.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel("校验表达式dto")
public class CheckoutExpressionDTO {

    @ApiModelProperty("表达式")
    @NotBlank
    private String expression;

    @ApiModelProperty("条件")
    @NotEmpty
    private List<CheckoutConditionDTO> conditionList;
}
