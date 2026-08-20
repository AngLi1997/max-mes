package com.bmos.lims2.server.inspect.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 请验单模板DTO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Data
@ApiModel("请验单模板DTO")
public class InspectionOrderTemplateDTO {

    @ApiModelProperty("模板ID")
    private Long id;

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("模板备注")
    private String remark;

    @ApiModelProperty("模板状态")
    private Boolean status;

    @ApiModelProperty("模板字段配置列表")
    private List<InspectionOrderTemplateFieldDTO> fields;
}