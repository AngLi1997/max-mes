package com.bmos.mes.service.log.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.logging.enums.OperationTypeEnum;

public class OperationTypeConverter implements Converter<OperationTypeEnum> {

    @Override
    public WriteCellData<?> convertToExcelData(OperationTypeEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(I18nUtils.getEnumMessage(value));
    }
}
