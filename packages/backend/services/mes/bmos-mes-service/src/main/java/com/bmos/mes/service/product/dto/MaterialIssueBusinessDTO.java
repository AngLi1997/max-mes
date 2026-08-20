package com.bmos.mes.service.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("物料下发业务")
public class MaterialIssueBusinessDTO {

    @ApiModelProperty("业务平台名称")
    private String platformName;

    @ApiModelProperty("业务平台子业务码")
    private List<Integer> childCodeList;
}
