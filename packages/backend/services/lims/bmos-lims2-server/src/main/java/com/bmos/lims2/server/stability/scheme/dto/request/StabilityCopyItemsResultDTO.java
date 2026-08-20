package com.bmos.lims2.server.stability.scheme.dto.request;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * copyItems 返回值：承载旧ID→新ID映射，供 copyPlans 修正引用
 */
@Data
public class StabilityCopyItemsResultDTO {

    /**
     * 检验项目配置ID映射：旧 itemConfigId → 新 itemConfigId
     */
    private Map<Long, Long> itemIdMap = new HashMap<>();

    /**
     * 分析项配置ID映射：旧 parameterConfigId → 新 parameterConfigId
     */
    private Map<Long, Long> paramIdMap = new HashMap<>();
}
