package com.bmos.mes.service.record.vo;

import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.model.BatchRecordParse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class RecordCopyVersionVO {

    @ApiModelProperty("记录项")
    private List<BatchRecordItem> items;

    @ApiModelProperty("html")
    private List<BatchRecordParse> parses;

    @ApiModelProperty(value = "组件")
    private List<BatchRecordComponent> components;
}
