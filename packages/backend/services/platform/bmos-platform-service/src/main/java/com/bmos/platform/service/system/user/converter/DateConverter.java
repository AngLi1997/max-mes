package com.bmos.platform.service.system.user.converter;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.WorkBookUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Excel-日期转换器
 *
 */
public class DateConverter implements Converter<String> {

    public static String pattern = "yyyy-MM-dd";
    private static SimpleDateFormat ldf = new SimpleDateFormat(pattern);
    public DateConverter() {
    }

    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.DATE;
    }

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {

        if (value != null) {
            try {
                //通过SimpleDateFormat 定义的格式转化器将字符串类型的日期转化成Date类型的变量
                Date date = ldf.parse(value);
                WriteCellData<?> cellData = new WriteCellData<>(date);
                //此处可以通过修改pattern来自定义导出后excel外显的日期格式
                WorkBookUtil.fillDataFormat(cellData, null, pattern);
                return cellData;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

