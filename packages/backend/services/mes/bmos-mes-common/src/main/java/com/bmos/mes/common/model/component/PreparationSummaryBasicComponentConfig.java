package com.bmos.mes.common.model.component;

import lombok.Getter;
import lombok.Setter;

/**
 * 配料投入汇总组件配置
 */
@Getter
@Setter
public class PreparationSummaryBasicComponentConfig extends BasicComponentConfig {

    /**
     * 投料汇总配置的物料id
     */
    private Long formulaMaterialId;

}
