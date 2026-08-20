package com.bmos.lims2.server.inspect.order.mapper;

import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.server.inspect.order.dto.*;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.division.dto.SampleDivisionPageQueryDTO;
import com.bmos.lims2.server.report.dto.ReportApprovalPageQueryDTO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionQueryDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderBySchemeQueryDTO;

/**
 * 检验单Mapper
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Mapper
public interface InspectionOrderMapper extends BaseMapperX<InspectionOrder> {

    /**
     * 分页查询检验单信息（包含关联信息）
     * @param queryDTO 查询条件
     * @return 检验单列表
     */
    List<InspectionOrderDTO> selectPageWithRelation(@Param("param") InspectionOrderPageQueryDTO queryDTO);

    /**
     * 根据ID查询检验单详情（包含关联信息）
     * @param id 检验单ID
     * @return 检验单详情
     */
    InspectionOrderDTO selectByIdWithRelation(@Param("id") Long id);

    /**
     * 根据检验单号查询检验单
     * @param orderNo 检验单号
     * @return 检验单信息
     */
    default InspectionOrder selectByOrderNo(String orderNo) {
        return selectOne(new LambdaQueryWrapperX<InspectionOrder>()
                .eq(InspectionOrder::getOrderNo, orderNo));
    }

    /**
     * 校验检验单是否存在
     * @param id 检验单ID
     * @return 是否存在
     */
    default boolean existsById(Long id) {
        return exists(new LambdaQueryWrapperX<InspectionOrder>()
                .eq(InspectionOrder::getId, id));
    }

    /**
     * 根据检品ID查询检验单列表
     * @param materialId 检品ID
     * @return 检验单列表
     */
    default List<InspectionOrder> selectByMaterialId(Long materialId) {
        return selectList(new LambdaQueryWrapperX<InspectionOrder>()
                .eq(InspectionOrder::getMaterialId, materialId));
    }

    /**
     * 根据检验方案版本ID查询检验单列表
     * @param schemeVersionId 检验方案版本ID
     * @return 检验单列表
     */
    default List<InspectionOrder> selectBySchemeVersionId(Long schemeVersionId) {
        return selectList(new LambdaQueryWrapperX<InspectionOrder>()
                .eq(InspectionOrder::getSchemeVersionId, schemeVersionId));
    }

    /**
     * 根据状态查询检验单列表
     * @param orderStatus 单据状态
     * @return 检验单列表
     */
    default List<InspectionOrder> selectByOrderStatus(String orderStatus) {
        return selectList(new LambdaQueryWrapperX<InspectionOrder>()
                .eq(InspectionOrder::getOrderStatus, orderStatus));
    }

    /**
     * 分页查询待接收的检验单列表
     * @param queryDTO 查询条件
     * @return 检验单分页列表
     */
    List<OrderPageDTO> selectPendingReceiveOrders(@Param("query") SampleReceivePageQueryDTO queryDTO);

    /**
     * 分页查询待分样的检验单列表
     * @param queryDTO 查询条件
     * @return 检验单分页列表
     */
    List<OrderPageDTO> selectPageWithSamplesForDivision(@Param("query") SampleDivisionPageQueryDTO queryDTO);

    List<InspectionOrderDTO> selectConfirmedOrders(@Param("param") InspectionOrderPageQueryDTO queryDTO);

    long countConfirmedOrdersForSampling();

    /**
     * 分页查询待样品审核的检验单列表
     * @param queryDTO 查询条件
     * @return 检验单分页列表
     */
    List<OrderPageDTO> selectPendingSampleAuditOrders(@Param("query") com.bmos.lims2.server.inspect.audit.dto.SampleAuditPageQueryDTO queryDTO);

    /**
     * 查询符合验证条件的检验单：指定检品、方案版本，且样品审核已通过
     */
    default java.util.List<InspectionOrder> selectEligibleForReportValidation(Long materialId, Long schemeVersionId) {
        return selectList(new LambdaQueryWrapperX<InspectionOrder>()
                .eq(InspectionOrder::getMaterialId, materialId)
                .eq(InspectionOrder::getSchemeVersionId, schemeVersionId)
                .eq(InspectionOrder::getOrderStatus, InspectionOrderStatusEnum.COMPLETED));
    }

    /**
     * 在指定检验单ID集合中筛选可自动发起样品审核的订单ID
     */
    List<Long> selectEligibleForSampleAuditStart(@Param("orderIds") List<Long> orderIds);

    /**
     * 查询已确认且样品审核完成的检验单（报告生成候选）
     */
    List<InspectionOrderDTO> selectSampleAuditedOrders(@Param("param") InspectionOrderPageQueryDTO queryDTO);

    /**
     * 查询待审批的报告列表
     * @param queryDTO
     * @return
     */
    List<OrderPageDTO> selectPendingReportAuditOrders(ReportApprovalPageQueryDTO queryDTO);

    /**
     * 联动下拉：按检品/方案版本/时间筛选检验单，按请验时间倒序
     */
    List<InspectionOrderOptionDTO> listOptions(@Param("query") InspectionOrderOptionQueryDTO queryDTO);

    /**
     * 下拉：根据方案ID与可选的检验单状态筛选检验单（按请验时间倒序）
     */
    List<InspectionOrderOptionDTO> listOptionsByScheme(@Param("query") InspectionOrderBySchemeQueryDTO queryDTO);

    /**
     * 根据请验单ID集合查询对应的方案版本ID（去重）
     * @param orderIds 请验单ID集合
     * @return 方案版本ID列表
     */
    List<Long> selectSchemeVersionIdsByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 是否存在使用指定请验单模板的检验单
     * @param templateId 请验单模板ID
     * @return true 表示存在
     */
    boolean existsByTemplateId(@Param("templateId") Long templateId);

    /**
     * 根据批号和检品ID查询常规检验单列表（用于获取稳定性计划0月检验数据，排除已终止的）
     */
    default List<InspectionOrder> selectByBatchNoAndMaterialId(String batchNo, Long materialId) {
        return selectList(new LambdaQueryWrapperX<InspectionOrder>()
                .eq(InspectionOrder::getBatchNo, batchNo)
                .eq(InspectionOrder::getMaterialId, materialId)
                .eq(InspectionOrder::getSchemeSource, com.bmos.lims2.common.enums.InspectionOrderSourceEnum.REGULAR)
                .ne(InspectionOrder::getOrderStatus, InspectionOrderStatusEnum.TERMINATED)
                .orderByDesc(InspectionOrder::getCreateTime));
    }

    /**
     * 查询0月候选检验单（带关联信息：方案名、版本号、请验人、请验时间）
     */
    List<com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO> selectCandidatesByBatchNoAndMaterialId(
            @Param("batchNo") String batchNo, @Param("materialId") Long materialId);
}