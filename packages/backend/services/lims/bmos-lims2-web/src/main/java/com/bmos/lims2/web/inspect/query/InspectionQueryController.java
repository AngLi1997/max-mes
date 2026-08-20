package com.bmos.lims2.web.inspect.query;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderPageQueryDTO;
import com.bmos.lims2.server.inspect.query.service.InspectionDetailDTO;
import com.bmos.lims2.server.inspect.query.service.OrderInfoDTO;
import com.bmos.lims2.server.inspect.query.service.InspectionQueryService;
import com.bmos.lims2.server.inspect.entry.dto.InspectItemTabDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO;
import com.bmos.lims2.server.inspect.entry.dto.EntryRecordsGroupedByAnalysisItemDTO;
import com.bmos.lims2.server.inspect.sample.ledger.dto.SampleLedgerListDTO;
import com.bmos.lims2.server.inspect.sample.ledger.dto.SampleLedgerPageQueryDTO;
import com.bmos.lims2.web.inspect.query.vo.resp.OrderInfoRespVO;
import com.bmos.lims2.web.inspect.query.vo.resp.OrderStatusFlagsVO;
import com.bmos.lims2.web.inspect.query.vo.resp.ReportTaskRespVO;
import com.bmos.lims2.web.inspect.query.vo.resp.SampleBriefRespVO;
import com.bmos.lims2.web.inspect.query.vo.resp.SampleLedgerRespVO;
import com.bmos.lims2.web.inspect.query.converter.InspectionQueryWebConverter;
import com.bmos.lims2.web.inspect.query.vo.req.SampleLedgerPageQueryVO;
import com.bmos.lims2.web.inspect.query.vo.req.EntryByItemQueryVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.lims2.web.inspect.order.converter.InspectionOrderWebConverter;
import com.bmos.lims2.web.inspect.order.vo.req.InspectionOrderPageQueryVO;
import com.bmos.lims2.web.inspect.order.vo.resp.InspectionOrderRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Description: 检验查询接口
 * @Author: yigaohui
 * @Date: 2025/09/05 10:40
 */
@RestController
@RequestMapping("/inspect/query")
@Api(tags = "检验查询")
@Validated
public class InspectionQueryController {

	private final InspectionQueryService inspectionQueryService;
    @Autowired
    private com.bmos.lims2.server.material.service.MaterialService materialService;

	public InspectionQueryController(InspectionQueryService inspectionQueryService) {
		this.inspectionQueryService = inspectionQueryService;
	}

	@PostMapping("/page")
	@ApiOperation("分页查询检验单列表")
	public ResponseInfo<CommonPage<InspectionOrderRespVO>> page(@RequestBody @Valid InspectionOrderPageQueryVO queryVO) {
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
		CommonPage<InspectionOrderDTO> page = inspectionQueryService.page(queryDTO);
		CommonPage<InspectionOrderRespVO> resp = BeanUtil.copyProperties(page, CommonPage.class);
		resp.setList(BeanUtil.copyToList(page.getList(), InspectionOrderRespVO.class));
		return ResponseInfo.success(resp);
	}

	@GetMapping("/order/{orderId}")
	@ApiOperation("检验单信息（含状态标志位）")
	public ResponseInfo<OrderInfoRespVO> orderInfo(@PathVariable Long orderId) {
		OrderInfoDTO dto = inspectionQueryService.getOrderInfo(orderId);
		OrderInfoRespVO vo = new OrderInfoRespVO();
		vo.setOrder(BeanUtil.copyProperties(dto.getOrder(), InspectionOrderRespVO.class));
		OrderStatusFlagsVO flags = new OrderStatusFlagsVO();
		flags.setRequested(dto.getFlags().isRequested());
		flags.setSampled(dto.getFlags().isSampled());
		flags.setInspected(dto.getFlags().isInspected());
		flags.setReported(dto.getFlags().isReported());
		vo.setRequestStartTime(dto.getRequestStartTime());
		vo.setRequestEndTime(dto.getRequestEndTime());
		vo.setInspectionEndTime(dto.getInspectionEndTime());
		vo.setInspectionStartTime(dto.getInspectionStartTime());
		vo.setSamplingStartTime(dto.getSamplingStartTime());
		vo.setSamplingEndTime(dto.getSamplingEndTime());
		vo.setReportStartTime(dto.getReportStartTime());
		vo.setReportEndTime(dto.getReportEndTime());
		vo.setStabilityPlanCode(dto.getStabilityPlanCode());
		vo.setStabilitySchemeName(dto.getStabilitySchemeName());
		vo.setStabilitySchemeVersionNo(dto.getStabilitySchemeVersionNo());
		vo.setStabilityPlannedDate(dto.getStabilityPlannedDate());
		vo.setStabilityPlanCreator(dto.getStabilityPlanCreator());
		vo.setStabilityPlanRemark(dto.getStabilityPlanRemark());
		vo.setStabilitySchemeCode(dto.getStabilitySchemeCode());
		vo.setFlags(flags);
		return ResponseInfo.success(vo);
	}

