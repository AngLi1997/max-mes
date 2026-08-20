package com.bmos.platform.service.signature.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.platform.common.enums.signature.SignatureActionEnum;

public class SignatureActionConverter implements Converter<SignatureActionEnum> {

    @Override
    public WriteCellData<?> convertToExcelData(SignatureActionEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(value.getName());
    }
}
