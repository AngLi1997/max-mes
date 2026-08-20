package com.bmos.common.exporter.listener;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author lizihao
 * @date 2022/9/19 11:26
 * 线程不安全
 */

@Slf4j
public class ReadCheckListener<T> extends AnalysisEventListener<T> {

    /**
     * 数据对应的属性bean，及加{@link ExcelProperty}注解的类
     */
    private T propertiesBean;

    /**
     * 数据列表
     */
    private final List<T> correctData;

    public ReadCheckListener(T propertiesBean, List<T> correctData) {
        this.propertiesBean = propertiesBean;
        this.correctData = correctData;
    }

    /**
     * 在这里进行模板的判断
     *
     * @param headMap 存放着导入表格的表头，键是索引，值是名称
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        /*
        count 记录模板表头有几个，用以判断用户导入的表格是否和模板完全一致
        如果用户导入表格较模板的表头多，但其余符合模板，这样不影响则不需要
         */
        int count = 0;
        // 获取数据实体的字段列表

        Field[] fields = propertiesBean.getClass().getDeclaredFields();
        // 遍历字段进行判断
        for (Field field : fields) {
            // 获取当前字段上的ExcelProperty注解信息
            ExcelProperty fieldAnnotation = field.getAnnotation(ExcelProperty.class);
            // 判断当前字段上是否存在ExcelProperty注解
            if (fieldAnnotation != null) {
                ++count;
                // 存在ExcelProperty注解则根据注解的index索引到表头中获取对应的表头名
                String headName = headMap.get(fieldAnnotation.index());
                // 判断表头是否为空或是否和当前字段设置的表头名不相同
                if (StringUtils.isEmpty(headName) || !headName.equals(fieldAnnotation.value()[0])) {
                    // 如果为空或不相同，则抛出异常不再往下执行
                    throw new BmosException(BaseResponseCode.EXPORT_TEMPLATE_ERROR);
                }
            }
        }

        // 判断用户导入表格的标题头是否完全符合模板
        if (count != headMap.size()) {
            throw new BmosException(BaseResponseCode.EXPORT_TEMPLATE_ERROR);
        }
    }

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        boolean b = checkObjAllFieldsIsNull(t);
        if (!b) {
            correctData.add(t);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


    /**
     * 简单判断对象的属性是否为空，对于list，数组等不做验证
     * 只能对属性为基础数据类型的包装类型、String类型的字段进行验证
     *
     * @param object
     * @return
     */
    public static boolean checkObjAllFieldsIsNull(Object object) {
        if (null == object) {
            return true;
        }
        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    return false;
                }

            }
        } catch (Exception e) {
            log.error("验证属性是否为空错误！", e);
        }

        return true;
    }
}
