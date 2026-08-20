package com.bmos.mes.service.audit.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("审批历史DTO")
public class FlowAuditHistoryDTO {

    @NotBlank
    String processInstanceId;

    private String deploymentId;

    private boolean asc;

}
