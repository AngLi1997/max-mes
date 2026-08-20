package com.bmos.platform.service.equipment.service.dto;

import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yigaohui
 * @date 2024/4/22
 **/
@Data
@Accessors(chain = true)
public class EquipmentAcquisitionDataPointDTO extends EquipmentAcquisitionDTO {
    /**
     * 采集点名称，用这个值和scada系统关联
     */
    private String dataPointName;

    /**
     * 数据类型
     */
    private AcquisitionPointDataTypeEnum dataType;

    /**
     * 状态
     */
    private AcquisitionPointStatusEnum status;
}
