package com.bmos.mes.service.weigh.simulate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 模拟称重配置实体类
 * 
 * @author system
 * @date 2025-01-26
 */
@Data
@TableName("mes_scale_config")
public class ScaleConfig {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 最小随机数值
     */
    private BigDecimal minValue;

    /**
     * 最大随机数值
     */
    private BigDecimal maxValue;

    /**
     * 小数位数
     */
    private Integer decimalPlaces;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
} 