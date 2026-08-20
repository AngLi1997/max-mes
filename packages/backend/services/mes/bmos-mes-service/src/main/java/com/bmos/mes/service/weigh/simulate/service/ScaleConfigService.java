package com.bmos.mes.service.weigh.simulate.service;

import com.bmos.mes.service.weigh.simulate.entity.ScaleConfig;

import java.math.BigDecimal;

/**
 * 模拟称重配置Service接口
 * 
 * @author system
 * @date 2025-01-26
 */
public interface ScaleConfigService {

    /**
     * 获取当前启用的称重配置
     * 
     * @return 称重配置
     */
    ScaleConfig getEnabledConfig();

    /**
     * 生成随机重量值
     * 
     * @return 随机重量值
     */
    BigDecimal generateRandomWeight();
} 