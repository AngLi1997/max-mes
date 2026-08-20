package com.bmos.file.excel.convert;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Excel
 */
@Slf4j
public class BigDecimalConvert implements Converter<BigDecimal> {
    @Override
    public BigDecimal convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String stringValue = cellData.getStringValue();
        if (StrUtil.isEmpty(stringValue)) {
            return null;
        }
        return new BigDecimal(stringValue);
    }

    @Override
    public WriteCellData<String> convertToExcelData(BigDecimal object, ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration) {
        if (Objects.isNull(object)) {
            return new WriteCellData<>("");
        }
        return new WriteCellData<>(object.setScale(2, RoundingMode.HALF_DOWN) + "");
    }
}
