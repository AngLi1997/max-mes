package com.bmos.mes.service.record.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel(value = "更新批记录版本DTO")
public class RecordVersionDTO {

    @ApiModelProperty(value = "版本id")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "记录管理表id")
    private Long recordId;

    @ApiModelProperty(value = "版本号")
    private String version;

    @NotBlank
    private String state;

    @ApiModelProperty(value = "备注")
    private String remark;
}
