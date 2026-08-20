package com.bmos.lims2.server.audit.behavior;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.audit.engine.core.behavior.TaskAssigneeBehavior;
import com.bmos.audit.engine.core.context.RuntimeContext;
import com.bmos.audit.engine.core.element.task.UserTask;
import com.bmos.audit.engine.core.model.AuditTaskAssignee;
import com.bmos.audit.engine.core.utils.ObjectUtil;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.lims2.common.enums.FlowAuditCodeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.FlowAuditUserService;
import com.bmos.lims2.server.audit.complete.TaskCompleteFactory;
import com.bmos.lims2.server.audit.entity.FlowAuditUser;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FlowAuditTaskAssigneeBehavior implements TaskAssigneeBehavior {

    private final static String SETTINGS = "settings";

    @Override
    public List<AuditTaskAssignee> findTaskAssignees(RuntimeContext context) {
        FlowAuditUserService userService = SpringUtil.getBean(FlowAuditUserService.class);
        UserTask userTask = (UserTask) context.getCurElement();
        Map<String, Object> payload = userTask.getPayload();
        List<AuditTaskAssignee> list = new ArrayList<>();
        if (ObjectUtil.isEmpty(payload) || ObjectUtil.isEmpty(payload.get(SETTINGS))) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_SELECT_ERROR);
        }
        payload = JsonUtils.parseObject((String) payload.get(SETTINGS),new TypeReference<Map<String,Object>>(){});

        String deploymentId = context.getAuditProcessInstance().getDeploymentId();
        List<FlowAuditUser> listByKey = userService.findListByKeyAndDeploymentId(userTask.getKey(),deploymentId);
        if (CollUtil.isEmpty(listByKey)) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_USER_ERROR);
        }
        String rule = (String) payload.get(FlowAuditCodeEnum.COMPLETE_TYPE.getValue());
        if (StrUtil.isBlank(rule)) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_SELECT_ERROR);
        }
        Map<String, List<FlowAuditUser>> userType = CollectionUtils.convertMultiMap(listByKey, FlowAuditUser::getAssigneeType);
        if (StrUtil.equals(rule, FlowAuditCodeEnum.COUNTERSIGN.getValue())) {
            List<String> tactics = (List<String>) payload.get(FlowAuditCodeEnum.STRATEGY.getValue());
            if (tactics.size() <= 1) {
                if (tactics.contains(FlowAuditCodeEnum.ALL_USER.getValue())) {
                    userType.get(FlowAuditCodeEnum.ALL_USER.getValue()).forEach(item -> {
                        AuditTaskAssignee assignee = new AuditTaskAssignee();
                        assignee.setAssignee(String.valueOf(item.getAssignee()));
                        assignee.setAssigneeType(item.getAssigneeType());
                        list.add(assignee);
                    });
                    return list;
                }
                if (tactics.contains(FlowAuditCodeEnum.ALL_ROLE.getValue())) {
                    userType.get(FlowAuditCodeEnum.ALL_ROLE.getValue()).forEach(item -> {
                        AuditTaskAssignee assignee = new AuditTaskAssignee();
                        assignee.setAssignee(String.valueOf(item.getAssignee()));
                        assignee.setAssigneeType(item.getAssigneeType());
                        list.add(assignee);
                    });
                    return list;
                }
            }
        }
        listByKey.forEach(item -> {
            AuditTaskAssignee assignee = new AuditTaskAssignee();
            assignee.setAssignee(String.valueOf(item.getAssignee()));
            assignee.setAssigneeType(item.getAssigneeType());
            list.add(assignee);
        });
        return list;
    }

    @Override
    @DistributedLock(expression = "#context.curAuditTaskInstance.taskId")
    public boolean completed(RuntimeContext context) {
        UserTask userTask = (UserTask) context.getCurElement();
        Map<String, Object> payload = userTask.getPayload();
        if (ObjectUtil.isEmpty(payload)) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_SELECT_ERROR);
        }
        TaskCompleteFactory taskCompleteFactory = SpringUtil.getBean(TaskCompleteFactory.class);
        payload = JsonUtils.parseObject((String) payload.get(SETTINGS),new TypeReference<Map<String,Object>>(){});
        String rule = (String) payload.get(FlowAuditCodeEnum.COMPLETE_TYPE.getValue());
        if (StrUtil.isBlank(rule) || ObjectUtil.isEmpty(taskCompleteFactory)) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_SELECT_ERROR);
        }
        return taskCompleteFactory.getRule(rule).completed(context);
    }
}
