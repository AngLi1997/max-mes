package com.bmos.mes.service.execute.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.file.FileManagerApiAdaptor;
import com.bmos.adaptor.file.model.FileUpload;
import com.bmos.adaptor.file.vo.FileVO;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.execute.AttachmentTypeEnum;
import com.bmos.mes.common.enums.execute.ModifyExceptionEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.exception.dto.RecordModifyExceptionDTO;
import com.bmos.mes.service.exception.dto.RecordModifyItemDTO;
import com.bmos.mes.service.exception.service.ExceptionManageService;
import com.bmos.mes.service.execute.convert.ExecuteAttachmentConvert;
import com.bmos.mes.service.execute.dto.ExecuteAttachmentAddRemarkDTO;
import com.bmos.mes.service.execute.dto.ExecuteAttachmentQueryDTO;
import com.bmos.mes.service.execute.dto.ExecuteAttachmentUploadDTO;
import com.bmos.mes.service.execute.mapper.ExecuteAttachmentMapper;
import com.bmos.mes.service.execute.model.ExecuteAttachment;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteAttachmentService;
import com.bmos.mes.service.execute.vo.AttachmentVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExecuteAttachmentServiceImpl implements ExecuteAttachmentService {

    @Autowired
    private ExecuteAttachmentMapper executeAttachmentMapper;

    @Autowired
    private FileManagerApiAdaptor fileManagerApiAdaptor;

    @Resource
    private ExceptionManageService exceptionManageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttachmentVO upload(ExecuteAttachmentUploadDTO dto) {
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFile(dto.getFile());
        FileVO fileVO = fileManagerApiAdaptor.fileUpload(fileUpload);
        ExecuteAttachment attachment = ExecuteAttachmentConvert.INSTANCE.convert(dto);
        if (dto.getReuse()) {
            attachment.setProcedureStepId(0L);
        }
        attachment.setPath(fileVO.getUrl());
        attachment.setAttachmentType(AttachmentTypeEnum.EVIDENCE_PICTURE.getValue());
        executeAttachmentMapper.insert(attachment);
        // 处理修订异常
        exceptionManageService.recordModifyException(RecordModifyExceptionDTO.builder().productPlanId(dto.getProductPlanId())
                .procedureStepId(dto.getProcedureStepId())
                .modifyException(ModifyExceptionEnum.TAKE_PICTURE)
                .itemList(Lists.newArrayList(RecordModifyItemDTO.builder()
                        .operationTime(LocalDateTime.now())
                        .userId(attachment.getCreateBy())
                        .build()))
                .build());
        return AttachmentVO.builder()
                .id(attachment.getId())
                .path(fileVO.getUrl())
                .type(attachment.getType())
                .createBy(attachment.getCreateBy())
                .createTime(LocalDateTime.now()).build();
    }

    @Override
    public List<AttachmentVO> getList(ExecuteAttachmentQueryDTO dto) {
        List<ExecuteAttachment> attachments = executeAttachmentMapper.selectItemList(dto);
        return ExecuteAttachmentConvert.INSTANCE.convertList(attachments);
    }

    @Override
    public List<ExecuteAttachment> getListByProductPlanId(Long productPlanId) {
        return executeAttachmentMapper.selectByProductPlanId(productPlanId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatch(List<ExecuteAttachment> pictureList) {
        if (CollUtil.isEmpty(pictureList)){
            return;
        }
        executeAttachmentMapper.saveOrUpdateBatch(pictureList);
    }

    @Override
    public List<AttachmentVO> getListByIdList(List<ExecuteFormData> executeFormData) {
        List<List<String>> list = CollectionUtils.convertList(executeFormData, item -> StrUtil.split(item.getValue(), StrUtil.C_COMMA));
        if (CollUtil.isEmpty(executeFormData) || CollUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        List<String> attachmentIdList = new ArrayList<>();
        list.forEach(attachmentIdList::addAll);
        List<ExecuteAttachment> executeAttachments = executeAttachmentMapper.selectListById(attachmentIdList);
        return BeanUtil.copyToList(executeAttachments,AttachmentVO.class);
    }

    @Override
    public String queryByIds(List<String> attachmentId) {
        List<ExecuteAttachment> executeAttachments = executeAttachmentMapper.selectListById(attachmentId);
        if (CollUtil.isEmpty(executeAttachments)){
            return null;
        }
        List<AttachmentVO> attachmentVOList = BeanUtil.copyToList(executeAttachments, AttachmentVO.class);
        return JsonUtils.toJsonString(attachmentVOList);
    }

    @Override
    public List<ExecuteAttachment> getListByPlanIdAndItemIdAndStepId(Long productPlanId, Long recordItemId, Long stepId) {
        return executeAttachmentMapper.selectItemList(productPlanId, recordItemId, stepId, AttachmentTypeEnum.EVIDENCE_PICTURE);
    }

    @Override
    public void addRemark(ExecuteAttachmentAddRemarkDTO dto) {
        ExecuteAttachment executeAttachment = executeAttachmentMapper.selectById(dto.getId());
        if (executeAttachment == null) {
            throw new BmosException(MesResponseCode.ATTACHMENT_NOT_EXIST);
        }
        executeAttachment.setRemark(dto.getRemark());
        executeAttachmentMapper.updateById(executeAttachment);
    }


}
