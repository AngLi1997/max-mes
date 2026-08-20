package com.bmos.lims2.server.audit;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.audit.engine.core.model.AuditTaskInstance;
import com.bmos.audit.engine.core.query.resp.PageHistoryInstanceResp;
import com.bmos.audit.engine.core.query.resp.TaskHistoryResp;
import com.bmos.audit.engine.core.query.resp.TaskToDoCountListResp;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.common.enums.FlowAuditStateEnum;
import com.bmos.lims2.server.audit.dto.SaveAuditDTO;
import com.bmos.lims2.server.audit.entity.FlowAudit;
import com.bmos.lims2.server.audit.entity.FlowAuditVersion;
import com.bmos.lims2.server.audit.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditConvert {
    FlowAuditConvert INSTANCE = Mappers.getMapper(FlowAuditConvert.class);

    FlowAudit convertToAudit(SaveAuditDTO dto);

    List<AuditHistoryVO> convertTList(List<PageHistoryInstanceResp> list);

    List<TaskHistoryVO> convertTHistoryList(List<TaskHistoryResp> list);

    AuditTaskVO convertToTaskVo(AuditTaskInstance taskInstance);

    List<AuditTaskVO> convertToListTaskVo(List<TaskHistoryResp> list);

    default void convertToAuditVo(List<FlowAuditVO> flowAuditList){
        FlowAuditVersionService versionService = SpringUtil.getBean(FlowAuditVersionService.class);
        List<Long> auditIdList = CollectionUtils.convertList(flowAuditList, FlowAuditVO::getId);
        List<FlowAuditVersion> auditVersionList = versionService.
                queryByAuditIdListAndState(auditIdList, FlowAuditStateEnum.STATE.getValue());
        if (CollUtil.isNotEmpty(auditVersionList)) {
            Map<Long, FlowAuditVersion> versionMap = CollectionUtils.convertMap(auditVersionList, FlowAuditVersion::getFlowAuditId);
            flowAuditList.forEach(item->{
                FlowAuditVersion version = versionMap.get(item.getId());
                if (ObjectUtil.isNotEmpty(version)) {
                    item.setState(FlowAuditStateEnum.getEnumByCode(version.getState()));
                    item.setRemark(version.getRemark());
                    item.setDeploymentId(version.getDeploymentId());
                    item.setVersion(version.getVersion());
                }
            });
        }
    }

    List<FlowAuditCategoryVO> convertToModelVo(List<FlowAudit> list);

    List<AuditCategoryCountVO> convertToTodoCountVo(List<TaskToDoCountListResp> listResps);
}
