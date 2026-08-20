package com.bmos.platform.service.equipment.service.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.bmos.platform.service.equipment.service.converter.AcquisitionPointDataTypeConverter;
import com.bmos.platform.service.equipment.service.converter.AcquisitionPointTypeConverter;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointTypeEnum;
import lombok.Data;

/**
 * @author yigaohui
 * @date 2024/4/20
 **/
@Data
public class AcquisitionPointExportDTO {

    @ExcelProperty(value = "采集点编码", index = 0)
    private String code;
    /**
     * 名称
     */
    @ExcelProperty(value = "采集点名称", index = 1)
    private String name;
    /**
     * 采集点名称，用这个值和scada系统关联
     */
    @ExcelProperty(value = "数据点位名称", index = 2)
    private String dataPointName;

    @ExcelProperty(value = "采集点类型", index = 3, converter = AcquisitionPointTypeConverter.class)
    private AcquisitionPointTypeEnum type;

    /**
     * 数据类型
     */
    @ExcelProperty(value = "数据类型", index = 4, converter = AcquisitionPointDataTypeConverter.class)
    private AcquisitionPointDataTypeEnum dataType;
}
