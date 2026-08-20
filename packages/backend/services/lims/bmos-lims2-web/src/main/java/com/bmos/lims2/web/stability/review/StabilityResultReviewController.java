package com.bmos.lims2.web.stability.review;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.audit.FlowAuditService;
import com.bmos.lims2.server.audit.vo.TaskHistoryVO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.material.dto.MaterialDTO;
import com.bmos.lims2.server.material.service.MaterialService;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditApproveDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditDetailDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditHeaderDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditRejectDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditReturnDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityResultReviewDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityResultReviewQueryDTO;
import com.bmos.lims2.server.stability.review.service.StabilityResultReviewService;
import com.bmos.lims2.web.stability.review.vo.request.StabilityAuditApproveReqVO;
import com.bmos.lims2.web.stability.review.vo.request.StabilityAuditRejectReqVO;
import com.bmos.lims2.web.stability.review.vo.request.StabilityAuditReturnReqVO;
import com.bmos.lims2.web.stability.review.vo.request.StabilityResultReviewQueryReqVO;
import com.bmos.lims2.web.stability.review.vo.response.StabilityResultReviewRespVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 稳定性结果审核Controller
 */
@RestController
@RequestMapping("/stability-result-review")
@Api(tags = "稳定性结果审核-接口")
@Validated
public class StabilityResultReviewController {

    @Autowired
    private StabilityResultReviewService stabilityResultReviewService;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private FlowAuditService flowAuditService;

    @Autowired
    private MaterialService materialService;

    @GetMapping("/page")
    @ApiOperation("分页查询稳定性结果审核列表")
    public ResponseInfo<CommonPage<StabilityResultReviewRespVO>> page(
            @Validated StabilityResultReviewQueryReqVO reqVO) {
        StabilityResultReviewQueryDTO queryDTO = new StabilityResultReviewQueryDTO();
        queryDTO.setPageNum(reqVO.getPageNum());
        queryDTO.setPageSize(reqVO.getPageSize());
        queryDTO.setPlanCode(reqVO.getPlanCode());
        queryDTO.setOrderNo(reqVO.getOrderNo());
        queryDTO.setBatchNo(reqVO.getBatchNo());
        queryDTO.setMaterialId(reqVO.getMaterialId());
        queryDTO.setRequestTimeStart(reqVO.getRequestTimeStart());
        queryDTO.setRequestTimeEnd(reqVO.getRequestTimeEnd());

        // 解析物料ID集合：优先 materialId，其次 categoryId（取分类下启用检品）
        List<Long> materialIds = null;
        if (reqVO.getMaterialId() != null) {
            materialIds = Collections.singletonList(reqVO.getMaterialId());
        } else if (reqVO.getCategoryId() != null) {
            List<MaterialDTO> materials = materialService.getAllByCategoryId(reqVO.getCategoryId());
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(materials)) {
                materialIds = materials.stream()
                        .filter(m -> Boolean.TRUE.equals(m.getStatus()))
                        .map(com.bmos.mybatis.dataobject.BaseDO::getId)
                        .collect(Collectors.toList());
            }
        }
        // 按分类查询但分类下无启用检品，直接返回空分页
        if (reqVO.getMaterialId() == null && reqVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<StabilityResultReviewRespVO> empty = new CommonPage<>();
            empty.setPageNum(reqVO.getPageNum());
            empty.setPageSize(reqVO.getPageSize());
            empty.setTotal(0);
            empty.setList(Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            queryDTO.setMaterialIds(materialIds);
        }

        CommonPage<StabilityResultReviewDTO> dtoPage = stabilityResultReviewService.pageReviewList(queryDTO);
        CommonPage<StabilityResultReviewRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(dtoPage.getPageNum());
        respPage.setPageSize(dtoPage.getPageSize());
        respPage.setTotal(dtoPage.getTotal());
        respPage.setList(BeanUtil.copyToList(dtoPage.getList(), StabilityResultReviewRespVO.class));
        return ResponseInfo.success(respPage);
    }

    @GetMapping("/{orderId}/audit-header")
    @ApiOperation("查询稳定性审核详情页请验信息面板（稳定性头部字段）")
    public ResponseInfo<StabilityAuditHeaderDTO> auditHeader(
            @ApiParam("检验单ID") @PathVariable Long orderId) {
        return ResponseInfo.success(stabilityResultReviewService.getAuditHeader(orderId));
    }

    @GetMapping("/{orderId}/audit-detail")
    @ApiOperation("查询稳定性结果审核详情（检验任务数据，与常规样品审核结构一致）")
    public ResponseInfo<StabilityAuditDetailDTO> auditDetail(
            @ApiParam("检验单ID") @PathVariable Long orderId) {
        return ResponseInfo.success(stabilityResultReviewService.getAuditDetail(orderId));
    }

    @GetMapping("/{orderId}/audit-progress")
    @ApiOperation("查询稳定性结果审核进度")
    public ResponseInfo<List<TaskHistoryVO>> auditProgress(
            @ApiParam("检验单ID") @PathVariable Long orderId) {
        InspectionOrder order = inspectionOrderMapper.selectById(orderId);
        if (order == null || order.getSampleAuditProcessInstanceId() == null) {
            return ResponseInfo.success(Collections.emptyList());
        }
        List<TaskHistoryVO> history = flowAuditService.listTaskHistory(order.getSampleAuditProcessInstanceId());
        return ResponseInfo.success(history);
    }

    @PostMapping("/{orderId}/approve")
    @ApiOperation("审核通过（需电子签名密码验证）")
    public ResponseInfo<Void> approve(
            @ApiParam("检验单ID") @PathVariable Long orderId,
            @RequestBody @Validated StabilityAuditApproveReqVO reqVO) {
        StabilityAuditApproveDTO dto = BeanUtil.copyProperties(reqVO, StabilityAuditApproveDTO.class);
        dto.setInspectionOrderId(orderId);
        stabilityResultReviewService.approve(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/{orderId}/return")
    @ApiOperation("审核退回（退回上一节点）")
    public ResponseInfo<Void> returnToPrev(
            @ApiParam("检验单ID") @PathVariable Long orderId,
            @RequestBody @Validated StabilityAuditReturnReqVO reqVO) {
        StabilityAuditReturnDTO dto = BeanUtil.copyProperties(reqVO, StabilityAuditReturnDTO.class);
        dto.setInspectionOrderId(orderId);
        stabilityResultReviewService.returnToPrev(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/{orderId}/reject")
    @ApiOperation("审核不通过（选择具体任务退回）")
    public ResponseInfo<Void> reject(
            @ApiParam("检验单ID") @PathVariable Long orderId,
            @RequestBody @Validated StabilityAuditRejectReqVO reqVO) {
        StabilityAuditRejectDTO dto = BeanUtil.copyProperties(reqVO, StabilityAuditRejectDTO.class);
        dto.setInspectionOrderId(orderId);
        stabilityResultReviewService.reject(dto);
        return ResponseInfo.success();
    }
}
