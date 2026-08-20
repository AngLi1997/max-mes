package com.bmos.lims2.server.eln.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "工艺查询记录vo")
public class SelectRecorVO {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("值")
    private Long value;

    @ApiModelProperty("数据是否删除")
    private Boolean disabled;
}
