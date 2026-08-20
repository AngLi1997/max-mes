package com.bmos.mes.service.unit.vo;

import com.bmos.expression.enums.RoundingEnum;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 公共单位信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 10:47
 */
@Getter
@Setter
@ToString
@ApiModel(value = "公共单位")
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

    /**
     * 修约规则字符串
     */
    private String roundingStr;

    public void setRoundingStr(String roundingStr) {
        this.rounding = RoundingEnum.getEnumByCode(roundingStr);
    }

    public String getRoundingStr() {
        return rounding == null ? null : rounding.getCode();
    }

    /**
     * 精度
     */
    private Long precision;
}
