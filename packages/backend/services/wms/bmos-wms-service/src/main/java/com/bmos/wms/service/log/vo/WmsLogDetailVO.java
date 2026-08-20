package com.bmos.wms.service.log.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("Wms操作日志VO")
public class WmsLogDetailVO extends WmsLogPageVO {

    @ApiModelProperty("操作对象")
    private String operationObject;

}
