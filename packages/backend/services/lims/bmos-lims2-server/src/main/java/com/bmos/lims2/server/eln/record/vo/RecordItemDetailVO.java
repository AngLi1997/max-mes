package com.bmos.lims2.server.eln.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "记录详情vo")
public class RecordItemDetailVO {

    @ApiModelProperty(value = "记录名称")
    private String recordName;

    @ApiModelProperty(value = "记录项集合")
    private List<RecordItemListVO> itemList;
}
