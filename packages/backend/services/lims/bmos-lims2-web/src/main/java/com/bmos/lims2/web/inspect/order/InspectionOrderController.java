package com.bmos.lims2.web.inspect.order;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.order.dto.*;
import com.bmos.lims2.server.inspect.division.dto.SampleDivisionPageQueryDTO;
import com.bmos.lims2.web.inspect.order.converter.InspectionOrderWebConverter;
import com.bmos.lims2.server.inspect.order.service.InspectionOrderService;
import com.bmos.lims2.web.inspect.order.vo.req.*;
import com.bmos.lims2.web.inspect.order.vo.resp.InspectionOrderRespVO;
import com.bmos.lims2.web.inspect.order.vo.resp.InspectionOrderTemplateRespVO;
import com.bmos.lims2.web.inspect.receive.vo.req.SampleReceivePageReqVO;
import com.bmos.lims2.web.inspect.order.vo.resp.OrderPageRespVO;
import com.bmos.lims2.web.inspect.order.vo.req.InspectionOrderOptionReqVO;
import com.bmos.lims2.web.inspect.order.vo.resp.InspectionOrderOptionRespVO;
import com.bmos.lims2.web.inspect.order.vo.req.InspectionOrderBySchemeReqVO;
import com.bmos.lims2.web.inspect.division.vo.req.SampleDivisionPageReqVO;
import com.bmos.lims2.web.inspect.sampling.vo.response.InspectionOrderSamplingRespVO;
import com.bmos.lims2.web.inspect.sampling.vo.response.SampleRespVO;
import com.bmos.lims2.web.inspect.sample.vo.req.SampleAddReqVO;
import com.bmos.lims2.server.inspect.sample.dto.SampleDTO;
import com.bmos.lims2.server.inspect.sample.service.SampleService;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 检验单管理接口
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@RestController
@RequestMapping("/inspect/order")
@Api(tags = "检验单管理")
@Slf4j
@Validated
public class InspectionOrderController {

    @Autowired
    private InspectionOrderService inspectionOrderService;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private com.bmos.lims2.server.material.service.MaterialService materialService;

