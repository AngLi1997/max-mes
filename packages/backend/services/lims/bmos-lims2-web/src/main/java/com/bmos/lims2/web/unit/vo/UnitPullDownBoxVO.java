package com.bmos.lims2.web.unit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Setter
@Getter
@ToString
@ApiModel(value = "单位下拉框vo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitPullDownBoxVO {

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("单位id")
    private Long unitId;
}
