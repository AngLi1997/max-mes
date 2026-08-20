package com.bmos.mes.service.trace.material.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/22 14:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialTraceMaterialStepView {

    private Long id;

    private String procedureName;

    private String procedureStepName;

    private String processName;

    private String processVersion;

    private String materialNo;

    private BigDecimal quantity;

    private Long unitId;

    private String unit;

    private String batchNo;

    private Boolean calcFlag = true;
}
