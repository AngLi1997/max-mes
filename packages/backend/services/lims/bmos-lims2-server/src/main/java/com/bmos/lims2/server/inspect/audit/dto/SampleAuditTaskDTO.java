package com.bmos.lims2.server.inspect.audit.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@ApiModel("样品审核待办任务")
public class SampleAuditTaskDTO {

    @ApiModelProperty("执行实例id")
    private String executionId;

    @ApiModelProperty("流程定义id")
    private String deploymentId;

    @ApiModelProperty("节点挂载属性")
    private Map<String,Object> payload;

    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty("流程实例ID")
    private String processInstanceId;

    @ApiModelProperty("审批节点名称")
    private String currentNodeName;

    @ApiModelProperty("发起人")
    private String initiator;

    @ApiModelProperty("发起时间")
    private LocalDateTime initiateTime;

    @ApiModelProperty("检验单ID")
    private Long id;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String createBy;
    @ApiModelProperty("请验人")
    private String requestUserId;

    @ApiModelProperty("请验人名称")
    private String requestUserName;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    public String getRequestUserName() {
        if (requestUserId == null) {
            return null;
        }
        if ("system".equalsIgnoreCase(requestUserId)) {
            return "system";
        }
        return UserUtils.getUserDisplayName(requestUserId);
    }

}


