package com.bmos.wms.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 发起请验信息（请验单字段值）。
 */
@Getter
@Setter
@ApiModel(value = "发起请验信息DTO")
public class InitiateInspectInfoDTO {

    @ApiModelProperty("请验单配置数据id（LIMS document_config_field 主键）")
    @NotNull
    private Long inspectConfigDataId;

    @ApiModelProperty("展示名称")
    @NotNull
    private String showName;

    @ApiModelProperty("字段 code")
    private String code;

    @ApiModelProperty("字段名称")
    private String dataName;

    @ApiModelProperty("是否必填")
    private Boolean required;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("排序")
    private Integer sort;
}
