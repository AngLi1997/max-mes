package com.bmos.file.excel.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.StringUtils;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class EasyExcelHeadListener<T> extends AnalysisEventListener<T> {
    private final EasyExcelValidator easyExcelValidator = new EasyExcelValidator();

    private final List<String> keyList = new ArrayList<String>();

    public Class<?> clazz;

    public EasyExcelHeadListener(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        log.info("解析到一条头数据:{}", JsonUtils.toJsonString(headMap));
        int approximateTotalRowNumber = context.readSheetHolder().getHeadRowNumber() - 1;
        easyExcelValidator.fillTitles(ConverterUtils.convertToStringMap(headMap, context), keyList);
        if (context.readRowHolder().getRowIndex() == approximateTotalRowNumber) {
            log.info("解析到一行表头{}", keyList);
            validatedLine(keyList.stream().filter(StrUtil::isNotEmpty).collect(Collectors.toSet()));
        }
    }

    // 校验单行
    private void validatedLine(Set<String> headNameSet) {
        int count = 0;
        // 获取数据实体的字段列表
        Field[] fields = clazz.getDeclaredFields();
        // 遍历字段进行判断
        for (Field field : fields) {
            // 获取当前字段上的ExcelProperty注解信息
            ExcelProperty fieldAnnotation = field.getAnnotation(ExcelProperty.class);
            // 判断当前字段上是否存在ExcelProperty注解
            if (fieldAnnotation != null) {
                ++count;
                // 存在ExcelProperty注解则根据注解的index索引到表头中获取对应的表头名
                String headName = String.join(StrUtil.EMPTY_JSON, fieldAnnotation.value());
                // 判断表头是否为空或是否和当前字段设置的表头名不相同
                if (StringUtils.isNotBlank(headName) && !headNameSet.contains(headName)) {
                    // 如果为空或不相同，则抛出异常不再往下执行
                    throw new BmosException(BaseResponseCode.EXCEL_TEMPLATE_ERROR);
                }
            }
        }

        // 判断用户导入表格的标题头是否完全符合模板
        if (count != headNameSet.size()) {
            throw new BmosException(BaseResponseCode.EXCEL_TEMPLATE_ERROR);
        }
    }

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
