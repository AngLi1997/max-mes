package com.bmos.lims2.server.eln.entry.service.impl;

import com.bmos.lims2.server.eln.entry.dto.FormDataAnnotationSaveDTO;
import com.bmos.lims2.server.eln.entry.dto.FormDataListQueryDTO;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormDataAnnotation;
import com.bmos.lims2.server.eln.entry.enums.ExecuteFormDataType;
import com.bmos.lims2.server.eln.entry.mapper.ExecuteFormDataAnnotationMapper;
import com.bmos.lims2.server.eln.entry.service.ExecuteFormDataAnnotationService;
import com.bmos.lims2.server.eln.entry.vo.FormDataAnnotationVO;
import com.bmos.common.holder.SysUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 执行表单数据-异常批注 服务实现
 * @Author: yigaohui
 * @Date: 2025/12/05 00:00
 */
@Service
@Slf4j
public class ExecuteFormDataAnnotationServiceImpl implements ExecuteFormDataAnnotationService {

    @Autowired
    private ExecuteFormDataAnnotationMapper annotationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(FormDataAnnotationSaveDTO dto) {
        if (dto == null) {
            return;
        }
        ExecuteFormDataAnnotation e = new ExecuteFormDataAnnotation();
        e.setValue(dto.getValue());
        e.setValueExtension(dto.getValueExtension());
        e.setInspectionOrderId(dto.getInspectionOrderId());
        e.setBatchNo(dto.getBatchNo());
        e.setSchemeId(dto.getSchemeId());
        e.setSchemeVersionId(dto.getSchemeVersionId());
        e.setRecordItemId(dto.getRecordItemId());
        e.setRecordVersionId(dto.getRecordVersionId());
        e.setRecordId(dto.getRecordId());
        e.setTaskId(dto.getTaskId());
        e.setItemId(dto.getItemId());
        e.setItemConfigId(dto.getItemConfigId());
        e.setParameterId(dto.getParameterId());
        e.setParameterConfigId(dto.getParameterConfigId());
        e.setFieldId(dto.getFieldId());
        e.setComponentType(dto.getComponentType());
        e.setSystemCreate(Boolean.FALSE);
        e.setOperationType(ExecuteFormDataType.ANNOTATION.getValue());
        e.setOperationTime(dto.getOperationTime());
        // 操作人取当前登录用户
        e.setOperationUser(SysUserHolder.getUser() != null ? SysUserHolder.getUser().getUserId() : null);
        e.setRemark(dto.getRemark());
        annotationMapper.insert(e);
    }

    @Override
    public List<FormDataAnnotationVO> getAnnotationList(FormDataListQueryDTO dto) {
        return annotationMapper
                .selectListByField(dto.getInspectionOrderId(), dto.getParameterConfigId(), dto.getFieldId(), dto.getTaskId())
                .stream()
                .map(e -> {
                    FormDataAnnotationVO vo = new FormDataAnnotationVO();
                    vo.setFieldId(e.getFieldId());
                    vo.setValue(e.getValue());
                    vo.setValueExtension(e.getValueExtension());
                    vo.setOperationType(e.getOperationType());
                    vo.setOperationUser(e.getOperationUser());
                    vo.setOperationTime(e.getOperationTime());
                    vo.setRemark(e.getRemark());
                    vo.setTaskId(e.getTaskId());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}


