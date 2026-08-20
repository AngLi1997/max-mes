package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("获取tag下所有内置属性入参")
public class TagPropertyDTO {

    @ApiModelProperty("标签id集合")
    private List<Long> idList;

}
