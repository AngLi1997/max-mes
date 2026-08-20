package com.bmos.lims2.server.inspect.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请验单模板字段DTO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Data
@ApiModel("请验单模板字段DTO")
public class InspectionOrderTemplateFieldDTO {

    @ApiModelProperty("字段ID")
    private Long id;

    @ApiModelProperty("字段代码")
    private String code;

    @ApiModelProperty("字段展示名称")
    private String showName;

    @ApiModelProperty("字段数据名称")
    private String dataName;

    @ApiModelProperty("是否必填")
    private Boolean required;

    @ApiModelProperty("默认值")
    private String defaultValue;

    @ApiModelProperty("排序")
    private Integer sort;
}