package com.bmos.mes.service.audit.service;

import com.bmos.mes.service.audit.dto.AuditPageDTO;
import com.bmos.mes.service.audit.dto.SaveAuditDTO;
import com.bmos.mes.service.audit.model.FlowAuditVersion;
import com.bmos.mes.service.audit.vo.FlowAuditDetailVO;
import com.bmos.mes.service.audit.vo.FlowAuditVO;

import java.util.List;

public interface FlowAuditVersionService {

    List<FlowAuditVO> selectVersionList(AuditPageDTO dto);

    FlowAuditVersion saveFlowAuditVersion(SaveAuditDTO dto);

    Boolean deleteFlowAudit(Long versionId);

    FlowAuditDetailVO findVersionById(Long versionId);

    List<FlowAuditVersion> queryByAuditIdListAndState(List<Long> auditIdList, Integer code);

    List<String> selectListByAuditId(Long id);

    FlowAuditVersion queryById(Long id);

    void disableByAuditId(Long flowAuditId);

    /**
     * @param id
     * @param enable true 启用 false 停用
     */
    void changeStateById(Long id, Boolean enable);


}
