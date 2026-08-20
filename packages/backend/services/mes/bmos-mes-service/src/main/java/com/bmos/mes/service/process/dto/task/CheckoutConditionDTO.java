package com.bmos.mes.service.process.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel("校验表达式条件")
public class CheckoutConditionDTO {

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("结果")
    private Boolean result;
}
