package com.bmos.mes.service.audit.complete;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.audit.engine.core.command.UpdateTaskCmd;
import com.bmos.audit.engine.core.context.RuntimeContext;
import com.bmos.audit.engine.core.model.AuditTaskInstance;
import com.bmos.audit.engine.core.query.cmd.TaskAssigneeQueryCmd;
import com.bmos.audit.engine.core.query.cmd.TaskListQueryCmd;
import com.bmos.audit.engine.core.query.service.AuditTaskQueryService;
import com.bmos.audit.engine.core.service.AuditTaskInstanceService;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.AdminUtil;
import com.bmos.mes.common.enums.audit.FlowAuditCodeEnum;
import com.bmos.mes.service.audit.convert.FlowCompleteConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会签
 *
 * @author renjinguang
 */
public class CountersignComplete implements TaskComplete {

    @Override
    public Boolean completed(RuntimeContext context) {
        if (AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())){
            return Boolean.TRUE;
        }
        PlatformApiAdaptor platformApiAdaptor = SpringUtil.getBean(PlatformApiAdaptor.class);
        List<String> roleIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(platformApiAdaptor.roleIds())) {
            platformApiAdaptor.roleIds().stream()
                    .map(item ->
                            roleIdList.add(String.valueOf(item)))
                    .collect(Collectors.toList());
        }
        TaskAssigneeQueryCmd assignees = TaskAssigneeQueryCmd.builder()
                .assignees(roleIdList, FlowAuditCodeEnum.ALL_ROLE.getValue())
                .assignee(SysUserHolder.getUser().getUserId(), FlowAuditCodeEnum.ALL_USER.getValue());
        AuditTaskInstance curAuditTaskInstance = context.getCurAuditTaskInstance();
        //查询不属于当前用户的任务
        TaskListQueryCmd taskListQueryCmd = TaskListQueryCmd.builder()
                .assigneeList(assignees.getAssignees())
                .executionId(curAuditTaskInstance.getExecutionId());
        AuditTaskQueryService taskQueryService = SpringUtil.getBean(AuditTaskQueryService.class);
        AuditTaskInstanceService taskInstanceService = SpringUtil.getBean(AuditTaskInstanceService.class);
        List<AuditTaskInstance> taskList = taskQueryService.findNotMyTaskListByexecutionIdAndAssignees(taskListQueryCmd);
        if (CollUtil.isNotEmpty(taskList)) {
            List<AuditTaskInstance> auditTaskInstanceList = context.getCurAuditTaskInstances();
            List<UpdateTaskCmd> updateTaskCmds = FlowCompleteConvert.INSTANCE.convertToUpdateCmd(auditTaskInstanceList);
            taskInstanceService.updateBatchByTaskIdAndAssigneeList(updateTaskCmds);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
