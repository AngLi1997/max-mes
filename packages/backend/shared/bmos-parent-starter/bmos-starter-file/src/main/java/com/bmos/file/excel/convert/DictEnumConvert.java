package com.bmos.file.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Excel
 */
@Slf4j
public class DictEnumConvert implements Converter<Object> {
    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        // 使用字典解析
        Class<KeyValueEnum> type = getEnumClassType(contentProperty);
        return Arrays.stream(type.getEnumConstants())
            .filter(codeEnum -> Objects.equals(codeEnum.getName(), cellData.getStringValue()))
            .findFirst()
            .orElse(null);
    }

    @Override
    public WriteCellData<String> convertToExcelData(Object object, ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration) {
        // 使用字典格式化
        return new WriteCellData<>(
            Optional.ofNullable(object).map(value -> ((KeyValueEnum)value)).map(KeyValueEnum::getName).orElse("")
        );
    }

    private static Class getEnumClassType(ExcelContentProperty contentProperty) {
        return contentProperty.getField().getType();
    }
}
