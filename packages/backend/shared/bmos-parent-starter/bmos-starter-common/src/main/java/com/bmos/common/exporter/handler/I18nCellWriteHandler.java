package com.bmos.common.exporter.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.bmos.common.exporter.util.ExcelI18nUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class I18nCellWriteHandler implements CellWriteHandler {

    @Override
    public void beforeCellCreate(CellWriteHandlerContext context) {
        if (!context.getHead()) {
            return;
        }
        final List<String> originHeadNames = context.getHeadData().getHeadNameList();
        if (CollUtil.isEmpty(originHeadNames)) {
            return;
        }
        List<String> newHeadNames = originHeadNames.stream()
                .map(this::getMessage)
                .collect(Collectors.toList());
        context.getHeadData().setHeadNameList(newHeadNames);
    }

    public String getMessage(String code) {
        return ExcelI18nUtil.getI18n(code);
    }
}
