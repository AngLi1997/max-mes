package com.bmos.lims2.server.inspect.document.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("请验单详情数据DTO")
@Data
public class DocumentConfigFieldDTO {

    @ApiModelProperty("请验单数据id")
    private Long id;

    @ApiModelProperty("请验单数据code")
    private String code;

    @ApiModelProperty("请验单展示数据名称")
    private String showName;

    @ApiModelProperty("请验单数据名称")
    private String dataName;

    @ApiModelProperty("是否必填")
    private Boolean required;

    @ApiModelProperty("默认值")
    private String defaultValue;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("字段来源")
    private String fieldSource;

}
