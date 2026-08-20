package com.bmos.lims2.server.inspect.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 自定义字段值DTO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Data
@ApiModel("自定义字段值DTO")
public class CustomFieldValueDTO {

    @ApiModelProperty("字段代码")
    @NotBlank(message = "字段代码不能为空")
    private String fieldCode;

    @ApiModelProperty("字段所属字典分类代码")
    private String dictCode;

    @ApiModelProperty("字段展示名称")
    private String fieldName;

    @ApiModelProperty("字段值")
    private String fieldValue;

    @ApiModelProperty("是否必填")
    private Boolean required;
}