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
@ApiModel(value = "字典列表集合vo")
public class DictDetailListVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "数据标签")
    private String dictLabel;

    @ApiModelProperty(value = "数据值")
    private String dictValue;

    @ApiModelProperty(value = "字典id")
    private Long dictId;
}
