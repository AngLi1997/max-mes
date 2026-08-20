package com.bmos.mes.service.mcp.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 16:05
 */
@Data
public class MesStorageInventoryDataVO {

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
     * 质量状态
     */
    private String qualityStatus;

    /**
     * 可用量
     */
    private BigDecimal availableQuantity;

    public String getAvailableQuantity() {
        Long unitId = extendUnitId == null ? baseUnitId : extendUnitId;
        return PrecisionHelper.precision(SpringUtil.getBean(UnitCache.class).toExt(availableQuantity, unitId), unitId).toPlainString();
    }

    @JsonIgnore
    private Long baseUnitId;

    @JsonIgnore
    private Long extendUnitId;

    /**
     * 单位
     */
    public String getUnit() {
        Long unitId = extendUnitId == null ? baseUnitId : extendUnitId;
        if (unitId == null){
            return null;
        }
        return SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }

    public String getQualityStatus() {
        if (qualityStatus == null){
            return null;
        }
        return MaterialQualityStatusEnum.getEnumByValue(qualityStatus).getName();
    }
}
