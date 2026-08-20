package com.bmos.platform.service.signature.converter;

import cn.hutool.core.util.BooleanUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.bmos.platform.common.enums.signature.SignatureActionEnum;

public class SignatureSuccessConverter implements Converter<Boolean> {

    @Override
    public WriteCellData<?> convertToExcelData(Boolean value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(BooleanUtil.isTrue(value) ? SUCCESS : FAILED);
    }

    private static final String SUCCESS = "成功";
    private static final String FAILED = "失败";
}
