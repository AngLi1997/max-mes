package com.bmos.mes.service.operate.dto.version;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "流程发起审核dto")
public class VersionStartFlowDTO {

    @ApiModelProperty(value = "版本id")
    @NotNull
    private Long versionId;

    @ApiModelProperty(value = "审核类型 ：true:启用，false:停用")
    @NotNull
    private Boolean auditType;

    @ApiModelProperty(value = "生效时间，以年月日传递")
    private String effectDate = "-";
}
