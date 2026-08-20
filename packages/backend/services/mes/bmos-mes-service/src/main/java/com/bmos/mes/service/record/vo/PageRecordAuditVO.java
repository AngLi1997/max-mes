package com.bmos.mes.service.record.vo;

import com.bmos.mes.service.utils.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value ="记录审核返回vo")
public class PageRecordAuditVO {

    @ApiModelProperty(value = "记录名称")
    private String name;

    @ApiModelProperty(value = "记录版本id")
    private Long versionId;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "节点id")
    private String nodeId;

    @ApiModelProperty(value = "发起人id")
    private String processStartBy;

    @ApiModelProperty(value = "发起人名称")
    private String processStartByName;

    @ApiModelProperty(value = "发起时间")
    private LocalDateTime processStartTime;

    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "流程启动id")
    private String deploymentId;

    @ApiModelProperty(value = "流程运行id")
    private String executionId;

    @ApiModelProperty(value = "业务参数")
    private Map<String,Object> payload;

    public String getProcessStartByName(){
        return UserUtils.getUsername(processStartBy);
    }
}
