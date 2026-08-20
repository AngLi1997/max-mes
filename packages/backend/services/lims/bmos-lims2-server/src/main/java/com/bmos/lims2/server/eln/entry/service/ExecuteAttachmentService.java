package com.bmos.lims2.server.eln.entry.service;


import com.bmos.lims2.server.eln.entry.dto.AttachmentDTO;
import com.bmos.lims2.server.eln.entry.dto.ExecuteAttachmentAddRemarkDTO;
import com.bmos.lims2.server.eln.entry.dto.ExecuteAttachmentDownloadDTO;
import com.bmos.lims2.server.eln.entry.dto.ExecuteAttachmentQueryDTO;
import com.bmos.lims2.server.eln.entry.dto.ExecuteAttachmentUploadDTO;
import com.bmos.lims2.server.eln.entry.entity.ExecuteAttachment;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.vo.AttachmentVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ExecuteAttachmentService {
    AttachmentDTO upload(ExecuteAttachmentUploadDTO dto);

    List<AttachmentDTO> getList(ExecuteAttachmentQueryDTO dto);

    /**
     * 拍照取证添加备注
     * @param dto
     */
    void addRemark(ExecuteAttachmentAddRemarkDTO dto);


    void saveOrUpdateBatch(List<ExecuteAttachment> pictureList);

    List<AttachmentVO> getListByIdList(List<ExecuteFormData> executeFormData);


    String queryByIds(List<String> attachmentId);

    List<ExecuteAttachment> getListByInspectionOrderId(Long inspectionOrderId);

    List<ExecuteAttachment> getListByTaskId(Long taskId);

    /**
     * 附件下载
     * @param dto 下载参数
     * @param response 响应
     */
    void download(ExecuteAttachmentDownloadDTO dto, HttpServletResponse response);
}
