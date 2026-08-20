package com.bmos.mes.service.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "工序配置记录项返回vo")
public class ProcessRecordItemVO {

    @ApiModelProperty(value = "记录名称")
    private String recordName;

    @ApiModelProperty(value = "版本id")
    private Long versionId;

    @ApiModelProperty(value ="记录项集合")
    private List<RecordItemListVO> recordItemList;
}
