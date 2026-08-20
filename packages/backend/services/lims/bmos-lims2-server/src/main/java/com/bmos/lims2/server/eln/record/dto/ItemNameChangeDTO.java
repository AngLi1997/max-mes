package com.bmos.lims2.server.eln.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("记录项名称修改DTO")
@Data
public class ItemNameChangeDTO {

    @ApiModelProperty("记录项id")
    @NotNull
    private Long id;

    @ApiModelProperty("记录项名称")
    @NotBlank
    private String name;

}
