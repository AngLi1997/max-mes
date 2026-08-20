package com.bmos.mes.service.audit.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.FlowAuditStateEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.audit.convert.FlowAuditVersionConvert;
import com.bmos.mes.service.audit.dto.AuditPageDTO;
import com.bmos.mes.service.audit.dto.SaveAuditDTO;
import com.bmos.mes.service.audit.mapper.FlowAuditVersionMapper;
import com.bmos.mes.service.audit.model.FlowAuditMessage;
import com.bmos.mes.service.audit.model.FlowAuditUser;
import com.bmos.mes.service.audit.model.FlowAuditVersion;
import com.bmos.mes.service.audit.service.FlowAuditMessageService;
import com.bmos.mes.service.audit.service.FlowAuditUserService;
import com.bmos.mes.service.audit.service.FlowAuditVersionService;
import com.bmos.mes.service.audit.vo.FlowAuditDetailVO;
import com.bmos.mes.service.audit.vo.FlowAuditVO;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class FlowAuditVersionServiceImpl implements FlowAuditVersionService {

    @Autowired
    private FlowAuditVersionMapper mapper;

    @Autowired
    private FlowAuditUserService userService;

    @Autowired
    private FlowAuditMessageService megService;

    @Resource
    private OperationHistoryService operationHistoryService;

    @Override
    public List<FlowAuditVO> selectVersionList(AuditPageDTO dto) {
        return mapper.selectVersionList(dto);
    }

    @Override
    public FlowAuditVersion saveFlowAuditVersion(SaveAuditDTO dto) {
        // 版本号校验
        if (mapper.versionExist(dto)) {
            throw new BmosException(MesResponseCode.AUDIT_VERSION_EXISTS);
        }
        try {
            FlowAuditVersion flowAuditVersion = FlowAuditVersionConvert.INSTANCE.convertToVersion(dto);
            flowAuditVersion.setId(dto.getVersionId());
            if (dto.getChangeVersion()){
                flowAuditVersion.setHistoryVersion(dto.getSourceVersion());
            }
            mapper.saveFlowAuditVersion(flowAuditVersion);
            // 保存历史
            operationHistoryService.save(OperationLogModel.builder()
                    .businessId(flowAuditVersion.getId())
                    .module(BusinessModule.AUDIT_CONFIG.name())
                    .operationType(Objects.isNull(dto.getVersionId()) ? OperationType.SAVE.getValue() : OperationType.REDACT.getValue())
                    .build());
            return flowAuditVersion;
        } catch (Exception e) {
            throw new BmosException(MesResponseCode.FLOW_AUDIT_STATE_ERROR);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFlowAudit(Long versionId) {
        FlowAuditVersion version = mapper.queryById(versionId);
        if (ObjectUtil.isEmpty(version) || !FlowAuditStateEnum.DESIGN.getValue().equals(version.getState())) {
            throw new BmosException(MesResponseCode.FLOW_AUDIT_NOT_DELETE);
        }
        mapper.deleteFlowAudit(versionId);
        List<FlowAuditUser> userList = userService.findListByDeploymentId(version.getDeploymentId());
        userService.deleteByIds(userList);
        List<FlowAuditMessage> megList = megService.findMegListByDeploymentId(version.getDeploymentId());
        return megService.deleteByIds(megList);
    }

    @Override
    public FlowAuditDetailVO findVersionById(Long versionId) {
        FlowAuditVersion version = mapper.queryById(versionId);
        if (ObjectUtil.isEmpty(version)) {
            throw new BmosException(MesResponseCode.FLOW_AUDIT_SELECT_ERROR);
        }
        FlowAuditDetailVO vo = FlowAuditVersionConvert.INSTANCE.convertToVo(version);
        vo.setVersionId(version.getId());
        return vo;
    }

    @Override
    public List<FlowAuditVersion> queryByAuditIdListAndState(List<Long> auditIdList, Integer code) {
        return mapper.queryByAuditIdListAndState(auditIdList,code);
    }

    @Override
    public List<String> selectListByAuditId(Long auditId) {
        List<FlowAuditVersion> auditVersionList = mapper.selectListByAuditId(auditId);
        return CollectionUtils.convertList(auditVersionList, FlowAuditVersion::getDeploymentId);
    }

    @Override
    public FlowAuditVersion queryById(Long id) {
        return mapper.queryById(id);
    }

    @Override
    public void disableByAuditId(Long flowAuditId) {
        FlowAuditVersion flowAuditVersion = mapper.selectVersionState(flowAuditId);
        if (ObjectUtil.isNull(flowAuditVersion)) {
            return;
        }
        changeStateById(flowAuditVersion.getId(), Boolean.FALSE);
    }

    @Override
    public void changeStateById(Long id, Boolean enable) {
        mapper.changeStateById(id, enable);
        // 记录历史
        operationHistoryService.save(OperationLogModel.builder().businessId(id)
                .operationType(enable ? OperationType.ENABLE.getValue() : OperationType.INVALID.getValue())
                .module(BusinessModule.AUDIT_CONFIG.name())
                .build());
    }
}
