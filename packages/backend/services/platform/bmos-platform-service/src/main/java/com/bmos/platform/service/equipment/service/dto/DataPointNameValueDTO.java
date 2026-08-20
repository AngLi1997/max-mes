package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author yigaohui
 * @date
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DataPointNameValueDTO extends DataPointDTO {

    @ApiModelProperty("采集项编码")
    private String acquisitionPointCode;

    @ApiModelProperty("采集项名称")
    private String dataPointName;

    @ApiModelProperty("点位的值")
    private String value;

    @ApiModelProperty("点位值时间戳")
    private Long timeStamp;
}