	@PostMapping("/sample-ledger/page")
	@ApiOperation("分页查询样品台账")
	public ResponseInfo<CommonPage<SampleLedgerListDTO>> sampleLedgerPage(@RequestBody @Valid SampleLedgerPageQueryVO queryVO) {
		SampleLedgerPageQueryDTO queryDTO = InspectionQueryWebConverter.INSTANCE.voToSampleLedgerPageQueryDTO(queryVO);
		return ResponseInfo.success(inspectionQueryService.sampleLedgerPage(queryDTO));
	}

	@GetMapping("/reports/{orderId}")
	@ApiOperation("查询检验单的报告信息")
	public ResponseInfo<java.util.List<ReportTaskRespVO>> reports(@PathVariable Long orderId) {
		// 使用服务层获取已生成报告的投影（包含模板/版本/时间等字段）
		java.util.List<com.bmos.lims2.server.report.dto.ReportGeneratedItemDTO> items =
				inspectionQueryService.listGeneratedReportsByOrderId(orderId);
		java.util.List<ReportTaskRespVO> list = new java.util.ArrayList<>();
		for (com.bmos.lims2.server.report.dto.ReportGeneratedItemDTO it : items) {
			ReportTaskRespVO vo = new ReportTaskRespVO();
			vo.setTaskId(it.getTaskId());
			vo.setInspectionOrderId(orderId);
			vo.setTemplateId(it.getTemplateId());
			vo.setTemplateVersionId(it.getTemplateVersionId());
			vo.setTemplateVersionNo(it.getTemplateVersionNo());
			vo.setTemplateName(it.getTemplateName());
			vo.setReportNo(it.getReportNo());
			vo.setStatus(it.getStatus());
			vo.setLifecycleStatus(it.getLifecycleStatus());
			vo.setPath(it.getPath());
			vo.setEndTime(it.getEndTime());
			vo.setReportApproved(it.getReportApproved());
			vo.setReportApprovalTime(it.getReportApprovalTime());
			vo.setReportRemark(it.getReportRemark());
			vo.setProcessInstanceId(it.getProcessInstanceId());
			vo.setGeneratedBy(it.getGeneratedBy());
			list.add(vo);
		}
		return ResponseInfo.success(list);
	}

	@GetMapping("/entry/tabs/{orderId}")
	@ApiOperation("检验信息-检验项目分页签")
	public ResponseInfo<java.util.List<InspectItemTabDTO>> entryTabs(@PathVariable Long orderId) {
		return ResponseInfo.success(inspectionQueryService.listInspectItemTabs(orderId));
	}

	@GetMapping("/samples/{orderId}")
	@ApiOperation("检验单样品列表（用于详情页）")
	public ResponseInfo<java.util.List<SampleBriefRespVO>> listOrderSamples(@PathVariable Long orderId) {
		java.util.List<com.bmos.lims2.server.inspect.query.service.OrderSampleDTO> list = inspectionQueryService.listOrderSamples(orderId);
		java.util.List<SampleBriefRespVO> resp = cn.hutool.core.bean.BeanUtil.copyToList(list, SampleBriefRespVO.class);
		return ResponseInfo.success(resp);
	}

	@PostMapping("/entry/list-by-item")
	@ApiOperation("检验信息-按检验项目查询录入记录（按分析项分组，不分页）")
	public ResponseInfo<java.util.List<EntryRecordsGroupedByAnalysisItemDTO>> listEntriesByItem(@RequestBody @Valid EntryByItemQueryVO vo) {
		return ResponseInfo.success(inspectionQueryService.listEntriesByItem(InspectionQueryWebConverter.INSTANCE.voToEntryByItemQueryDTO(vo)));
	}
}


