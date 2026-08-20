package com.bmos.mes.service.plan.info.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.plan.PlanAuditProgressStatusEnum;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel("生产审核进度详情分页VO")
public class PlanAuditProgressDetailVO {

    @ApiModelProperty(value = "工序节点名称")
    private String procedureName;

    @ApiModelProperty(value = "任务节点名称")
    private String procedureStepName;

    @ApiModelProperty(value = "节点类型")
    private String nodeFunction;

    @ApiModelProperty(value = "审核状态")
    private PlanAuditProgressStatusEnum auditStatus;

    @ApiModelProperty("工作流状态")
    private String flowState;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "完成时间")
    private String completeTime;

    @ApiModelProperty(value = "审核人")
    private String auditUserId;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    public String getAuditUserName() {
        if (!Objects.equals(getAuditStatus(), PlanAuditProgressStatusEnum.AUDIT_COMPLETED)) {
            return StrUtil.EMPTY;
        }
        BaseUserDO user = UserUtils.getUser(auditUserId);
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }

    public PlanAuditProgressStatusEnum getAuditStatus() {
        return PlanAuditProgressStatusEnum.getEnumByMappingValue(flowState);
    }

    public Integer getProcessChangeNumber() {
        return processChangeNumber == null ? 0 : processChangeNumber + 1;
    }

    public Integer getProcedureChangeNumber() {
        return procedureChangeNumber == null ? 0 : procedureChangeNumber + 1;
    }

    /**
     * 审核完成和审核中才有开始时间
     * @return
     */
    public String getStartTime() {
        if (Objects.equals(getAuditStatus(), PlanAuditProgressStatusEnum.UNDER_AUDIT) ||
                Objects.equals(getAuditStatus(), PlanAuditProgressStatusEnum.AUDIT_COMPLETED)) {
            return startTime;
        }
        return StrUtil.EMPTY;
    }

    /**
     * 审核完成才有完成时间
     * @return
     */
    public String getCompleteTime() {
        if (Objects.equals(getAuditStatus(), PlanAuditProgressStatusEnum.AUDIT_COMPLETED)) {
            return completeTime;
        }
        return StrUtil.EMPTY;
    }
}
