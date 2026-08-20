package com.bmos.mes.service.process.vo;

import com.bmos.mes.service.utils.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@ToString
@ApiModel("工艺待办VO")
public class ProcessTodoPageVO {

    @ApiModelProperty("执行实例id")
    private String executionId;

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("流程定义id")
    private String deploymentId;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("发起时间")
    private LocalDateTime processStartTime;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("发起人")
    private String startBy;

    @ApiModelProperty("发起人名称")
    private String startByUsername;

    @ApiModelProperty("节点挂载属性")
    private Map<String,Object> payload;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("生效时间")
    private String effectDate;

    public String getStartByUsername() {
        return UserUtils.getUsername(startBy);
    }
}
