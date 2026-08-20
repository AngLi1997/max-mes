package com.bmos.lims2.server.operate.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.lims2.server.operate.dto.SaveOperateRuleDTO;
import com.bmos.lims2.server.operate.model.OperateRuleVersion;
import com.bmos.lims2.server.operate.vo.OperateRuleAuditVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Mapper
public interface OperateRuleVersionConvert {
    OperateRuleVersionConvert INSTANCE = Mappers.getMapper(OperateRuleVersionConvert.class);

    OperateRuleVersion convertToSave(SaveOperateRuleDTO dto);

    default List<OperateRuleAuditVO> handleTodoFlowData(List<OperateRuleAuditVO> voList, Map<String, TaskListResp> map) {
        List<OperateRuleAuditVO> vo = new ArrayList<>();
        voList.forEach(item -> {
            TaskListResp taskListResp = map.get(item.getVersionId().toString());
            if (ObjectUtil.isEmpty(taskListResp)){
                return;
            }
            item.setNodeName(taskListResp.getElementName());
            item.setProcessStartBy(taskListResp.getProcessStartBy());
            item.setProcessStartTime(taskListResp.getProcessStartTime());
            item.setPayload(taskListResp.getPayload());
            item.setTaskId(taskListResp.getTaskId());
            item.setDeploymentId(taskListResp.getDeploymentId());
            item.setExecutionId(taskListResp.getExecutionId());
            item.setNodeId(taskListResp.getElementKey());
            vo.add(item);
        });
        if (CollUtil.isEmpty(vo)){
            return vo;
        }
        return vo.stream()
                .sorted(Comparator.comparing(OperateRuleAuditVO::getProcessStartTime).reversed())
                .collect(Collectors.toList());
    }
}
