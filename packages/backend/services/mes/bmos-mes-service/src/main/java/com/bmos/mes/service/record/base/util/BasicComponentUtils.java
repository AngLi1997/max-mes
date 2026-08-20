package com.bmos.mes.service.record.base.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.bmos.mes.service.process.vo.ComponentConfigDetailVO;
import com.bmos.mes.service.record.base.contants.ComponentConfigFieldCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础组件工具类
 *
 * @author yigaohui
 * @date 2024/5/27
 **/
public final class BasicComponentUtils {


    /**
     * 从配置信息中获取工位id
     * 过滤出当前生产计划使用的产线的工位id
     * @param configs          配置信息
     * @param productionLineId 产线id
     * @return 组件id，工位id集合
     */
    public static Map<Long, List<Long>> getStations(List<ComponentConfigDetailVO> configs, Long productionLineId) {
        HashMap<Long, List<Long>> res = new HashMap<>();
        String lineStr = String.valueOf(productionLineId);
        configs.forEach(item -> {
            String configInfo = item.getConfigInfo();
            if (StrUtil.isEmpty(configInfo)) {
                return;
            }
            JSONObject config = new JSONObject(configInfo);
            JSONArray stationShowList = config.getJSONArray(ComponentConfigFieldCodes.STATION_SHOW);
            if (CollectionUtil.isEmpty(stationShowList)) {
                return;
            }
            List<String> list = stationShowList.toList(String.class);
            List<Long> stationIds = list.stream().filter(e -> e.startsWith(lineStr)).map(e -> {
                return Long.valueOf(CollUtil.getLast(StrUtil.split(e, StrUtil.DASHED)));
            }).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(stationIds)) {
                res.put(item.getId(), stationIds);
            }
        });
        return res;
    }

    /**
     * 过滤出配置中属于当前产线的工位id
     * @param stationShow
     * @param productionLineId
     * @return
     */
    public static List<Long> filterStations(List<String> stationShow, Long productionLineId) {
        if (CollUtil.isEmpty(stationShow) || productionLineId == null) {
            return new ArrayList<>();
        }
        String lineStr = String.valueOf(productionLineId);
        List<Long> stationIds = stationShow.stream().filter(e -> e.startsWith(lineStr)).map(e -> {
            return Long.valueOf(CollUtil.getLast(StrUtil.split(e, StrUtil.DASHED)));
        }).collect(Collectors.toList());
        return stationIds;
    }
}
