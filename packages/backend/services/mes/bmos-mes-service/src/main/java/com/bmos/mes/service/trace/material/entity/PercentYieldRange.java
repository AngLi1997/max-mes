package com.bmos.mes.service.trace.material.entity;

import com.bmos.mes.common.enums.record.NumberCompareResultEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 收率范围
 * @author liang
 * @version 1.0.0
 * @date 2024/11/20 18:20
 */
@Data
@ApiModel("收率范围")
public class PercentYieldRange {

    @ApiModelEnumProperty(value = "收率范围上限符号", enumClass = NumberCompareResultEnum.class)
    private String upperSymbol;

    @ApiModelProperty(value = "收率范围上限值", example = "1")
    private BigDecimal upperValue;

    @ApiModelEnumProperty(value = "收率范围下限符号", enumClass = NumberCompareResultEnum.class)
    private String lowerSymbol;

    @ApiModelProperty(value = "收率范围下限值", example = "1")
    private BigDecimal lowerValue;
}
