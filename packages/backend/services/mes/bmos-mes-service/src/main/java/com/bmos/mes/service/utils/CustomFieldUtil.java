package com.bmos.mes.service.utils;

import com.bmos.mes.common.model.component.ComponentDetail;
import com.bmos.mes.common.model.component.CustomFieldDetailInfo;

import java.util.*;
import java.util.stream.Collectors;

public class CustomFieldUtil {

    private static final ThreadLocal<Map<Long, Map<String, CustomFieldDetailInfo>>> valueMap =
            new ThreadLocal<>();

    /**
     * 获取自定义字段值
     *
     * @param list            自定义字段列表
     * @param componentDetail 自定义组件详情
     * @param queryId         查询id (物料id或物料批次id)
     * @return
     */
    public static String getFieldValue(List<CustomFieldDetailInfo> list, ComponentDetail componentDetail,
                                       Long queryId) {
        Map<Long,  Map<String, CustomFieldDetailInfo>> fieldMap = getValueMap(list);
        Map<String, CustomFieldDetailInfo> stringCustomFieldDetailInfoMap = fieldMap.get(queryId);
        if (Objects.isNull(stringCustomFieldDetailInfoMap)){
            return null;
        }
        return Optional.ofNullable(stringCustomFieldDetailInfoMap.get(componentDetail.getFieldData()))
                .map(CustomFieldDetailInfo::getFieldValue)
                .orElse(null);
    }

    private static Map<Long,  Map<String, CustomFieldDetailInfo>> getValueMap(List<CustomFieldDetailInfo> list) {
        Map<Long,  Map<String, CustomFieldDetailInfo>> fieldMap = valueMap.get();
        if (fieldMap == null) {
            Map<Long,  Map<String, CustomFieldDetailInfo>> map = new HashMap<>();
            for (CustomFieldDetailInfo customFieldDetailInfo : list) {
                if (map.containsKey(customFieldDetailInfo.getKeyId())){
                    map.get(customFieldDetailInfo.getKeyId()).put(customFieldDetailInfo.getField(), customFieldDetailInfo);
                } else {
                    Map<String, CustomFieldDetailInfo> hashMap = new HashMap<>();
                    hashMap.put(customFieldDetailInfo.getField(), customFieldDetailInfo);
                    map.put(customFieldDetailInfo.getKeyId(), hashMap);
                }
            }
            valueMap.set(map);
            return valueMap.get();
        }
        return fieldMap;
    }

}
