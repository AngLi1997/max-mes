package com.bmos.lims2.server.operate.service;


import com.bmos.lims2.server.operate.dto.version.*;
import com.bmos.lims2.server.operate.model.OperateRule;
import com.bmos.lims2.server.operate.model.OperateRuleVersion;
import com.bmos.lims2.server.operate.vo.OperateRuleAuditVO;
import com.bmos.lims2.server.operate.vo.OperateRuleVersionDetailsVO;
import com.bmos.lims2.server.operate.vo.OperateRuleVersionPageVO;
import com.bmos.mybatis.page.CommonPage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author renjinguang
 */
public interface OperateRuleVersionService {

    CommonPage<OperateRuleVersionPageVO> getRecordPage(VersionPageDTO dto);

    List<OperateRuleVersion> getListByParentIdAndState(List<Long> parentId, String code);

    void save(OperateRuleVersion version);

    OperateRuleVersionDetailsVO getDetailsById(Long id);

    void saveVersion(SaveVersionDTO dto);

    void update(UpdateVersionDTO dto);

    void updateState(UpdateStateDTO dto);

    void startFlow(VersionStartFlowDTO dto);

    CommonPage<OperateRuleAuditVO> pageTodoFlow(OperateVersionAuditDTO dto);

    OperateRuleVersion selectById(Long id);

    void flowUpdateVersion(OperateRuleVersion version);

    List<OperateRuleVersion> selectList();

    List<OperateRuleVersion> getWaitValidVersionByDateAndState(String date, String code);

    void updateOperateRuleVersion(List<OperateRuleVersion> list);

    void auditOperateRuleNodeLog(String businessKey, String remark, String userId, String name, String comment);

    void rejectOperateRuleHistoryLog(String businessId, String comment, String remark, String userId, String nodeName);

    List<OperateRule> selectByListByState(String code, List<Long> deptIds);

    OperateRuleVersion download(Long versionId);

    List<OperateRule> getAppDetail(Long stepModelId);

    List<OperateRuleVersion> selectByOperateRuleId(Long operateId);

    void updateValid(Long versionId);

    void downloadSop(Long versionId, HttpServletResponse response);
}
