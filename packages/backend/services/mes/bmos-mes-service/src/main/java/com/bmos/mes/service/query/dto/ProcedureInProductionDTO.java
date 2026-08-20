package com.bmos.mes.service.query.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @className: ProcedureInProductionDTO
 * @author: yigaohui
 * @date: 2024/12/4 18:48
 * @Version: 1.0
 * @description:
 */

@Data
public class ProcedureInProductionDTO {
    private Long processId;

    private String processName;

    private Long procedureId;

    private String procedureName;

    private List<String> inProductionBatchNoList;
}
