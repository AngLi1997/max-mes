package com.bmos.lims2.server.audit.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.audit.engine.core.command.CompleteBatchTaskCmd;
import com.bmos.audit.engine.core.command.UpdateTaskCmd;
import com.bmos.audit.engine.core.model.AuditExecutionInstance;
import com.bmos.audit.engine.core.model.AuditTaskInstance;
import com.bmos.audit.engine.core.query.cmd.PageListTaskQueryCmd;
import com.bmos.audit.engine.core.query.resp.TaskHistoryResp;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.common.enums.FlowAuditCodeEnum;
import com.bmos.lims2.server.audit.dto.CompleteDTO;
import com.bmos.lims2.server.audit.dto.FlowAuditTaskDTO;
import com.bmos.lims2.server.audit.vo.AuditNodeStateVO;
import com.bmos.lims2.server.audit.vo.FlowAuditNodeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface FlowCompleteConvert {

    FlowCompleteConvert INSTANCE = Mappers.getMapper(FlowCompleteConvert.class);

    default CompleteBatchTaskCmd convertToCompleteCmd(CompleteDTO dto) {
        CompleteBatchTaskCmd cmd = CompleteBatchTaskCmd.builder()
                .assignees(convertToRoleIdList(), FlowAuditCodeEnum.ALL_ROLE.getValue())
                .assignee(SysUserHolder.getUser().getUserId() ,FlowAuditCodeEnum.ALL_USER.getValue())
                .taskId(dto.getTaskId()).processInstanceId(dto.getProcessInstanceId())
                .remark(dto.getRemark())
                .comment(dto.getComment())
                .completeBy(SysUserHolder.getUser().getUserId());
        if (AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())){
            cmd = CompleteBatchTaskCmd.builder()
                    .taskId(dto.getTaskId()).processInstanceId(dto.getProcessInstanceId())
                    .remark(dto.getRemark())
                    .comment(dto.getComment())
                    .completeBy(SysUserHolder.getUser().getUserId());
        }
        return cmd;
    }

    List<UpdateTaskCmd> convertToUpdateCmd(List<AuditTaskInstance> list);

    default PageListTaskQueryCmd convertToQueryCmd(FlowAuditTaskDTO dto) {
        List<String> businessKeys = new ArrayList<>();
        if (CollUtil.isNotEmpty(dto.getBusinessKeyList())) {
            businessKeys.addAll(dto.getBusinessKeyList());
        }
        if (StrUtil.isNotEmpty(dto.getBusinessKey())) {
            businessKeys.add(dto.getBusinessKey());
        }
        if (AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())){
            return PageListTaskQueryCmd.builder()
                    .businessKeyList(businessKeys)
                    .category(dto.getCategory())
                    .categorys(dto.getCategoryList())
                    .current(dto.getCurrent())
                    .size(dto.getSize());
        }
        return PageListTaskQueryCmd.builder()
                .assignees(convertToRoleIdList(), FlowAuditCodeEnum.ALL_ROLE.getValue())
                .assignee(SysUserHolder.getUser().getUserId(), FlowAuditCodeEnum.ALL_USER.getValue())
                .businessKeyList(businessKeys)
                .category(dto.getCategory())
                .categorys(dto.getCategoryList())
                .current(dto.getCurrent())
                .orderBy(dto.getOrderBy())
                .dir(dto.getDir())
                .size(dto.getSize());
    }

    default List<String> convertToRoleIdList() {
        PlatformApiAdaptor platformApiAdaptor = SpringUtil.getBean(PlatformApiAdaptor.class);
        List<String> roleIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(platformApiAdaptor.roleIds())) {
            platformApiAdaptor.roleIds().stream().map(item -> roleIdList.add(String.valueOf(item))).collect(Collectors.toList());
        }
        return roleIdList;
    }

    List<FlowAuditNodeVO> convertToNodeList(List<TaskHistoryResp> list);

    default List<AuditNodeStateVO> convertNodeState(List<AuditExecutionInstance> execution) {
        Map<String, List<AuditExecutionInstance>> executionMap = CollectionUtils.convertMultiMap(execution, AuditExecutionInstance::getElementKey);
        List<AuditNodeStateVO> voList = new ArrayList<>();
        executionMap.forEach((key, value) -> {
            AuditExecutionInstance auditExecutionInstance = value.stream().findFirst().get();
            AuditNodeStateVO vo = new AuditNodeStateVO();
            vo.setElementKey(auditExecutionInstance.getElementKey());
            vo.setState(auditExecutionInstance.getState());
            voList.add(vo);
        });
        return voList;
    }
}
