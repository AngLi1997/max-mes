package com.bmos.file.excel.convert;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Excel
 */
@Slf4j
public class LocalDateConvert implements Converter<LocalDate> {
    @Override
    public LocalDate convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String stringValue = cellData.getStringValue();
        if (StrUtil.isEmpty(stringValue)) {
            return null;
        }
        return LocalDateTimeUtil.parseDate(stringValue);
    }

    @Override
    public WriteCellData<String> convertToExcelData(LocalDate object, ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration) {
        if (Objects.isNull(object)) {
            return new WriteCellData<>("");
        }
        return new WriteCellData<>(LocalDateTimeUtil.formatNormal(object));
    }
}
