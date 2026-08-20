package com.bmos.platform.service.system.user.converter;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.platform.service.system.user.enums.ActiveEnum;

import java.util.Objects;

/**
 * Excel-性别转换器
 *
 * @author renjinguang
 */
public class ActiveEnumConverter implements Converter<ActiveEnum> {
    @Override
    public ActiveEnum convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        for (ActiveEnum value : ActiveEnum.values()) {
            if (Objects.equals(I18nUtils.getEnumMessage(value), cellData.getStringValue())) {
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
    public WriteCellData<?> convertToExcelData(WriteConverterContext<ActiveEnum > context) {
        return new WriteCellData<String>(I18nUtils.getEnumMessage(context.getValue()));
    }
}