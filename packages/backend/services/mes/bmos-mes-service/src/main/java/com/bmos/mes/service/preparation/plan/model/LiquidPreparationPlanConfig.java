package com.bmos.mes.service.preparation.plan.model;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import lombok.Data;

import java.util.List;

@Data
public class LiquidPreparationPlanConfig {

    /**
     * 产出中间品配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 目标体积
     */
    private String targetVolume;

    /**
     * 配液物料
     */
    private List<LiquidPreparationPlanMaterial> materialList;

    public void validateConfig() {
        if (formulaMaterialId == null || StrUtil.isEmpty(targetVolume) || CollUtil.isEmpty(materialList)){
            throw new BmosException(MesResponseCode.PREPARATION_COMPONENT_CONFIG_NOT_ENOUGH);
        }
    }

}
