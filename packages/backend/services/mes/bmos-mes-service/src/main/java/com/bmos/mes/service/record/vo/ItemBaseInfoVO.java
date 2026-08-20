package com.bmos.mes.service.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("记录项基础信息")
public class ItemBaseInfoVO {

    @ApiModelProperty(value = "记录项id")
    private Long id;

    @ApiModelProperty(value = "记录项业务id")
    private Long itemId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("排序")
    private Integer sort;

}