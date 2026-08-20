package com.bmos.mes.service.audit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "流程详情vo")
public class FlowAuditDetailVO {

    @ApiModelProperty(value = "流程定义id")
    private String deploymentId;

    @ApiModelProperty(value = "流程id")
    private Long flowAuditId;

    @ApiModelProperty(value = "版本id")
    private Long versionId;

    @ApiModelProperty(value = "流程编码")
    private String code;

    @ApiModelProperty(value = "流程名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "流程模型")
    private String flowAuditModel;

    @ApiModelProperty(value = "分类编码")
    private String categoryCode;

    @ApiModelProperty(value = "审核人员集合")
    private List<FlowAuditUserVO> auditUserList;

    @ApiModelProperty(value = "消息通知人员集合")
    private List<FlowAuditMegVO> auditMegDTOList;
}
