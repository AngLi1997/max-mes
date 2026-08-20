package com.bmos.mes.service.workflow.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class BindDeploymentDTO {

    private String superDeploymentId;

    private String nodeId;

    private String currentDeploymentId;

}
