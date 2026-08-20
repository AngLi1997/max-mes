package com.bmos.lims2.web.inspect.order.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自定义字段值响应VO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Data
@ApiModel("自定义字段值响应")
public class CustomFieldValueRespVO {

    @ApiModelProperty("字段代码")
    private String fieldCode;

    @ApiModelProperty("字段展示名称")
    private String fieldName;

    @ApiModelProperty("字段值")
    private String fieldValue;

    @ApiModelProperty("是否必填")
    private Boolean required;
}