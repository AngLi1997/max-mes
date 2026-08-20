package com.bmos.lims2.server.eln.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("工艺批记录列表VO")
public class ProcessRecordListVO {

    @ApiModelProperty("记录id")
    private Long recordId;

    @ApiModelProperty("版本列表")
    private List<SelectRecorVO> versionList;

}
