package com.bmos.common.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.common.util.i18n.I18nUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class I18nTranslateConvert implements Converter<String> {
    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) throws Exception {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return new WriteCellData<>(I18nUtils.getMenuMessage(value, value, null,
                ((ServletRequestAttributes) requestAttributes).getRequest()));
    }
}
