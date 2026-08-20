package com.bmos.lims2.server.inspect.audit.service;

import com.bmos.lims2.server.inspect.audit.dto.SampleAuditPageQueryDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO;
import com.bmos.lims2.server.inspect.audit.dto.SampleAuditTaskDTO;
import com.bmos.lims2.server.inspect.order.dto.OrderPageDTO;
import com.bmos.mybatis.page.CommonPage;

public interface SampleAuditService {

    CommonPage<SampleAuditTaskDTO> pagePendingSampleAuditOrders(SampleAuditPageQueryDTO queryDTO);

    InspectionOrderEntryDTO getSampleAuditDetail(Long orderId);

    String startSampleAudit(Long orderId);

    /**
     * 样品审核流程通过回调
     * @param processInstanceId 流程实例ID
     * @param comment 审批意见
     * @param remark 备注
     * @param userId 处理人
     * @param nodeName 节点名称
     */
    void auditProcessSuccessCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName);

    void rejectTasks(com.bmos.lims2.server.inspect.audit.dto.SampleAuditRejectDTO dto);

    /**
     * 样品审核流程拒绝结束回调
     * @param processInstanceId 流程实例ID
     * @param comment 审批意见
     * @param remark 拒绝原因备注
     * @param userId 处理人
     * @param nodeName 节点名称
     */
    void auditProcessRejectCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName);
}


