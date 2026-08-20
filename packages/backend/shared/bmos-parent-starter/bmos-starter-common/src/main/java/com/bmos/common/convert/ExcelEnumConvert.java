package com.bmos.common.convert;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.common.base.enums.CommonEnum;

import com.alibaba.excel.converters.Converter;
import com.bmos.common.util.i18n.I18nUtils;

public class ExcelEnumConvert  implements Converter<CommonEnum> {
    @Override
    public WriteCellData<?> convertToExcelData(CommonEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(I18nUtils.getEnumMessage(value));
    }
}
