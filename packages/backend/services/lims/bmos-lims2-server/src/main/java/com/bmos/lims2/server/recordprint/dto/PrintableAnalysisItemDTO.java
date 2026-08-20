package com.bmos.lims2.server.recordprint.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 可打印分析项列表DTO（仅复核通过）
 * @Author: yigaohui
 * @Date: 2025/11/25 10:20
 */
@Getter
@Setter
public class PrintableAnalysisItemDTO {

    /**
     * 分析项ID（lm_inspection_scheme_parameter.id）
     */
    private Long parameterId;

    /**
     * 分析项编码
     */
    private String parameterCode;

    /**
     * 分析项名称
     */
    private String parameterName;

    /**
     * 任务ID（复核通过的任务）
     */
    private Long taskId;

    /**
     * 分析项与记录绑定ID（lm_inspect_parameter_record.id）
     */
    private Long parameterRecordId;
}


