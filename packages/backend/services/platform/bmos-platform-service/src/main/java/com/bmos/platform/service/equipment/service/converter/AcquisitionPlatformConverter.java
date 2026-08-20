package com.bmos.platform.service.equipment.service.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.common.exporter.util.ExcelI18nUtil;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;

import java.util.Objects;

/**
 * @author yigaohui
 * @date 2024/4/25
 **/
public class AcquisitionPlatformConverter implements Converter<AcquisitionPlatformEnum> {
    @Override
    public AcquisitionPlatformEnum convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        for (AcquisitionPlatformEnum value : AcquisitionPlatformEnum.values()) {
            if (Objects.equals(ExcelI18nUtil.getI18n(value.getName()), cellData.getStringValue())) {
                return value;
            }
        }
        return null;
    }

    /**
     * 将 Java 类型转为 Excel 中填写的值
     *
     * @return
     */
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<AcquisitionPlatformEnum > context) {
        return new WriteCellData<String>(context.getValue().getName());
    }

}
