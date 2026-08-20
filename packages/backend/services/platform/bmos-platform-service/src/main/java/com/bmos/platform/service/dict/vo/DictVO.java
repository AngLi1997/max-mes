package com.bmos.platform.service.dict.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "字典下拉框返回VO")
public class DictVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "值")
    private String value;
}
