package com.bmos.mes.service.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(value = "复制版本VO对象")
public class RecordVersionVO {

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "版本id")
    private String versionId;
}
