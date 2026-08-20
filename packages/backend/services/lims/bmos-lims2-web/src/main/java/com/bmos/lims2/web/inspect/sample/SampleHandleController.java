package com.bmos.lims2.web.inspect.sample;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.sample.dto.*;
import com.bmos.lims2.server.inspect.sample.service.SampleHandleService;
import com.bmos.lims2.web.inspect.sample.vo.req.SampleBatchProcessReqVO;
import com.bmos.lims2.web.inspect.sample.vo.req.SampleBatchRecycleReqVO;
import com.bmos.lims2.web.inspect.sample.vo.req.SampleHandlePageReqVO;
import com.bmos.lims2.web.inspect.sample.vo.req.SampleHandleStatusCountReqVO;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleHandleListRespVO;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleScanResultRespVO;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleHandleStatusCountRespVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/sample-handle")
@Api(tags = "样品处理-接口")
@Validated
public class SampleHandleController {

    @Autowired
    private SampleHandleService sampleHandleService;

    @Autowired
    private com.bmos.lims2.server.material.service.MaterialService materialService;

    @PostMapping("/page")
    @ApiOperation("分页查询可进行样品处理的样品列表")
    public ResponseInfo<CommonPage<SampleHandleListRespVO>> page(@RequestBody @Valid SampleHandlePageReqVO reqVO) {
        SampleHandlePageQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, SampleHandlePageQueryDTO.class);
        // 解析物料ID集合：优先 materialId 其次 categoryId(启用检品)
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
        // 若按分类未解析到任何启用检品，直接返回空分页
        if (reqVO.getMaterialId() == null && reqVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<SampleHandleListRespVO> empty = new CommonPage<>();
            empty.setPageNum(reqVO.getPageNum());
            empty.setPageSize(reqVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            queryDTO.setMaterialIds(materialIds);
        }
        CommonPage<SampleHandleListDTO> page = sampleHandleService.getHandlePageList(queryDTO);
        CommonPage<SampleHandleListRespVO> resp = new CommonPage<>();
        resp.setTotal(page.getTotal());
        resp.setPageNum(page.getPageNum());
        resp.setPageSize(page.getPageSize());
        resp.setList(BeanUtil.copyToList(page.getList(), SampleHandleListRespVO.class));
        return ResponseInfo.success(resp);
    }

    @PostMapping("/recycle/batch")
    @ApiOperation("批量回收")
    public ResponseInfo<Void> batchRecycle(@RequestBody @Valid SampleBatchRecycleReqVO reqVO) {
        SampleBatchRecycleDTO dto = new SampleBatchRecycleDTO();
        dto.setItems(BeanUtil.copyToList(reqVO.getItems(), SampleRecycleItemDTO.class));
        sampleHandleService.batchRecycle(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/process/batch")
    @ApiOperation("批量处理")
    public ResponseInfo<Void> batchProcess(@RequestBody @Valid SampleBatchProcessReqVO reqVO) {
        SampleBatchProcessDTO dto = new SampleBatchProcessDTO();
        dto.setItems(BeanUtil.copyToList(reqVO.getItems(), SampleProcessItemDTO.class));
        sampleHandleService.batchProcess(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/detail/{sampleId}")
    @ApiOperation("查看样品详情")
    public ResponseInfo<SampleScanResultRespVO> detail(@ApiParam(value = "样品ID", required = true) @PathVariable Long sampleId) {
        SampleScanResultDTO dto = sampleHandleService.getSampleDetail(sampleId);
        SampleScanResultRespVO vo = BeanUtil.copyProperties(dto, SampleScanResultRespVO.class);
        return ResponseInfo.success(vo);
    }

    @PostMapping("/status/count")
    @ApiOperation("查询样品处理状态数量（待回收/待处理/已处理）")
    public ResponseInfo<SampleHandleStatusCountRespVO> statusCount(@RequestBody @Valid SampleHandleStatusCountReqVO reqVO) {
        SampleHandlePageQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, SampleHandlePageQueryDTO.class);
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
        // 若按分类未解析到任何启用检品，直接返回零统计
        SampleHandleStatusCountRespVO sampleHandleStatusCountRespVO = new SampleHandleStatusCountRespVO();
        if (reqVO.getMaterialId() == null && reqVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            SampleHandleStatusCountRespVO respVO = sampleHandleStatusCountRespVO;
            respVO.setToRecycleCount(0L);
            respVO.setToProcessCount(0L);
            respVO.setProcessedCount(0L);
            return ResponseInfo.success(respVO);
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            queryDTO.setMaterialIds(materialIds);
        }
        // 统计接口不需要分页参数，也不按单一状态过滤
        SampleHandleStatusCountDTO dto = sampleHandleService.getHandleStatusCount(queryDTO);
        BeanUtil.copyProperties(dto, sampleHandleStatusCountRespVO);
        return ResponseInfo.success(sampleHandleStatusCountRespVO);
    }
}


