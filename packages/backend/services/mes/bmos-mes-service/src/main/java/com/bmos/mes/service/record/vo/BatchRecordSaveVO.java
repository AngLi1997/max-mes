package com.bmos.mes.service.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("记录保存VO")
@AllArgsConstructor
public class BatchRecordSaveVO {

    @ApiModelProperty("记录id")
    private Long recordId;

    @ApiModelProperty("记录版本id")
    private Long recordVersionId;

}
