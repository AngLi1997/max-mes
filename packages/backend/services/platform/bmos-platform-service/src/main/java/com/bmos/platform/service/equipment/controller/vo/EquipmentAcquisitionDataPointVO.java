package com.bmos.platform.service.equipment.controller.vo;

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
public class EquipmentAcquisitionDataPointVO {
    /**
     * 采集点名称，用这个值和scada系统关联
     */
    private String dataPointName;

    /**
     * 采集点名称
     */
    private String name;

    /**
     * 采集点编码
     */
    private String code;

    /**
     * 类型
     */
    private String type;

    /**
     * 采集点id
     */
    private Long id;

    /**
     * 数据类型
     */
    private AcquisitionPointDataTypeEnum dataType;

    /**
     * 状态
     */
    private AcquisitionPointStatusEnum status;
}
