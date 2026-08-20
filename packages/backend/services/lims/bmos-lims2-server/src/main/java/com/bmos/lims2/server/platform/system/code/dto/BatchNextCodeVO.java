package com.bmos.lims2.server.platform.system.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("BatchNextCodeVO:返回下一个编码批量")
public class BatchNextCodeVO {
    @ApiModelProperty("规则编码")
    private String code;
    @ApiModelProperty("编号")
    private List<NextCodeVO> nos;
    @ApiModelProperty("时间")
    private LocalDate applyTime;
}
