package com.bmos.mes.service.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "流程校验dto")
public class FlowCheckoutDTO {

    @ApiModelProperty(value = "流程模型")
    @NotBlank
    private String flowAuditModel;

    @ApiModelProperty(value = "人员信息")
    private List<CheckoutFlowAuditUserDTO> userList;

    @ApiModelProperty(value = "消息人员")
    private List<CheckoutFlowAuditMegDTO> megUserList;
}
