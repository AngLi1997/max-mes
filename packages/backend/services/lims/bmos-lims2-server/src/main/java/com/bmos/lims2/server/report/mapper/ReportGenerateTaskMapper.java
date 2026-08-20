package com.bmos.lims2.server.report.mapper;

import com.bmos.lims2.server.report.dto.ReportGeneratedDTO;
import com.bmos.lims2.server.report.dto.ReportGeneratedPageQueryDTO;
import com.bmos.lims2.server.report.dto.ReportGeneratedItemDTO;
import com.bmos.lims2.server.report.dto.ReportOrderInfoDTO;
import com.bmos.lims2.server.report.dto.ReportApprovalPageQueryDTO;
import com.bmos.lims2.server.report.dto.ReportApprovalPendingItemDTO;
import com.bmos.lims2.server.report.entity.ReportGenerateTask;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportGenerateTaskMapper extends BaseMapperX<ReportGenerateTask> {

    /**
     * 分页查询模板版本已生成报告列表
     */
    List<ReportGeneratedDTO> selectGeneratedPage(@Param("query") ReportGeneratedPageQueryDTO queryDTO);

    /**
     * 查询待审批的报告生成任务（业务过滤），用于结合工作流待办做二次过滤
     */
    List<ReportApprovalPendingItemDTO> selectPendingApprovalTasks(@Param("query") ReportApprovalPageQueryDTO queryDTO);

    /**
     * 根据检验单ID查询报告生成任务列表
     */
    java.util.List<ReportGenerateTask> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 不分页：查询某检验单的已生成报告列表（成功），按生成时间倒序
     */
    java.util.List<ReportGeneratedItemDTO> selectGeneratedList(
            @Param("orderId") Long orderId,
            @Param("templateId") Long templateId
    );

    /**
     * 不分页：按检验单ID查询已生成报告列表（成功），按生成时间倒序
     */
    java.util.List<ReportGeneratedItemDTO> selectGeneratedListByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询检验单基本信息，用于组装返回
     */
    ReportOrderInfoDTO selectOrderInfo(@Param("orderId") Long orderId);

    /**
     * 将同一检验单下、相同模板的已生效报告置为作废（排除当前任务）
     */
    int voidEffectiveByOrderAndTemplate(@Param("orderId") Long orderId,
                                        @Param("templateId") Long templateId,
                                        @Param("excludeTaskId") Long excludeTaskId);
}


