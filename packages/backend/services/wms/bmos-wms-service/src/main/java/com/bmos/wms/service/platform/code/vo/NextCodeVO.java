package com.bmos.wms.service.platform.code.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("NextCodeVO:返回下一个编码")
public class NextCodeVO {
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("编号")
    private String no;
    @ApiModelProperty("时间")
    private LocalDate applyTime;
}
