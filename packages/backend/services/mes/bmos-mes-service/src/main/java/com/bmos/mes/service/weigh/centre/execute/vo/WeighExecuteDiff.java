package com.bmos.mes.service.weigh.centre.execute.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 称量执行允差信息
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 17:22
 */
@Data
@ApiModel("称量执行允差信息")
public class WeighExecuteDiff {

    /**
     * 允差类型
     */
    @ApiModelEnumProperty(value = "配料允差类型", enumClass = ToleranceTypeEnum.class)
    @EnumValidate(ToleranceTypeEnum.class)
    private ToleranceTypeEnum toleranceTypeEnum;

    /**
     * 允差上限
     */
    @ApiModelProperty(value = "配料允差上限", example = "2")
    private BigDecimal maxTolerance = BigDecimal.ZERO;

    /**
     * 允差下限
     */
    @ApiModelProperty(value = "配料允差下限", example = "2")
    private BigDecimal minTolerance = BigDecimal.ZERO;

    /**
     * 允差范围 【允差下限，标准，允差上限】
     */
    @ApiModelProperty(value = "允差范围 【允差下限，标准，允差上限】")
    public BigDecimal[] toleranceDiff = new BigDecimal[3];

    /**
     * 允差统一减值
     * @param value
     */
    public void diffSubtract(BigDecimal value) {
        for (int i = 0; i < toleranceDiff.length; i++) {
            if (toleranceDiff[i] == null){
                continue;
            }
            toleranceDiff[i] = toleranceDiff[i].subtract(value);
            if (toleranceDiff[i].compareTo(BigDecimal.ZERO) < 0){
                toleranceDiff[i] = BigDecimal.ZERO;
            }
        }
    }
}
