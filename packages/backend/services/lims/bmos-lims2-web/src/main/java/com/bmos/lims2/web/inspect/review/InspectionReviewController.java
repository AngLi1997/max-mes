package com.bmos.lims2.web.inspect.review;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.entry.dto.AnalysisItemEntryDTO;
import com.bmos.lims2.server.inspect.entry.dto.AnalysisItemEntryQueryDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryQueryDTO;
import com.bmos.lims2.server.inspect.entry.vo.AnalysisItemEntryQueryVO;
import com.bmos.lims2.server.inspect.entry.vo.InspectionOrderEntryQueryVO;
import com.bmos.lims2.server.inspect.review.service.InspectionReviewService;
import com.bmos.lims2.server.inspect.review.dto.BatchReviewDTO;
import com.bmos.lims2.server.inspect.entry.service.InspectionEntryService;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/inspection/review")
@Api(tags = "检验复核")
@Validated
public class InspectionReviewController {

    @Autowired
    private InspectionReviewService reviewService;

    @Autowired
    private InspectionEntryService entryService;

    @ApiOperation("分析项复核列表查询")
    @PostMapping("/analysis-item/page")
    public ResponseInfo<CommonPage<AnalysisItemEntryDTO>> getAnalysisItemReviewPage(
            @ApiParam("查询条件") @RequestBody @Valid AnalysisItemEntryQueryVO queryVO) {
        AnalysisItemEntryQueryDTO queryDTO = entryService.setAnalysisItemEntryPermission(queryVO);
        // 强制仅待复核
        queryDTO.setUnreviewedOnly(Boolean.TRUE);
        CommonPage<AnalysisItemEntryDTO> page = reviewService.getAnalysisItemReviewPage(queryDTO);
        return ResponseInfo.success(page);
    }

    @ApiOperation("检验单复核列表查询")
    @PostMapping("/inspection-order/page")
    public ResponseInfo<CommonPage<InspectionOrderEntryDTO>> getInspectionOrderReviewPage(
            @ApiParam("查询条件") @RequestBody @Valid InspectionOrderEntryQueryVO queryVO) {
        InspectionOrderEntryQueryDTO queryDTO = entryService.setInspectionOrderEntryPermission(queryVO);
        // 强制仅待复核
        queryDTO.setUnreviewedOnly(Boolean.TRUE);
        CommonPage<InspectionOrderEntryDTO> page = reviewService.getInspectionOrderReviewPage(queryDTO);
        return ResponseInfo.success(page);
    }

    @ApiOperation("批量复核")
    @PostMapping("/batch")
    public ResponseInfo<Void> batchReview(@RequestBody @Valid BatchReviewDTO req) {
        reviewService.batchReview(req.getTaskIds(), SysUserHolder.getUser().getUserId(), req.getApprove(), req.getReason());
        return ResponseInfo.success();
    }
}


