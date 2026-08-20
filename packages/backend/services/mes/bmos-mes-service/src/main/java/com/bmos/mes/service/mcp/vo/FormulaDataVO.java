package com.bmos.mes.service.mcp.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.unit.service.UnitCache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 配方数据vo (mcp)
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 14:38
 */
@Data
public class FormulaDataVO {

    private String bomName;

    private String bomVersion;

    private String productName;

    private String productCode;

    private String batchQuantity;

    @JsonIgnore
    private Long batchUnitId;

    public String getBatchUnitName() {
        if (batchUnitId == null) {
            return null;
        }
        UnitCache unitCache = SpringUtil.getBean(UnitCache.class);
        return unitCache.getGlobalUnitName(batchUnitId);
    }
}
