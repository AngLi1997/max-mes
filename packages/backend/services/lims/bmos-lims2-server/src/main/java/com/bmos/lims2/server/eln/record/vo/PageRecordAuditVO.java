package com.bmos.lims2.server.eln.record.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
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

    @ApiModelProperty(value = "方法编码")
    private String code;

    @ApiModelProperty(value = "记录id")
    private Long recordId;

    @ApiModelProperty(value = "记录版本id")
    private Long versionId;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人id")
    private String createBy;

    @ApiModelProperty(value = "创建人姓名")
    private String createByUsername;

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

    public String getCreateByUsername() {
        BaseUserDO user = UserUtils.getUser(createBy);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    public String getProcessStartByName(){
        BaseUserDO user = UserUtils.getUser(processStartBy);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
