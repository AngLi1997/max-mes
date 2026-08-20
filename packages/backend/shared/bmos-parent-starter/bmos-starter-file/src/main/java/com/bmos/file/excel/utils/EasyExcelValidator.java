package com.bmos.file.excel.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class EasyExcelValidator {
    public void fillTitles(Map<Integer, String> headMap, List<String> keyList) {
        List<String> rowKeyList = new ArrayList<>(headMap.size());

        //合并表头
        //遍历获取第一行和第二行表头，存入keyList
        boolean isFirstRow = CollectionUtil.isEmpty(keyList);
        Set<Integer> integerSet = headMap.keySet();
        for (int i = 0; i < integerSet.size() && (isFirstRow || i < keyList.size()); i++) {
            rowKeyList.add(getKey(headMap, i));
        }
        for (int i = 0; i < rowKeyList.size(); i++) {
            if (isFirstRow) {
                keyList.add(rowKeyList.get(i) == null ? "" : rowKeyList.get(i));
            } else {
                String tempKey = keyList.get(i);
                String[] split = tempKey.split(StrUtil.EMPTY_JSON);
                String lastStr = split[split.length - 1];
                if (!Objects.equals(rowKeyList.get(i), lastStr)) {
                    keyList.set(i, String.format("%s%s%s", tempKey, StrUtil.EMPTY_JSON, rowKeyList.get(i)));
                }
            }
        }
    }

    private String getKey(Map<Integer, String> headMap, int i) {
        String key = headMap.get(i);
        if (StrUtil.isEmpty(key)) {
            key = headMap.get(i - 1);
            headMap.put(i, key);
        }
        return key;
    }


}
