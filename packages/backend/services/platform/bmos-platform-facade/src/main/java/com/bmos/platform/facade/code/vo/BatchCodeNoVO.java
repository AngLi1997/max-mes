package com.bmos.platform.facade.code.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("BatchCodeNoVO:返回下一个编码批量")
public class BatchCodeNoVO {
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("编号")
    private List<String> nos;
    @ApiModelProperty("编号规则不存在")
    private Boolean ruleNotExist;
}
