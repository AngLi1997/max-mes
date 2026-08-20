package com.bmos.mes.service.equipment.vo;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.bmos.mes.service.equipment.service.enums.EquipmentAcquisitionComponentInputTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yigaohui
 * @date 2024/4/24
 **/
@Data
@ApiModel("采集项点位数据")
public class EquipmentAcquisitionPointVO {

    @ApiModelProperty("采集项id")
    private Long acquisitionId;

    @ApiModelProperty("采集项code")
    private String acquisitionCode;

    @ApiModelProperty("数据点位名称")
    private String dataPointName;

    @ApiModelProperty("数据点位值")
    private String dataPointValue;

    @ApiModelProperty("设备数据编码")
    private String dataPropertyCode;

    @ApiModelProperty("数据点位值时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataPointValueTime;

    @ApiModelEnumProperty(value = "录入类型", enumClass = EquipmentAcquisitionComponentInputTypeEnum.class)
    private EquipmentAcquisitionComponentInputTypeEnum inputType;
}