    @PostMapping("/page")
    @ApiOperation("分页查询检验单列表")
    public ResponseInfo<CommonPage<InspectionOrderRespVO>> getInspectionOrderPage(
            @RequestBody @Valid InspectionOrderPageQueryVO queryVO) {
        
        InspectionOrderPageQueryDTO queryDTO = InspectionOrderWebConverter.INSTANCE.voToPageQueryDTO(queryVO);
        // 解析物料ID集合：优先 materialId 其次 categoryId(启用检品)
        java.util.List<Long> materialIds = null;
        if (queryVO.getMaterialId() != null) {
            materialIds = java.util.Collections.singletonList(queryVO.getMaterialId());
        } else if (queryVO.getCategoryId() != null) {
            java.util.List<com.bmos.lims2.server.material.dto.MaterialDTO> materials = materialService.getAllByCategoryId(queryVO.getCategoryId());
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(materials)) {
                materialIds = materials.stream()
                        .filter(m -> java.lang.Boolean.TRUE.equals(m.getStatus()))
                        .map(com.bmos.mybatis.dataobject.BaseDO::getId)
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            queryDTO.setMaterialIds(materialIds);
        }
        // 若按分类未解析到任何启用检品，直接返回空分页
        if (queryVO.getMaterialId() == null && queryVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<InspectionOrderRespVO> empty = new CommonPage<>();
            empty.setPageNum(queryVO.getPageNum());
            empty.setPageSize(queryVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        
        CommonPage<InspectionOrderDTO> page = inspectionOrderService.getInspectionOrderPage(queryDTO);
        CommonPage<InspectionOrderRespVO> commonPage = BeanUtil.copyProperties(page, CommonPage.class);
        commonPage.setList(BeanUtil.copyToList(page.getList(), InspectionOrderRespVO.class));
        return ResponseInfo.success(commonPage);
    }

    @PostMapping("/report-page")
    @ApiOperation("报告生成页面分页查询检验单（仅常规请验单，状态固定为已完成/已终止）")
    public ResponseInfo<CommonPage<InspectionOrderRespVO>> getReportPageOrders(
            @RequestBody @Valid InspectionOrderPageQueryVO queryVO) {
        InspectionOrderPageQueryDTO queryDTO = InspectionOrderWebConverter.INSTANCE.voToPageQueryDTO(queryVO);
        java.util.List<Long> materialIds = null;
        if (queryVO.getMaterialId() != null) {
            materialIds = java.util.Collections.singletonList(queryVO.getMaterialId());
        } else if (queryVO.getCategoryId() != null) {
            java.util.List<com.bmos.lims2.server.material.dto.MaterialDTO> materials = materialService.getAllByCategoryId(queryVO.getCategoryId());
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(materials)) {
                materialIds = materials.stream()
                        .filter(m -> java.lang.Boolean.TRUE.equals(m.getStatus()))
                        .map(com.bmos.mybatis.dataobject.BaseDO::getId)
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            queryDTO.setMaterialIds(materialIds);
        }
        if (queryVO.getMaterialId() == null && queryVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<InspectionOrderRespVO> empty = new CommonPage<>();
            empty.setPageNum(queryVO.getPageNum());
            empty.setPageSize(queryVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        CommonPage<InspectionOrderDTO> page = inspectionOrderService.getReportPageOrders(queryDTO);
        CommonPage<InspectionOrderRespVO> commonPage = BeanUtil.copyProperties(page, CommonPage.class);
        commonPage.setList(BeanUtil.copyToList(page.getList(), InspectionOrderRespVO.class));
        return ResponseInfo.success(commonPage);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询检验单详情")
    public ResponseInfo<InspectionOrderRespVO> getInspectionOrderById(
            @PathVariable @NotNull(message = "检验单ID不能为空") Long id) {
        
        InspectionOrderDTO orderDTO = inspectionOrderService.getInspectionOrderById(id);
        InspectionOrderRespVO respVO = InspectionOrderWebConverter.INSTANCE.dtoToRespVO(orderDTO);
        
        return ResponseInfo.success(respVO);
    }

    @PostMapping("/save")
    @ApiOperation("保存检验单（新增或更新）")
    public ResponseInfo<Long> saveInspectionOrder(@RequestBody @Valid InspectionOrderSaveVO saveVO) {
        
        InspectionOrderSaveDTO saveDTO = InspectionOrderWebConverter.INSTANCE.voToSaveDTO(saveVO);
        Long id = inspectionOrderService.saveInspectionOrder(saveDTO);
        
        return ResponseInfo.success(id);
    }

    @PostMapping("/confirm/{id}")
    @ApiOperation("确认检验单")
    public ResponseInfo<Void> confirmInspectionOrder(
            @PathVariable @NotNull(message = "检验单ID不能为空") Long id) {
        
        inspectionOrderService.confirmInspectionOrder(id);
        return ResponseInfo.success();
    }

    @PostMapping("/batch-confirm")
    @ApiOperation("批量确认检验单")
    public ResponseInfo<Void> batchConfirmInspectionOrder(
            @RequestBody @Valid InspectionOrderBatchConfirmVO confirmVO) {
        
        inspectionOrderService.batchConfirmInspectionOrder(confirmVO.getIds());
        return ResponseInfo.success();
    }

    @PostMapping("/terminate")
    @ApiOperation("终止检验单")
    public ResponseInfo<Void> terminateInspectionOrder(
            @RequestBody @Valid InspectionOrderTerminateVO terminateVO) {
        
        inspectionOrderService.terminateInspectionOrder(terminateVO.getId(), terminateVO.getTerminateReason());
        return ResponseInfo.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除检验单")
    public ResponseInfo<Void> deleteInspectionOrder(
            @PathVariable @NotNull(message = "检验单ID不能为空") Long id) {
        
        inspectionOrderService.deleteInspectionOrder(id);
        return ResponseInfo.success();
    }

    @GetMapping("/print/{id}")
    @ApiOperation("打印检验单PDF")
    public ResponseEntity<byte[]> printInspectionOrder(
            @PathVariable @NotNull(message = "检验单ID不能为空") Long id) {
        
        byte[] pdfBytes = inspectionOrderService.generateInspectionOrderPdf(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "inspection-order-" + id + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/order-no/{orderNo}")
    @ApiOperation("根据检验单号查询检验单")
    public ResponseInfo<InspectionOrderRespVO> getInspectionOrderByOrderNo(
            @PathVariable @NotNull(message = "检验单号不能为空") String orderNo) {
        
        InspectionOrderDTO orderDTO = inspectionOrderService.getInspectionOrderByOrderNo(orderNo);
        if (orderDTO == null) {
            return ResponseInfo.success(null);
        }
        
        InspectionOrderRespVO respVO = BeanUtil.copyProperties(orderDTO, InspectionOrderRespVO.class);
        return ResponseInfo.success(respVO);
    }

    @GetMapping("/exists/{id}")
    @ApiOperation("校验检验单是否存在")
    public ResponseInfo<Boolean> existsInspectionOrder(
            @PathVariable @NotNull(message = "检验单ID不能为空") Long id) {
        
        boolean exists = inspectionOrderService.existsInspectionOrder(id);
        return ResponseInfo.success(exists);
    }

    @GetMapping("/templates/{materialId}")
    @ApiOperation("根据检品ID获取可用的请验单模板列表")
    public ResponseInfo<List<InspectionOrderTemplateRespVO>> getAvailableTemplates(
            @PathVariable @NotNull(message = "检品ID不能为空") Long materialId) {
        
        List<InspectionOrderTemplateDTO> templateList = inspectionOrderService.getAvailableTemplatesByMaterialId(materialId);
        List<InspectionOrderTemplateRespVO> respList = BeanUtil.copyToList(templateList, InspectionOrderTemplateRespVO.class);
        
        return ResponseInfo.success(respList);
    }

    @GetMapping("/{orderId}/sample-detail")
    @ApiOperation("查看请验单取样详情")
    public ResponseInfo<InspectionOrderSamplingRespVO> getInspectionOrderSamplingDetail(
            @PathVariable @NotNull(message = "请验单ID不能为空") Long orderId) {
        
        // 获取请验单详情
        InspectionOrderDTO orderDTO = inspectionOrderService.getInspectionOrderById(orderId);
        if (orderDTO == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_ORDER_NOT_EXITS);
        }
        
        // 获取样品列表
        List<SampleDTO> samples = sampleService.getSamplesByInspectionOrderId(orderId);
        
        // 构建返回结果
        InspectionOrderSamplingRespVO respVO = BeanUtil.copyProperties(orderDTO, InspectionOrderSamplingRespVO.class);
        respVO.setSamples(samples.stream()
                .map(sample -> BeanUtil.copyProperties(sample, SampleRespVO.class))
                .collect(Collectors.toList()));
        
        return ResponseInfo.success(respVO);
    }

    @GetMapping("/confirmed/count")
    @ApiOperation("查询待取样的请验单数量（用于取样登记页签徽标）")
    public ResponseInfo<Long> countConfirmedOrders() {
        return ResponseInfo.success(inspectionOrderService.countConfirmedOrders());
    }

    @PostMapping("/confirmed")
    @ApiOperation("查询已确认的请验单列表（用于取样）")
    public ResponseInfo<CommonPage<InspectionOrderSamplingRespVO>> getConfirmedInspectionOrders(@RequestBody @Valid InspectionOrderPageQueryVO queryVO) {
        InspectionOrderPageQueryDTO dto = BeanUtil.copyProperties(queryVO, InspectionOrderPageQueryDTO.class);
        // 解析物料ID集合
        java.util.List<Long> materialIds = null;
        if (queryVO.getMaterialId() != null) {
            materialIds = java.util.Collections.singletonList(queryVO.getMaterialId());
        } else if (queryVO.getCategoryId() != null) {
            java.util.List<com.bmos.lims2.server.material.dto.MaterialDTO> materials = materialService.getAllByCategoryId(queryVO.getCategoryId());
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(materials)) {
                materialIds = materials.stream()
                        .filter(m -> java.lang.Boolean.TRUE.equals(m.getStatus()))
                        .map(com.bmos.mybatis.dataobject.BaseDO::getId)
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            dto.setMaterialIds(materialIds);
        }
        // 若按分类未解析到任何启用检品，直接返回空分页
        if (queryVO.getMaterialId() == null && queryVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<InspectionOrderSamplingRespVO> empty = new CommonPage<>();
            empty.setPageNum(queryVO.getPageNum());
            empty.setPageSize(queryVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        CommonPage<InspectionOrderDTO> page = inspectionOrderService.getConfirmedOrders(dto);

        // 转换为响应VO
        CommonPage<InspectionOrderSamplingRespVO> result = new CommonPage<>();
        result.setPageNum(page.getPageNum());
        result.setPageSize(page.getPageSize());
        result.setTotal(page.getTotal());
        result.setList(page.getList().stream()
                .map(order -> BeanUtil.copyProperties(order, InspectionOrderSamplingRespVO.class))
                .collect(Collectors.toList()));

        return ResponseInfo.success(result);
    }


    @ApiOperation("分页查询待接收的检验单列表")
    @PostMapping("/receive/page")
    public ResponseInfo<CommonPage<OrderPageRespVO>> getPendingReceiveOrders(
            @RequestBody @Valid SampleReceivePageReqVO reqVO) {

        SampleReceivePageQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, SampleReceivePageQueryDTO.class);
        // 解析物料ID集合
        java.util.List<Long> materialIds = null;
        if (reqVO.getMaterialId() != null) {
            materialIds = java.util.Collections.singletonList(reqVO.getMaterialId());
        } else if (reqVO.getCategoryId() != null) {
            java.util.List<com.bmos.lims2.server.material.dto.MaterialDTO> materials = materialService.getAllByCategoryId(reqVO.getCategoryId());
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(materials)) {
                materialIds = materials.stream()
                        .filter(m -> java.lang.Boolean.TRUE.equals(m.getStatus()))
                        .map(com.bmos.mybatis.dataobject.BaseDO::getId)
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            queryDTO.setMaterialIds(materialIds);
        }
        // 若按分类未解析到任何启用检品，直接返回空分页
        if (reqVO.getMaterialId() == null && reqVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<OrderPageRespVO> empty = new CommonPage<>();
            empty.setPageNum(reqVO.getPageNum());
            empty.setPageSize(reqVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        CommonPage<OrderPageDTO> result = inspectionOrderService.getPendingReceiveOrders(queryDTO);

        // 转换为响应VO
        CommonPage<OrderPageRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(result.getPageNum());
        respPage.setPageSize(result.getPageSize());
        respPage.setTotal(result.getTotal());
        respPage.setList(result.getList().stream()
                .map(dto -> BeanUtil.copyProperties(dto, OrderPageRespVO.class))
                .collect(Collectors.toList()));

        return ResponseInfo.success(respPage);
    }


    @ApiOperation("分页查询待分样检验单列表")
    @PostMapping("/division/page")
    public ResponseInfo<CommonPage<OrderPageRespVO>> pageSampleDivisionOrders(
            @Valid @RequestBody SampleDivisionPageReqVO reqVO) {

        SampleDivisionPageQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, SampleDivisionPageQueryDTO.class);
        // 解析物料ID集合
        java.util.List<Long> materialIds = null;
        if (reqVO.getMaterialId() != null) {
            materialIds = java.util.Collections.singletonList(reqVO.getMaterialId());
        } else if (reqVO.getCategoryId() != null) {
            java.util.List<com.bmos.lims2.server.material.dto.MaterialDTO> materials = materialService.getAllByCategoryId(reqVO.getCategoryId());
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(materials)) {
                materialIds = materials.stream()
                        .filter(m -> java.lang.Boolean.TRUE.equals(m.getStatus()))
                        .map(com.bmos.mybatis.dataobject.BaseDO::getId)
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            queryDTO.setMaterialIds(materialIds);
        }
        // 若按分类未解析到任何启用检品，直接返回空分页
        if (reqVO.getMaterialId() == null && reqVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<OrderPageRespVO> empty = new CommonPage<>();
            empty.setPageNum(reqVO.getPageNum());
            empty.setPageSize(reqVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        CommonPage<OrderPageDTO> result = inspectionOrderService.pageSampleDivisionOrders(queryDTO);
        // 转换为响应VO
        CommonPage<OrderPageRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(result.getPageNum());
        respPage.setPageSize(result.getPageSize());
        respPage.setTotal(result.getTotal());
        respPage.setList(result.getList().stream()
                .map(dto -> BeanUtil.copyProperties(dto, OrderPageRespVO.class))
                .collect(Collectors.toList()));

        return ResponseInfo.success(respPage);
    }

    @ApiOperation("联动下拉：根据检品/方案版本/请验时间筛选检验单（按请验时间倒序）")
    @PostMapping("/options")
    public ResponseInfo<CommonPage<InspectionOrderOptionRespVO>> listOrderOptions(
            @RequestBody @Valid InspectionOrderOptionReqVO reqVO) {

        // DTO转换并调用服务
        com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionQueryDTO queryDTO =
                BeanUtil.copyProperties(reqVO, com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionQueryDTO.class);
        CommonPage<com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionDTO> page =
                inspectionOrderService.listOptions(queryDTO);

        // 转换为响应VO
        CommonPage<InspectionOrderOptionRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), InspectionOrderOptionRespVO.class));
        return ResponseInfo.success(respPage);
    }

    @ApiOperation("根据方案ID获取检验单下拉列表（可选传入检验单状态）")
    @PostMapping("/options/by-scheme")
    public ResponseInfo<java.util.List<InspectionOrderOptionRespVO>> listOrderOptionsByScheme(
            @RequestBody @Valid InspectionOrderBySchemeReqVO reqVO) {

        com.bmos.lims2.server.inspect.order.dto.InspectionOrderBySchemeQueryDTO queryDTO =
                BeanUtil.copyProperties(reqVO, com.bmos.lims2.server.inspect.order.dto.InspectionOrderBySchemeQueryDTO.class);
        java.util.List<com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionDTO> list =
                inspectionOrderService.listOptionsByScheme(queryDTO);

        java.util.List<InspectionOrderOptionRespVO> respList = BeanUtil.copyToList(list, InspectionOrderOptionRespVO.class);
        return ResponseInfo.success(respList);
    }
}