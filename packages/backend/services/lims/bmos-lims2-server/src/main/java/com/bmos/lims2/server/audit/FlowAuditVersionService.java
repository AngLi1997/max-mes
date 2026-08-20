package com.bmos.lims2.server.audit;


import com.bmos.lims2.server.audit.dto.AuditPageDTO;
import com.bmos.lims2.server.audit.dto.SaveAuditDTO;
import com.bmos.lims2.server.audit.entity.FlowAuditVersion;
import com.bmos.lims2.server.audit.vo.FlowAuditDetailVO;
import com.bmos.lims2.server.audit.vo.FlowAuditVO;

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
