package com.bmos.lims2.server.eln.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("记录信息及记录项名称id列表")
@Data
public class RecordInfoItemListVO {

    @ApiModelProperty("记录名称")
    private String recordName;

    @ApiModelProperty("记录编码")
    private String recordCode;

    @ApiModelProperty("记录项列表")
    private List<ItemBaseInfoVO> itemList;

}
