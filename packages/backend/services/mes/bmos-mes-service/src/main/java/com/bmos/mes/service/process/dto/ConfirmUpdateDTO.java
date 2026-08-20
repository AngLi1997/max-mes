package com.bmos.mes.service.process.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "填写审批结论dto")
public class ConfirmUpdateDTO {

    @ApiModelProperty("审批意见")
    @NotBlank
    private String opinion;

    @ApiModelProperty("主键id")
    @NotNull
    private Long id;

    @ApiModelProperty("备注")
    private String remark;
}
