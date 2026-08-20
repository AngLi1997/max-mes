package com.bmos.platform.service.system.user.converter;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.common.exporter.util.ExcelI18nUtil;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.platform.service.system.user.enums.GenderEnum;

import java.util.Objects;

/**
 * Excel-性别转换器
 *
 */
public class GenderEnumConverter implements Converter<GenderEnum> {
    @Override
    public GenderEnum convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        for (GenderEnum value : GenderEnum.values()) {
            if (Objects.equals(ExcelI18nUtil.getI18n(value.getName()), cellData.getStringValue())) {
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
    public WriteCellData<?> convertToExcelData(WriteConverterContext<GenderEnum > context) {
        return new WriteCellData<String>(I18nUtils.getEnumMessage(context.getValue()));
    }
}