package com.bmos.lims2.server.operate.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.common.enums.AuditTypeEnum;
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
@ApiModel(value = "文件审核返回vo")
public class OperateRuleAuditVO {

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "文件编号")
    private String code;

    @ApiModelProperty(value = "文件版本id")
    private Long versionId;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "版本描述")
    private String remark;

    @ApiModelProperty(value = "审核类型")
    private String auditType;

    @ApiModelProperty(value = "审核类型")
    private AuditTypeEnum auditTypeEnum;

    @ApiModelProperty(value = "生效日期")
    private String effectDate;

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

    @ApiModelProperty(value = "版本主键id")
    private Long id;

    @ApiModelProperty(value = "业务参数")
    private Map<String,Object> payload;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建人名称")
    private String createByUsername;

    public String getProcessStartByName(){
        BaseUserDO user = UserUtils.getUser(processStartBy);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    public AuditTypeEnum getAuditTypeEnum(){
        return AuditTypeEnum.getEnumByType(auditType);
    }

    public String getEffectDate(){
        if (StrUtil.equals(auditType,AuditTypeEnum.OPERATE_RULE_BLOCK.getAuditType())){
            return StrUtil.DASHED;
        }
        return effectDate;
    }

    public String getCreateByUsername() {
        BaseUserDO user = UserUtils.getUser(createBy);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
