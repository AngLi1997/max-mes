package com.bmos.mes.service.execute.service;

import com.bmos.mes.service.execute.dto.ExecuteAttachmentQueryDTO;
import com.bmos.mes.service.execute.dto.ExecuteAttachmentAddRemarkDTO;
import com.bmos.mes.service.execute.dto.ExecuteAttachmentUploadDTO;
import com.bmos.mes.service.execute.model.ExecuteAttachment;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.AttachmentVO;

import java.util.List;

public interface ExecuteAttachmentService {
    AttachmentVO upload(ExecuteAttachmentUploadDTO dto);

    List<AttachmentVO> getList(ExecuteAttachmentQueryDTO dto);

    List<ExecuteAttachment> getListByProductPlanId(Long productPlanId);

    void saveOrUpdateBatch(List<ExecuteAttachment> pictureList);

    List<AttachmentVO> getListByIdList(List<ExecuteFormData> executeFormData);

    String queryByIds(List<String> attachmentId);

    /**
     * 根据生产计划id 记录项id 步骤id查询附件列表
     * @param productPlanId
     * @param recordItemId
     * @param stepId
     * @return
     */
    List<ExecuteAttachment> getListByPlanIdAndItemIdAndStepId(Long productPlanId, Long recordItemId, Long stepId);

    /**
     * 拍照取证添加备注
     * @param dto
     */
    void addRemark(ExecuteAttachmentAddRemarkDTO dto);
}
