package com.bmos.lims2.server.inspect.division.dto;

import lombok.Data;

import java.math.BigDecimal;
import com.bmos.lims2.server.inspect.common.util.QuantityUtils;

/**
 * @className: SampleDivisionRemainDTO
 * @author: yigaohui
 * @date: 2025/8/19 13:58
 * @Version: 1.0
 * @description:
 */

@Data
public class SampleDivisionRemainDTO {
    private BigDecimal originalQuantity;
    private Long originalUnitId;
    private String originalUnitName;
    private BigDecimal dividedQuantity;
    private BigDecimal remainingQuantity;
}
