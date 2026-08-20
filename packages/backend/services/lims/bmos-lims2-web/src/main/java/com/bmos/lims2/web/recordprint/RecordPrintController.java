package com.bmos.lims2.web.recordprint;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.lims2.server.recordprint.dto.PrintItemReqDTO;
import com.bmos.lims2.server.recordprint.dto.PrintableAnalysisItemDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.recordprint.dto.RecordPrintPageReqDTO;
import com.bmos.lims2.server.recordprint.service.RecordPrintService;
import com.bmos.lims2.server.material.service.MaterialService;
import com.bmos.lims2.web.recordprint.vo.req.PrintItemVO;
import com.bmos.lims2.web.recordprint.vo.req.RecordPrintMergeReqVO;
import com.bmos.lims2.web.recordprint.vo.req.RecordPrintPageReqVO;
import com.bmos.lims2.web.recordprint.vo.resp.PrintableAnalysisItemRespVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.bmos.lims2.server.recordprint.util.PdfWatermark;
import com.bmos.lims2.server.recordprint.util.WordPdfUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.execute.parameter.feign.BusinessParameterFeign;
import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.lims2.server.platform.util.FeignUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description: 记录打印接口
 * @Author: yigaohui
 * @Date: 2025/11/25 10:45
 */
@RestController
@RequestMapping("/record-print")
@Api(tags = "记录打印")
@Slf4j
@Validated
public class RecordPrintController {

    @Autowired
    private RecordPrintService recordPrintService;

    @Autowired
    private MaterialService materialService;

    @Value("${pdf.watermark.font-path:}")
    private String pdfWatermarkFontPath;

    @Autowired
    private BusinessParameterFeign businessParameterFeign;

    @PostMapping("/page")
    @ApiOperation("分页查询可打印的检验单（已确认 + 有复核通过任务）")
    public ResponseInfo<CommonPage<InspectionOrderDTO>> page(@RequestBody @Valid RecordPrintPageReqVO reqVO) {
        RecordPrintPageReqDTO reqDTO = BeanUtil.copyProperties(reqVO, RecordPrintPageReqDTO.class);
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
        if (reqVO.getMaterialId() == null && reqVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<InspectionOrderDTO> empty = new CommonPage<>();
            empty.setPageNum(reqVO.getPageNum());
            empty.setPageSize(reqVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            reqDTO.setMaterialIds(materialIds);
        }
        CommonPage<InspectionOrderDTO> page = recordPrintService.pagePrintableInspections(reqDTO);
        return ResponseInfo.success(page);
    }

    @GetMapping("/{inspectionId}/analysis-items")
    @ApiOperation("查询检验单下可打印的分析项（复核通过、已完成，按编码排序）")
    public ResponseInfo<List<PrintableAnalysisItemRespVO>> listPrintableAnalysisItems(
            @PathVariable @NotNull(message = "检验单ID不能为空") Long inspectionId) {
        List<PrintableAnalysisItemDTO> list = recordPrintService.listPrintableAnalysisItems(inspectionId);
        List<PrintableAnalysisItemRespVO> resp = BeanUtil.copyToList(list, PrintableAnalysisItemRespVO.class);
        return ResponseInfo.success(resp);
    }

    @GetMapping("/analysis/{taskId}/preview")
    @ApiOperation("单个分析项PDF预览（带水印）")
    public void preview(
            @PathVariable @NotNull(message = "任务ID不能为空") Long taskId,
            HttpServletResponse response) throws IOException {
        byte[] pdfBytes = recordPrintService.previewAnalysisPdf(taskId);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=analysis-preview-" + taskId + ".pdf");
        response.getOutputStream().write(pdfBytes);
        response.flushBuffer();
    }

    @PostMapping("/merge")
    @ApiOperation("批量分析项合并打印PDF")
    public void merge(@RequestBody @Valid RecordPrintMergeReqVO reqVO, HttpServletResponse response) throws IOException {
        List<PrintItemReqDTO> items = reqVO.getItems().stream().map(this::toDTO).collect(Collectors.toList());
        byte[] pdfBytes = recordPrintService.mergePrintPdf(reqVO.getInspectionId(), items);
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inspection-" + reqVO.getInspectionId() + "-print.pdf");
        response.getOutputStream().write(pdfBytes);
        response.flushBuffer();
    }

    private PrintItemReqDTO toDTO(PrintItemVO vo) {
        PrintItemReqDTO dto = new PrintItemReqDTO();
        dto.setTaskId(vo.getTaskId());
        return dto;
    }
}


