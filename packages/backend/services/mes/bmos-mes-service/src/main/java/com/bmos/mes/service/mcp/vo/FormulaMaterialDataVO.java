package com.bmos.mes.service.mcp.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.common.enums.formula.QuantityTypeEnum;
import com.bmos.unit.service.UnitCache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 16:21
 */
@Data
public class FormulaMaterialDataVO {

    private String materialName;

    private String materialCode;

    private String quantityType;

    private String quantity;

    @JsonIgnore
    private Long unitId;

    public String getUnit() {
        if (unitId == null){
            return null;
        }
        return SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }

    public String getQuantityType() {
        if (quantityType == null){
            return null;
        }
        return QuantityTypeEnum.getEnumByValue(Integer.valueOf(quantityType)).getName();
    }
}
