package com.bmos.platform.service.log.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.logging.enums.OperationTypeEnum;

public class OperationTypeConverter implements Converter<OperationTypeEnum> {

    @Override
    public WriteCellData<?> convertToExcelData(OperationTypeEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(value.getName());
    }
}
