package com.bmos.mes.service.plan.document.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.mes.common.enums.plan.TemplateVersionOperateTypeEnum;
import com.bmos.mes.common.enums.plan.TemplateVersionStatusEnum;
import com.bmos.mes.service.plan.document.controller.vo.*;
import com.bmos.mes.service.plan.document.model.BatchTemplateInfo;
import com.bmos.mes.service.plan.document.model.BatchTemplateInfoProcess;
import com.bmos.mes.service.plan.document.model.BatchTemplateOperateLog;
import com.bmos.mes.service.plan.document.model.BatchTemplateVersion;
import com.bmos.mes.service.plan.document.service.dto.TemplateInfoBindDTO;
import com.bmos.mes.service.plan.document.service.dto.TemplateSaveDTO;
import com.bmos.mes.service.plan.document.service.dto.TemplateVersionSaveDTO;
import com.bmos.mes.service.plan.info.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface BatchTemplateConverter {

    BatchTemplateConverter INSTANCE = Mappers.getMapper(BatchTemplateConverter.class);

    BatchTemplateInfo convert2TemplateInfo(TemplateSaveDTO dto);

    default BatchTemplateVersion convert2TemplateVersion(TemplateSaveDTO dto, Long templateInfoId){
        BatchTemplateVersion batchTemplateVersion = new BatchTemplateVersion();
        batchTemplateVersion.setVersion(dto.getVersion());
        batchTemplateVersion.setBatchTemplateInfoId(templateInfoId);
        batchTemplateVersion.setRemark(dto.getRemark());
        batchTemplateVersion.setPath(dto.getPath());
        return batchTemplateVersion;
    }

    default BatchTemplateVersion convert2TemplateVersion(TemplateVersionSaveDTO dto, Long templateInfoId){
        BatchTemplateVersion batchTemplateVersion = new BatchTemplateVersion();
        batchTemplateVersion.setVersion(dto.getVersion());
        batchTemplateVersion.setBatchTemplateInfoId(templateInfoId);
        batchTemplateVersion.setRemark(dto.getRemark());
        batchTemplateVersion.setPath(dto.getPath());
        return batchTemplateVersion;
    }

    default List<BatchTemplateInfoProcess> convert2TemplateInfoProcessList(TemplateInfoBindDTO dto){
        List<BatchTemplateInfoProcess> batchTemplateInfoProcessesList = new ArrayList<>();
        if (CollUtil.isEmpty(dto.getProcessIds())){
            return batchTemplateInfoProcessesList;
        }
        Set<Long> processIdSet = new HashSet<>();
        for (Long processId : dto.getProcessIds()) {
            if (processIdSet.contains(processId)){
                continue;
            }
            processIdSet.add(processId);
            BatchTemplateInfoProcess batchTemplateInfoProcess = new BatchTemplateInfoProcess();
            batchTemplateInfoProcess.setBatchTemplateInfoId(dto.getTemplateInfoId());
            batchTemplateInfoProcess.setProcessId(processId);
            batchTemplateInfoProcessesList.add(batchTemplateInfoProcess);
        }
        return batchTemplateInfoProcessesList;
    }

    default List<TemplateInfoPageVO> convert2PageVO(List<BatchTemplateInfo> list, Map<Long, String> treeNameMap, Map<Long, List<BatchTemplateInfoProcess>> batchTemplateInfoProcessMap){
        List<TemplateInfoPageVO> pageVOList = new ArrayList<>();
        if (CollUtil.isEmpty(list)){
            return pageVOList;
        }
        for (BatchTemplateInfo batchTemplateInfo : list) {
            TemplateInfoPageVO templateInfoPageVO = new TemplateInfoPageVO();
            templateInfoPageVO.setId(batchTemplateInfo.getId());
            templateInfoPageVO.setName(batchTemplateInfo.getName());
            templateInfoPageVO.setCategoryName(treeNameMap.get(batchTemplateInfo.getCategoryId()));
            List<BatchTemplateInfoProcess> processList = batchTemplateInfoProcessMap.get(batchTemplateInfo.getId());
            if (CollUtil.isNotEmpty(processList)){
                templateInfoPageVO.setProcessIdList(processList.stream().map(BatchTemplateInfoProcess::getProcessId).collect(Collectors.toList()));
            } else {
                templateInfoPageVO.setProcessIdList(Collections.emptyList());
            }
            pageVOList.add(templateInfoPageVO);
        }
        return pageVOList;
    }

    default List<TemplateVersionPageVO> convert2TemPlateVersionVO(List<BatchTemplateVersion> templateVersions){
        List<TemplateVersionPageVO> pageVOS = new ArrayList<>();
        if (CollUtil.isEmpty(templateVersions)){
            return pageVOS;
        }
        for (BatchTemplateVersion templateVersion : templateVersions) {
            TemplateVersionPageVO templateVersionPageVO = new TemplateVersionPageVO();
            templateVersionPageVO.setId(templateVersion.getId());
            templateVersionPageVO.setVersion(templateVersion.getVersion());
            templateVersionPageVO.setRemark(templateVersion.getRemark());
            templateVersionPageVO.setStatus(TemplateVersionStatusEnum.getEnumByValue(templateVersion.getStatus()));
            templateVersionPageVO.setNormal(templateVersion.getNormal());
            pageVOS.add(templateVersionPageVO);
        }
        return pageVOS;
    }

    default List<TemplateVersionHistoryVO> convert2LogVO(List<BatchTemplateOperateLog> batchTemplateOperateLogs){
        List<TemplateVersionHistoryVO> logVOS = new ArrayList<>();
        if (CollUtil.isEmpty(batchTemplateOperateLogs)){
            return logVOS;
        }
        for (BatchTemplateOperateLog batchTemplateOperateLog : batchTemplateOperateLogs) {
            TemplateVersionHistoryVO templateVersionHistoryVO = new TemplateVersionHistoryVO();
            templateVersionHistoryVO.setPath(batchTemplateOperateLog.getPath());
            templateVersionHistoryVO.setId(batchTemplateOperateLog.getId());
            templateVersionHistoryVO.setOperationType(CommonEnum.getEnumByValue(TemplateVersionOperateTypeEnum.class, batchTemplateOperateLog.getOperateType()));
            templateVersionHistoryVO.setCreateUsername(batchTemplateOperateLog.getOperatorName());
            templateVersionHistoryVO.setCreateBy(batchTemplateOperateLog.getCreateBy());
            templateVersionHistoryVO.setCreateTime(batchTemplateOperateLog.getOperateTime());
            templateVersionHistoryVO.setRemark(batchTemplateOperateLog.getRemark());
            logVOS.add(templateVersionHistoryVO);
        }
        return logVOS;
    }

    List<PlanEasyVO> convert2PlanEasyVO(List<Plan> planList);

    List<TemplateVersionEasyVO> convert2TemplateVersionEasyVOList(List<BatchTemplateVersion> batchTemplateVersion);
}
