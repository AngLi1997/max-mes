package com.bmos.lims2.web.inspect.audit;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.audit.dto.SampleAuditPageQueryDTO;
import com.bmos.lims2.server.inspect.audit.service.SampleAuditService;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO;
import com.bmos.lims2.server.inspect.audit.dto.SampleAuditTaskDTO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import com.bmos.lims2.web.inspect.audit.vo.SampleAuditPageReqVO;

@RestController
@RequestMapping("/sample-audit")
@Api(tags = "样品审核")
@Validated
public class SampleAuditController {

    @Autowired
    private SampleAuditService sampleAuditService;
    @Autowired
    private com.bmos.lims2.server.material.service.MaterialService materialService;

    @PostMapping("/page")
    @ApiOperation("分页查询待样品审核检验单列表")
    public ResponseInfo<CommonPage<SampleAuditTaskDTO>> page(@RequestBody @Validated SampleAuditPageReqVO vo) {
        SampleAuditPageQueryDTO dto = new SampleAuditPageQueryDTO();
        dto.setOrderNo(vo.getOrderNo());
        dto.setMaterialName(vo.getMaterialName());
        dto.setMaterialCode(vo.getMaterialCode());
        dto.setBatchNo(vo.getBatchNo());
        dto.setInspectionRequestTimeStart(vo.getInspectionRequestTimeStart());
        dto.setInspectionRequestTimeEnd(vo.getInspectionRequestTimeEnd());
        dto.setPageNum(vo.getPageNum());
        dto.setPageSize(vo.getPageSize());

        // 解析物料ID集合：优先 materialId 其次 categoryId(启用检品)
        java.util.List<Long> materialIds = null;
        if (vo.getMaterialId() != null) {
            materialIds = java.util.Collections.singletonList(vo.getMaterialId());
        } else if (vo.getCategoryId() != null) {
            java.util.List<com.bmos.lims2.server.material.dto.MaterialDTO> materials = materialService.getAllByCategoryId(vo.getCategoryId());
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(materials)) {
                materialIds = materials.stream()
                        .filter(m -> java.lang.Boolean.TRUE.equals(m.getStatus()))
                        .map(com.bmos.mybatis.dataobject.BaseDO::getId)
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        // 若按分类未解析到任何启用检品，直接返回空分页
        if (vo.getMaterialId() == null && vo.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<SampleAuditTaskDTO> empty = new CommonPage<>();
            empty.setPageNum(vo.getPageNum());
            empty.setPageSize(vo.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            dto.setMaterialIds(materialIds);
        }
        return ResponseInfo.success(sampleAuditService.pagePendingSampleAuditOrders(dto));
    }

    @GetMapping("/detail/{orderId}")
    @ApiOperation("样品审核详情（按检验项目分组，仅返回分组）")
    public ResponseInfo<InspectionOrderEntryDTO> detail(@PathVariable @NotNull Long orderId) {
        return ResponseInfo.success(sampleAuditService.getSampleAuditDetail(orderId));
    }

    @PostMapping("/start/{orderId}")
    @ApiOperation("发起样品审核流程")
    public ResponseInfo<String> start(@PathVariable @NotNull Long orderId) {
        return ResponseInfo.success(sampleAuditService.startSampleAudit(orderId));
    }

    @PostMapping("/reject")
    @ApiOperation("样品审核退回指定任务")
    public ResponseInfo<Void> reject(@RequestBody @Validated com.bmos.lims2.server.inspect.audit.dto.SampleAuditRejectDTO dto) {
        sampleAuditService.rejectTasks(dto);
        return ResponseInfo.success();
    }
}


