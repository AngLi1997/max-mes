package com.bmos.mes.service.weigh.centre.requirement.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 物料投入组件配置(前端定义)
 * @author liang
 * @version 1.0.0
 * @date 2024/7/8 16:17
 */
@Data
public class MaterialInputComponentConfig {

    /**
     * 显示工位列表
     */
    private List<String> stationShow;

    /**
     * 工位列表
     */
    private List<String> station;

    /**
     * 物料配置
     */
    private List<MaterialConfig> materialList;

    @Data
    public static final class MaterialConfig {

        /**
         * 配置key
         */
        private String key;

        /**
         * 物料来源 1 称量中心
         */
        private Integer source;

        /**
         * 称量中心id
         */
        private Long productionPreparationCenter;

        /**
         * 配方物料id
         */
        private Long formulaMaterialId;

        /**
         * 单位id
         */
        private Long unitId;

        /**
         * 单位名称
         */
        private String unitName;

        /**
         * 物料需求量
         */
        private BigDecimal demand;

        /**
         * 物料需求时间天数
         */
        private Integer requirementTime;

        /**
         * 需求失效时间天数
         */
        private Integer demandExpirationTime;
    }
}
