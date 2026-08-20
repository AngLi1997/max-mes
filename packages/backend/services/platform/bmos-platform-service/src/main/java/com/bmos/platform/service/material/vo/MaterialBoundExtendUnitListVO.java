package com.bmos.platform.service.material.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("物料绑定拓展单位列表VO")
public class MaterialBoundExtendUnitListVO {

    @ApiModelProperty("拓展单位id")
    private Long id;

    @ApiModelProperty("拓展单位名称")
    private String extendUnitName;

    @ApiModelProperty("表达式")
    private String expression;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String expressionValue;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String unitName;

    @ApiModelProperty("单位id")
    private Long unitId;

}
