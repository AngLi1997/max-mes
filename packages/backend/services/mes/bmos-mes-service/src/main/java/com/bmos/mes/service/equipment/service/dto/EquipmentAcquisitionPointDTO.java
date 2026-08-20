package com.bmos.mes.service.equipment.service.dto;

import com.bmos.mes.service.equipment.service.enums.EquipmentAcquisitionComponentInputTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yigaohui
 * @date 2024/4/24
 **/
@Data
public class EquipmentAcquisitionPointDTO {
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
    private LocalDateTime dataPointValueTime;

    @ApiModelEnumProperty(value = "录入类型", enumClass = EquipmentAcquisitionComponentInputTypeEnum.class)
    private EquipmentAcquisitionComponentInputTypeEnum inputType;
}
