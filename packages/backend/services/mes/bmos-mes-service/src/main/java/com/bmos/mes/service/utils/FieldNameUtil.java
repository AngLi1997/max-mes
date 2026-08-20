package com.bmos.mes.service.utils;

import com.bmos.mes.service.process.vo.ProcedureConfirmVO;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author renjinguang
 */
public class FieldNameUtil {

    public static <T> List<String> getFieldName(Class<T> t){
        List<String> list = new ArrayList<>();
        Field[] declaredFields = t.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            boolean annotationPresent = declaredField.isAnnotationPresent(ApiModelProperty.class);
            if (annotationPresent){
                String value = declaredField.getAnnotation(ApiModelProperty.class).value();
                list.add(value);
            }
        }
        return list;
    }
}
