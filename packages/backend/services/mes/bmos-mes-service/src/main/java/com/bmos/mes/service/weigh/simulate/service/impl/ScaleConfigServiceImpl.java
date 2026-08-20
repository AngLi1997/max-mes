package com.bmos.mes.service.weigh.simulate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.weigh.simulate.entity.ScaleConfig;
import com.bmos.mes.service.weigh.simulate.mapper.ScaleConfigMapper;
import com.bmos.mes.service.weigh.simulate.service.ScaleConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * 模拟称重配置Service实现类
 * 
 * @author system
 * @date 2025-01-26
 */
@Service
@Slf4j
public class ScaleConfigServiceImpl implements ScaleConfigService {

    @Autowired
    private ScaleConfigMapper scaleConfigMapper;

    private final Random random = new Random();

    @Override
    public ScaleConfig getEnabledConfig() {
        LambdaQueryWrapper<ScaleConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScaleConfig::getEnabled, true);
        queryWrapper.orderByDesc(ScaleConfig::getUpdateTime);
        queryWrapper.last("LIMIT 1");
        
        ScaleConfig config = scaleConfigMapper.selectOne(queryWrapper);
        
        // 如果没有配置，返回默认配置
        if (config == null) {
            config = getDefaultConfig();
        }
        
        return config;
    }

    @Override
    public BigDecimal generateRandomWeight() {
        ScaleConfig config = getEnabledConfig();
        
        BigDecimal minValue = config.getMinValue();
        BigDecimal maxValue = config.getMaxValue();
        Integer decimalPlaces = config.getDecimalPlaces();
        
        // 生成范围内的随机数
        double randomDouble = minValue.doubleValue() + 
            (maxValue.doubleValue() - minValue.doubleValue()) * random.nextDouble();
        
        BigDecimal randomValue = BigDecimal.valueOf(randomDouble);
        
        // 设置小数位数
        return randomValue.setScale(decimalPlaces, RoundingMode.HALF_UP);
    }

    /**
     * 获取默认配置
     * 
     * @return 默认配置
     */
    private ScaleConfig getDefaultConfig() {
        ScaleConfig defaultConfig = new ScaleConfig();
        defaultConfig.setConfigName("默认配置");
        defaultConfig.setMinValue(new BigDecimal("0.001"));
        defaultConfig.setMaxValue(new BigDecimal("999.999"));
        defaultConfig.setDecimalPlaces(3);
        defaultConfig.setEnabled(true);
        defaultConfig.setRemark("系统默认配置");
        
        return defaultConfig;
    }
} 