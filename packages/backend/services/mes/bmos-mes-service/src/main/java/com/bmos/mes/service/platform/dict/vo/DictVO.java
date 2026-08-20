package com.bmos.mes.service.platform.dict.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Data
@ApiModel(value = "字典下拉框返回VO")
public class DictVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "值")
    private String value;
}
