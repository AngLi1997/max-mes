package com.bmos.platform.service.equipment.service.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.bmos.common.convert.ExcelEnumConvert;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.service.equipment.service.converter.AcquisitionPlatformConverter;
import com.bmos.platform.service.equipment.service.converter.AcquisitionPointDataTypeConverter;
import com.bmos.platform.service.equipment.service.converter.AcquisitionPointTypeConverter;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointStatusEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointTypeEnum;
import lombok.Data;
import org.apache.poi.ss.usermodel.Font;

import java.time.LocalDateTime;

/**
 * @author yigaohui
 * @date 2024/4/20
 **/
@Data
public class AcquisitionPointDTO {

    @ExcelIgnore
    private Long id;


    @ExcelProperty(value = "采集点编码(必填)", index = 0)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String code;
    /**
     * 名称
     */
    @ExcelProperty(value = "采集点名称(必填)", index = 1)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String name;
    /**
     * 采集点名称，用这个值和scada系统关联
     */
    @ExcelProperty(value = "数据点位名称(必填)", index = 2)
    @HeadFontStyle(color = Font.COLOR_RED)
    private String dataPointName;

    @ExcelProperty(value = "采集点类型(必填)", index = 3, converter = AcquisitionPointTypeConverter.class)
    @HeadFontStyle(color = Font.COLOR_RED)
    private AcquisitionPointTypeEnum type;

    /**
     * 数据类型
     */
    @ExcelProperty(value = "数据类型(必填)", index = 4, converter = AcquisitionPointDataTypeConverter.class)
    @HeadFontStyle(color = Font.COLOR_RED)
    private AcquisitionPointDataTypeEnum dataType;


    @ExcelProperty(value = "采集点数采平台(必填)", index = 5, converter = AcquisitionPlatformConverter.class)
    @HeadFontStyle(color = Font.COLOR_RED)
    private AcquisitionPlatformEnum acquisitionPlatform;

    /**
     * 状态
     */
    @ExcelIgnore
    private AcquisitionPointStatusEnum status;


    /**
     * 描述
     */
    @ExcelProperty(value = "描述",index = 6)
    @HeadFontStyle
    private String description;


    /**
     * 设备数据code
     */
    @ExcelIgnore
    private String equipmentTagDataCode;

    @ExcelIgnore
    private LocalDateTime createTime;

    @ExcelIgnore
    private LocalDateTime updateTime;

    @ExcelIgnore
    private String updateBy;

    @ExcelIgnore
    private String createBy;

    @ExcelIgnore
    private Boolean deleted;
}
