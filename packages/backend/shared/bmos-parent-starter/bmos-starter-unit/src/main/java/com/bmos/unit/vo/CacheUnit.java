package com.bmos.unit.vo;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.cache.redis.objects.CommonUnit;
import com.bmos.expression.enums.RoundingEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 公共单位信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 10:47
 */
@ApiModel(value = "单位")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CacheUnit {

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 父级单位id(扩展单位的标准单位)
     */
    private Long parentUnitId;

    /**
     * 单位换算率（标准单位的单位换算率默认为1）
     * （扩展单位数值 x 单位换算率 = 标准单位数值）
     */
    private BigDecimal rate;

    /**
     * 是否为扩展单位
     */
    private Boolean extend;

    /**
     * 修约规则
     */
    @JsonIgnore
    private RoundingEnum rounding;

    private String roundingStr;

    public void setRoundingStr(String roundingStr) {
        this.roundingStr = roundingStr;
        this.rounding = RoundingEnum.getEnumByCode(roundingStr);
    }

    public String getRoundingStr() {
        return rounding == null ? null : rounding.getCode();
    }

    /**
     * 精度(位)
     */
    private Long precision;

    /**
     * 是否启用
     */
    private Boolean enabled;

    public BigDecimal getRate() {
        return rate == null ? BigDecimal.ONE : rate;
    }

    public static CacheUnit from(CommonUnit source) {
        if (source == null) {
            return null;
        }
        CacheUnit cacheUnit = new CacheUnit();
        BeanUtil.copyProperties(source, cacheUnit);
        cacheUnit.setRounding(RoundingEnum.getEnumByCode(cacheUnit.getRoundingStr()));
        return cacheUnit;
    }
}
