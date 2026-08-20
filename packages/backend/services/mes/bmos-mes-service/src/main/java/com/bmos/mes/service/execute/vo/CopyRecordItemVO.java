package com.bmos.mes.service.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("复制版本VO")
public class CopyRecordItemVO {

    @ApiModelProperty("版本号")
    private Long version;

    @ApiModelProperty("工序换班次数,默认0")
    private int procedureChangeNumber;

    @ApiModelProperty("工艺换班次数,默认0")
    private int processChangeNumber;

    @ApiModelProperty("是否已作废")
    private Boolean discard;
}
