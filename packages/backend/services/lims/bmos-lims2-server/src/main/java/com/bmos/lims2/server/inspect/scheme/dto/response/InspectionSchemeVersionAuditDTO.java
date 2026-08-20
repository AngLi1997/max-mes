package com.bmos.lims2.server.inspect.scheme.dto.response;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 检验方案版本审批响应DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Getter
@Setter
@ApiModel("检验方案版本审批响应DTO")
public class InspectionSchemeVersionAuditDTO {

    @ApiModelProperty("执行实例id")
    private String executionId;

    @ApiModelProperty("流程定义id")
    private String deploymentId;

    @ApiModelProperty("节点挂载属性")
    private Map<String,Object> payload;


    @ApiModelProperty("版本ID")
    private Long id;

    @ApiModelProperty("检验方案名称")
    private String schemeName;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("审批节点名称")
    private String currentNodeName;

    @ApiModelProperty("发起人")
    private String initiator;

    @ApiModelProperty("发起人名称")
    private String initiatorName;

    @ApiModelProperty("发起时间")
    private LocalDateTime initiateTime;

    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty("流程实例ID")
    private String processInstanceId;

    public String getInitiatorName(){
        return UserUtils.getUserDisplayName(initiator);
    }
}