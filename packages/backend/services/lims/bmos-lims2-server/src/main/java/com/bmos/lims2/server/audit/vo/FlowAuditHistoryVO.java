package com.bmos.lims2.server.audit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(value = "流程审批进度vo")
public class FlowAuditHistoryVO {

    @ApiModelProperty(value = "流程图数据")
    private String metaInfo;

    @ApiModelProperty(value = "节点状态")
    private List<AuditNodeStateVO> nodeStateList;

    @ApiModelProperty(value = "节点信息")
    private List<FlowAuditNodeVO> nodeList;
}
