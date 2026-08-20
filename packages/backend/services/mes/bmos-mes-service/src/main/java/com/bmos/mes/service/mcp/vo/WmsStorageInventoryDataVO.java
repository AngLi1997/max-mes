package com.bmos.mes.service.mcp.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 16:05
 */
@Data
public class WmsStorageInventoryDataVO {

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料批次
     */
    private String materialBatchNo;

    /**
     * 有效期至
     */
    private String expiredDate;

    /**
     * 可用量
     */
    private BigDecimal availableQuantity;


    public String getAvailableQuantity() {
        UnitCache cache = SpringUtil.getBean(UnitCache.class);
        CacheUnit globalUnit = cache.getGlobalUnit(unitId);
        if (globalUnit == null){
            return null;
        }
        return PrecisionHelper.precision(cache.toExt(availableQuantity, unitId), unitId).toPlainString();
    }

    @JsonIgnore
    private Long unitId;

    /**
     * 单位
     */
    public String getUnit() {
        if (unitId == null){
            return null;
        }
        return SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }
}
