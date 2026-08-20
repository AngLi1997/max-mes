package com.bmos.lims2.server.inspect.order.service;

import com.bmos.lims2.server.inspect.document.dto.DocumentConfigFieldSaveDTO;
import com.bmos.lims2.server.inspect.order.dto.*;
import com.bmos.lims2.server.inspect.division.dto.SampleDivisionPageQueryDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 检验单服务接口
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
public interface InspectionOrderService {

    /**
     * 分页查询检验单列表
     * @param queryDTO 查询条件
     * @return 检验单分页列表
     */
    CommonPage<InspectionOrderDTO> getInspectionOrderPage(InspectionOrderPageQueryDTO queryDTO);

    /**
     * 报告生成页面分页查询（仅常规请验单，状态固定为 COMPLETED/TERMINATED）
     */
    CommonPage<InspectionOrderDTO> getReportPageOrders(InspectionOrderPageQueryDTO queryDTO);

    /**
     * 根据ID查询检验单详情
     * @param id 检验单ID
     * @return 检验单详情
     */
    InspectionOrderDTO getInspectionOrderById(Long id);

    /**
     * 保存检验单（新增或更新）
     * @param saveDTO 保存数据
     * @return 检验单ID
     */
    Long saveInspectionOrder(InspectionOrderSaveDTO saveDTO);

    /**
     * 确认检验单
     * @param id 检验单ID
     */
    void confirmInspectionOrder(Long id);

    /**
     * 批量确认检验单
     * @param ids 检验单ID列表
     */
    void batchConfirmInspectionOrder(List<Long> ids);

    /**
     * 终止检验单
     * @param id 检验单ID
     * @param terminateReason 终止原因
     */
    void terminateInspectionOrder(Long id, String terminateReason);

    /**
     * 删除检验单
     * @param id 检验单ID
     */
    void deleteInspectionOrder(Long id);

    /**
     * 生成检验单PDF
     * @param id 检验单ID
     * @return PDF文件字节数组
     */
    byte[] generateInspectionOrderPdf(Long id);

    /**
     * 请验单配置预览PDF
     * @param configId 请验单配置ID
     * @return PDF文件字节数组
     */
    byte[] previewInspectionOrderPdfByConfigId(Long configId);

    /**
     * 请验单配置预览PDF（支持未保存的自定义字段值）
     * 合并规则：用户输入值 > 模板默认值 > 空字符串
     * @return PDF文件字节数组
     */
    byte[] previewInspectionOrderPdfByConfigId(List<DocumentConfigFieldSaveDTO> fields);

    /**
     * 根据检验单号查询检验单
     * @param orderNo 检验单号
     * @return 检验单信息
     */
    InspectionOrderDTO getInspectionOrderByOrderNo(String orderNo);

    /**
     * 校验检验单是否存在
     * @param id 检验单ID
     * @return 是否存在
     */
    boolean existsInspectionOrder(Long id);

    /**
     * 根据检品ID获取可用的请验单模板列表
     * @param materialId 检品ID
     * @return 模板列表
     */
    List<InspectionOrderTemplateDTO> getAvailableTemplatesByMaterialId(Long materialId);

    /**
     * 分页查询已确认的检验单列表（用于取样功能）
     */
    CommonPage<InspectionOrderDTO> getConfirmedOrders(InspectionOrderPageQueryDTO queryDTO);

    /**
     * 统计已确认（待取样）的检验单数量（用于取样登记页签徽标）
     */
    long countConfirmedOrders();

    /** 取样页签用：仅计入状态=CONFIRMED 且存在未取样样品的检验单数量（与列表逻辑一致） */
    long countConfirmedOrdersForSampling();



    /**
     * 分页查询待接收的检验单列表
     * 查询条件：存在样品已经被取样但是没有被接收的检验单
     * @param queryDTO 查询条件
     * @return 检验单分页列表
     */
    CommonPage<OrderPageDTO> getPendingReceiveOrders(SampleReceivePageQueryDTO queryDTO);

    /**
     * 分页查询待分样的检验单列表
     * @param queryDTO 查询条件
     * @return 检验单分页列表
     */
    CommonPage<OrderPageDTO> pageSampleDivisionOrders(SampleDivisionPageQueryDTO queryDTO);

    /**
     * 联动下拉：按检品/方案版本/时间筛选检验单
     */
    CommonPage<InspectionOrderOptionDTO> listOptions(InspectionOrderOptionQueryDTO queryDTO);

    /**
     * 下拉：根据方案ID与可选的检验单状态筛选检验单
     */
    java.util.List<InspectionOrderOptionDTO> listOptionsByScheme(InspectionOrderBySchemeQueryDTO queryDTO);
}