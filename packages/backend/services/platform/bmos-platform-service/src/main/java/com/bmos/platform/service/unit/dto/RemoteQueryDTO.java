package com.bmos.platform.service.unit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("查询单位及拓展单位DTO")
public class RemoteQueryDTO {

    @ApiModelProperty("单位ids")
    List<Long> unitIds;

    @ApiModelProperty("拓展单位ids")
    List<Long> unitExtendIds;

}


