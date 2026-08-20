package com.bmos.lims2.server.eln.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "记录树VO")
public class RecordTreeVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "记录名称")
    private String name;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;
}
