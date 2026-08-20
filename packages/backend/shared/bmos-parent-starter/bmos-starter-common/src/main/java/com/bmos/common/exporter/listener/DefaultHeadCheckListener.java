package com.bmos.common.exporter.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.exporter.util.ExcelI18nUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 检查表格的表头是否与实体匹配
 *
 * @author : ldl
 * @version : 1.0
 */
@Slf4j
public class DefaultHeadCheckListener<T> extends AnalysisEventListener<T> {

    private Class<T> head;

    public DefaultHeadCheckListener(Class<T> head) {
        this.head = head;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        Map<Integer, String> templateHead = getHead(head);
        /*if (templateHead.size() != headMap.size()) {
            throw new BmosException(PlatformResponseCode.EXPORT_TEMPLATE_HEADER_ERROR);
        }*/
        for (Map.Entry<Integer, String> entry : templateHead.entrySet()) {
            String columnName = headMap.get(entry.getKey());
            if (StringUtils.isEmpty(columnName) || !columnName.equals(entry.getValue())) {
                throw new BmosException(BaseResponseCode.EXPORT_TEMPLATE_HEADER_ERROR);
            }
        }
    }

    private Map<Integer, String> getHead(Class<?> clazz) {
        Map<Integer, String> headMap = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            ExcelIgnore ignoreProperty = field.getAnnotation(ExcelIgnore.class);
            if (ignoreProperty != null) {
                continue;
            }
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            String headName = field.getName();
            String[] colName = excelProperty.value();
            int index = excelProperty.index();
            if (index < 0) {
                continue;
            }
            if (colName.length > 0 && StrUtil.isNotEmpty(colName[0])) {
                headName = ExcelI18nUtil.getI18n(colName[0]);
            }

            headMap.put(index, headName);
        }
        return headMap;
    }


    @Override
    public void invoke(T data, AnalysisContext context) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
