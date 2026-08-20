package com.bmos.platform.facade.notify.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName AuditMessage
 * @Description TODO
 * @Author Ren Jin Guang
 * @Date 2025/1/9 14:33
 */
@Setter
@Getter
@ToString
public class AuditMessage {

    @ApiModelProperty("发起人名称")
    private String auditUser;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("审批意见")
    private String auditContent;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("业务主体")
    private String businessText;

    @ApiModelProperty("时间")
    private LocalDateTime time;

    @ApiModelProperty("接收用户")
    private List<String> notifyUserIds;

    @ApiModelProperty("是否是发起审核")
    private Boolean isStart;
}
