package com.bmos.lims2.server.inspect.document.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 请验单配置字段排序项
 * @Author: yigaohui
 * @Date: 2025/09/30 11:32
 */
@Data
@ApiModel("请验单配置字段排序项")
public class FieldSortDTO {

    @ApiModelProperty(value = "字段ID", required = true)
    @NotNull
    private Long fieldId;

    @ApiModelProperty(value = "排序值", required = true)
    @NotNull
    private Integer sort;
}


