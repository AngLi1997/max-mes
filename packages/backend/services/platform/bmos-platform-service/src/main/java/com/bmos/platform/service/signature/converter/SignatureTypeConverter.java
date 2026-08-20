package com.bmos.platform.service.signature.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.platform.common.enums.signature.SignatureActionEnum;
import com.bmos.platform.common.enums.signature.SignatureTypeEnum;

public class SignatureTypeConverter implements Converter<SignatureTypeEnum> {

    @Override
    public WriteCellData<?> convertToExcelData(SignatureTypeEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(value.getName());
    }
}
