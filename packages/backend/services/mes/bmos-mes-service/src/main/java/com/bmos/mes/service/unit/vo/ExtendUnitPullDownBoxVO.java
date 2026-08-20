package com.bmos.mes.service.unit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Setter
@Getter
@ToString
@ApiModel(value = "拓展单位下拉框vo")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExtendUnitPullDownBoxVO {

    @ApiModelProperty("拓展单位名称")
    private String extendUnitName;

    @ApiModelProperty("拓展单位id")
    private Long id;

    @ApiModelProperty("表达式")
    private String expression;
}
