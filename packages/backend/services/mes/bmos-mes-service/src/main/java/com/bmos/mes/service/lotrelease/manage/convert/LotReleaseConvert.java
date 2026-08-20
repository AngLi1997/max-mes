package com.bmos.mes.service.lotrelease.manage.convert;

import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.mes.service.lotrelease.manage.model.LotRelease;
import com.bmos.mes.service.lotrelease.manage.model.LotReleaseHistory;
import com.bmos.mes.service.lotrelease.manage.vo.LogReleaseHistoryVO;
import com.bmos.mes.service.lotrelease.manage.vo.LotReleaseAuditPageVO;
import com.bmos.mes.service.lotrelease.manage.vo.LotReleasePlanVO;
import com.bmos.mes.service.lotrelease.manage.vo.LotReleaseVersionPageVO;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplate;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateHistory;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateVersion;
import com.bmos.mes.service.lotrelease.template.vo.LogReleaseTemplateVersionHistoryVO;
import com.bmos.mes.service.lotrelease.template.vo.LotReleaseTemplateLinkVO;
import com.bmos.mes.service.lotrelease.template.vo.LotReleaseTemplateVersionItemVO;
import com.bmos.mes.service.lotrelease.template.vo.LotReleaseTemplateVersionLinkVO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 18:06
 */
@Mapper
public interface LotReleaseConvert {
    LotReleaseConvert INSTANCE = Mappers.getMapper(LotReleaseConvert.class);

    CommonPage<LotReleasePlanVO> convertToPlanVO(CommonPage<Plan> planCommonPage);

    CommonPage<LotReleaseVersionPageVO> convertToPageVO(CommonPage<LotRelease> page);

    List<LotReleaseTemplateLinkVO> convertToLinkVO(List<LotReleaseTemplate> lotReleaseTemplates);

    LotReleaseTemplateVersionLinkVO convertToVersionLinkVO(LotReleaseTemplateVersion lotReleaseTemplateVersions);

    default CommonPage<LotReleaseAuditPageVO> convertToVersionAuditVO(CommonPage<LotRelease> page, Map<String, TaskListResp> map) {
        if (page == null) {
            return null;
        }

        List<LotReleaseAuditPageVO> resultList = new ArrayList<>();
        for (LotRelease lotRelease : page.getList()) {
            resultList.add(lotReleaseToLotReleaseAuditPageVO(lotRelease, map.get(lotRelease.getId().toString())));
        }
        CommonPage<LotReleaseAuditPageVO> result = new CommonPage<>();
        result.setPageNum(page.getPageNum());
        result.setPageSize(page.getPageSize());
        result.setTotalPage(page.getTotalPage());
        result.setTotal(page.getTotal());
        result.setList(resultList);
        return result;
    }

    default LotReleaseAuditPageVO lotReleaseToLotReleaseAuditPageVO(LotRelease lotRelease, TaskListResp taskListResp) {
        LotReleaseAuditPageVO releaseAuditPageVO = new LotReleaseAuditPageVO();
        releaseAuditPageVO.setId(lotRelease.getId());
        releaseAuditPageVO.setNo(lotRelease.getNo());
        releaseAuditPageVO.setProductName(lotRelease.getProductName());
        releaseAuditPageVO.setProductMergeCode(lotRelease.getProductMergeCode());
        releaseAuditPageVO.setSpecification(lotRelease.getSpecification());
        releaseAuditPageVO.setProcessName(lotRelease.getProcessName());
        releaseAuditPageVO.setBatchNo(lotRelease.getBatchNo());
        releaseAuditPageVO.setName(lotRelease.getName());
        releaseAuditPageVO.setTemplateVersion(lotRelease.getTemplateVersion());
        releaseAuditPageVO.setSubmitterName(lotRelease.getSubmitterName());
        releaseAuditPageVO.setSubmitterId(lotRelease.getSubmitterId());
        releaseAuditPageVO.setSubmitterTime(lotRelease.getSubmitterTime());
        releaseAuditPageVO.setProcessStartTime(taskListResp.getProcessStartTime());
        releaseAuditPageVO.setFileUrl(lotRelease.getFileUrl());
        if (taskListResp == null){
            return releaseAuditPageVO;
        }
        releaseAuditPageVO.setTaskId(taskListResp.getTaskId());
        releaseAuditPageVO.setProcessInstanceId(taskListResp.getProcessInstanceId());
        releaseAuditPageVO.setDeploymentId(taskListResp.getDeploymentId());
        releaseAuditPageVO.setExecutionId(taskListResp.getExecutionId());
        releaseAuditPageVO.setNodeId(taskListResp.getElementKey());
        releaseAuditPageVO.setPayload(taskListResp.getPayload());
        return releaseAuditPageVO;
    }

    List<LotReleaseTemplateVersionItemVO> convertToVersionItemVO(List<LotReleaseTemplateVersion> list);

    default LogReleaseHistoryVO convertToHistoryVO(LotReleaseHistory history){
        LogReleaseHistoryVO result = new LogReleaseHistoryVO();
        result.setId(history.getId());
        result.setOperationType(history.getOperateType());
        result.setCreateBy(history.getOperateUserId());
        if (StrUtil.isNotBlank(history.getOperateUserId())){
            result.setCreateUsername(history.getOperateUserName() + StrUtil.DASHED + Objects.requireNonNull(UserUtils.getUser(history.getOperateUserId())).getLoginName());
        }
        result.setCreateTime(history.getOperateTime());
        result.setRemark(history.getOperateRemark());
        result.setComment(history.getComment());
        result.setNodeName(history.getNodeName());
        result.setPath(history.getExt());
        return result;
    }

    List<LogReleaseHistoryVO> convertToHistoryVO(List<LotReleaseHistory> histories);

    List<LogReleaseTemplateVersionHistoryVO> convertToTemplateHistoryVO(List<LotReleaseTemplateHistory> histories);

    default LogReleaseTemplateVersionHistoryVO convertToHistoryVO(LotReleaseTemplateHistory history){
        LogReleaseTemplateVersionHistoryVO result = new LogReleaseTemplateVersionHistoryVO();
        result.setId(history.getId());
        result.setOperationType(history.getOperateType());
        result.setCreateBy(history.getOperateUserId());
        if (StrUtil.isNotBlank(history.getOperateUserId())){
            result.setCreateUsername(history.getOperateUserName() + StrUtil.DASHED + Objects.requireNonNull(UserUtils.getUser(history.getOperateUserId())).getLoginName());
        }
        result.setCreateTime(history.getOperateTime());
        result.setRemark(history.getOperateRemark());
        result.setComment(history.getComment());
        result.setNodeName(history.getNodeName());
        result.setPath(history.getExt());
        return result;
    }
}
