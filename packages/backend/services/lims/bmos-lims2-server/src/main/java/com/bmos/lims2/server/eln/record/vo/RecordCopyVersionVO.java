package com.bmos.lims2.server.eln.record.vo;

import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.entity.BatchRecordItem;
import com.bmos.lims2.server.eln.record.entity.BatchRecordParse;
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
