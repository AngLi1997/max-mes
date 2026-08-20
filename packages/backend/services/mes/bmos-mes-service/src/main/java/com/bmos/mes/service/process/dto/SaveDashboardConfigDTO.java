package com.bmos.mes.service.process.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/31 17:44
 */
@Data
public class SaveDashboardConfigDTO {

    @NotNull
    private Long processId;

    @NotBlank
    private String processVersion;

    @NotEmpty
    @Valid
    private List<SaveDashboardConfigProcedureDTO> procedureList;
}
