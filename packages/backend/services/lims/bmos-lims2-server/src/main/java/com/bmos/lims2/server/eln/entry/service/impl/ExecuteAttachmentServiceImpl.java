package com.bmos.lims2.server.eln.entry.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.lims2.common.constants.RecordConstant;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.config.minio.MinioFileClient;
import com.bmos.lims2.server.config.minio.constants.MinioBucket;
import com.bmos.lims2.server.eln.entry.converter.ExecuteAttachmentConvert;
import com.bmos.lims2.server.eln.entry.dto.AttachmentDTO;
import com.bmos.lims2.server.eln.entry.dto.ExecuteAttachmentAddRemarkDTO;
import com.bmos.lims2.server.eln.entry.dto.ExecuteAttachmentDownloadDTO;
import com.bmos.lims2.server.eln.entry.dto.ExecuteAttachmentQueryDTO;
import com.bmos.lims2.server.eln.entry.dto.ExecuteAttachmentUploadDTO;
import com.bmos.lims2.server.eln.entry.entity.ExecuteAttachment;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.mapper.ExecuteAttachmentMapper;
import com.bmos.lims2.server.eln.entry.service.ExecuteAttachmentService;
import com.bmos.lims2.server.eln.entry.vo.AttachmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ExecuteAttachmentServiceImpl implements ExecuteAttachmentService {

    @Autowired
    private ExecuteAttachmentMapper executeAttachmentMapper;

    @Resource
    private MinioFileClient minioFileClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttachmentDTO upload(ExecuteAttachmentUploadDTO dto) {
        AttachmentVO attachmentVO =this.uploadInner(dto);
        ExecuteAttachment attachment = ExecuteAttachmentConvert.INSTANCE.convert(dto);
        attachment.setPath(attachmentVO.getPath());
        attachment.setAttachmentType(dto.getAttachmentType().getValue());
        attachment.setFileName(dto.getFileName());
        executeAttachmentMapper.insert(attachment);
        return AttachmentDTO.builder()
                .id(attachment.getId())
                .path(attachmentVO.getPath())
                .type(attachmentVO.getType())
                .createBy(attachment.getCreateBy())
                .createTime(LocalDateTime.now()).build();
    }

    private AttachmentVO uploadInner(ExecuteAttachmentUploadDTO fileUpload) {
        MultipartFile file = fileUpload.getFile();
        try {
            String suffix = fileUpload.getFileName().substring(fileUpload.getFileName().lastIndexOf("."));
            File files = File.createTempFile(RecordConstant.TEMPORARY_FOLDER, suffix);
            file.transferTo(files);
            String key = IdUtils.getSnowflakeStr() + "_" + System.currentTimeMillis();
            String bucketName = minioFileClient.getBucketName(MinioBucket.ELN_RECORD);
            String uploadPatch = minioFileClient.uploadFile(MinioBucket.ELN_RECORD, files, String.format("/%s" + suffix, key));
            return AttachmentVO.builder()
                    .id(IdUtils.getSnowflake())
                    .type(suffix)
                    .createBy(SysUserHolder.getUser().getUserId())
                    .createTime(LocalDateTime.now())
                    .path(bucketName + uploadPatch)
                    .build();
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new BmosException(LimsResponseCode.ATTACHMENT_FILE_ERROR);
        }
    }

    @Override
    public List<AttachmentDTO> getList(ExecuteAttachmentQueryDTO dto) {
        List<ExecuteAttachment> attachments = executeAttachmentMapper.selectItemList(dto);
        return ExecuteAttachmentConvert.INSTANCE.convertList(attachments);
    }

    @Override
    public void addRemark(ExecuteAttachmentAddRemarkDTO dto) {
        ExecuteAttachment executeAttachment = executeAttachmentMapper.selectById(dto.getId());
        if (executeAttachment == null) {
            throw new BmosException(LimsResponseCode.ATTACHMENT_NOT_EXIST);
        }
        executeAttachment.setRemark(dto.getRemark());
        executeAttachmentMapper.updateById(executeAttachment);
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
    public List<ExecuteAttachment> getListByInspectionOrderId(Long inspectionOrderId) {
        return executeAttachmentMapper.selectByInspectionOrderId(inspectionOrderId);
    }

    @Override
    public List<ExecuteAttachment> getListByTaskId(Long taskId) {
        return executeAttachmentMapper.getListByTaskId(taskId);
    }

    @Override
    public void download(ExecuteAttachmentDownloadDTO dto, HttpServletResponse response) {
        String path = dto.getPath();
        if (StrUtil.isBlank(path)) {
            throw new BmosException(LimsResponseCode.ATTACHMENT_FILE_ERROR);
        }
        try {
            String bucketName = minioFileClient.getBucketName(MinioBucket.ELN_RECORD);
            String objectPath = path;
            if (StrUtil.startWith(path, bucketName)) {
                objectPath = StrUtil.removePrefix(path, bucketName);
                objectPath = StrUtil.removePrefix(objectPath, StrUtil.SLASH);
            }
            minioFileClient.download(MinioBucket.ELN_RECORD, objectPath, response);
        } catch (Exception e) {
            log.error("下载附件失败, path={}", path, e);
            throw new BmosException(LimsResponseCode.FILE_DOWNLOAD_FAILED);
        }
    }
}
