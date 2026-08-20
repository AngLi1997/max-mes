package com.bmos.platform.service.unit.vo;

import com.bmos.expression.enums.RoundingEnum;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共单位信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 10:47
 */
@ApiModel(value = "公共单位")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonUnitVO {

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
     * <p>
     * （扩展单位数值 x 单位换算率 = 标准单位数值）
     */
    private String rate;

    /**
     * 是否为扩展单位
     */
    private Boolean extend;

    /**
     * 修约规则
     */
    private RoundingEnum rounding;

    private String roundingStr;

    public void setRoundingStr(String roundingStr) {
        this.roundingStr = roundingStr;
        this.rounding = RoundingEnum.getEnumByCode(roundingStr);
    }

    /**
     * 精度
     */
    private Long precision;

    /**
     * 是否启用
     */
    private Boolean enabled;

    public String getRate() {
        return rate == null ? "1" : rate;
    }
}
