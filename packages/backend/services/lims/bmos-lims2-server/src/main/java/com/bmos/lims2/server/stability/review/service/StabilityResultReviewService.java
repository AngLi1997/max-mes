package com.bmos.lims2.server.stability.review.service;

import com.bmos.lims2.server.stability.review.dto.StabilityAuditApproveDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditDetailDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditHeaderDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditRejectDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditReturnDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityResultReviewDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityResultReviewQueryDTO;
import com.bmos.mybatis.page.CommonPage;

/**
 * 稳定性结果审核服务接口
 */
public interface StabilityResultReviewService {

    /**
     * 分页查询稳定性结果审核列表
     */
    CommonPage<StabilityResultReviewDTO> pageReviewList(StabilityResultReviewQueryDTO queryDTO);

    /**
     * 发起稳定性结果审核（对应"审核处理"按钮，仅CONFIRMED状态可发起）
     *
     * @param orderId 检验单ID
     * @return 流程实例ID
     */
    String startReview(Long orderId);

    /**
     * 查询稳定性审核详情页头部"请验信息"面板（轻量接口）
     *
     * @param orderId 检验单ID
     */
    StabilityAuditHeaderDTO getAuditHeader(Long orderId);

    /**
     * 查询稳定性审核详情（检验任务数据，含分析项分组，与常规样品审核结构一致）
     *
     * @param orderId 检验单ID
     */
    StabilityAuditDetailDTO getAuditDetail(Long orderId);

    /**
     * 审核通过（需密码电子签名验证）
     */
    void approve(StabilityAuditApproveDTO dto);

    /**
     * 审核退回（退回上一节点）
     */
    void returnToPrev(StabilityAuditReturnDTO dto);

    /**
     * 审核不通过（选择具体任务退回）
     */
    void reject(StabilityAuditRejectDTO dto);

    /**
     * 审核通过回调（流程引擎 PROCESS_END 事件触发）
     */
    void auditProcessSuccessCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName);

    /**
     * 审核不通过回调（流程引擎 PROCESS_REJECT_END 事件触发）
     */
    void auditProcessRejectCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName);
}
