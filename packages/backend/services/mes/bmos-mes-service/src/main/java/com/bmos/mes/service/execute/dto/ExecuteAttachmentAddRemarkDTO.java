package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("生产附件添加备注DTO")
public class ExecuteAttachmentAddRemarkDTO {

    @ApiModelProperty("生产附件id")
    @NotNull
    private Long id;

    @ApiModelProperty("备注")
    private String remark;

}
