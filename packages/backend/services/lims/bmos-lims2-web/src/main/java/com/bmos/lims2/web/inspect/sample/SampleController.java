package com.bmos.lims2.web.inspect.sample;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.service.InspectionOrderService;
import com.bmos.lims2.server.inspect.sample.dto.*;
import com.bmos.lims2.server.inspect.sample.service.SampleService;
import com.bmos.lims2.web.inspect.sample.converter.SampleCollectionWebConverter;
import com.bmos.lims2.web.inspect.sample.converter.SampleWebConverter;
import com.bmos.lims2.web.inspect.sample.vo.req.SampleBatchCollectionReqVO;
import com.bmos.lims2.web.inspect.sample.vo.req.SampleCollectionPageReqVO;
import com.bmos.lims2.web.inspect.sample.vo.req.SamplePrintTagReqVO;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleCollectionListRespVO;
import com.bmos.lims2.web.inspect.sample.vo.req.SampleAddReqVO;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleScanResultRespVO;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleListRespVO;
import com.bmos.lims2.web.inspect.sampling.vo.response.InspectionOrderSamplingRespVO;
import com.bmos.lims2.web.inspect.sampling.vo.response.SampleRespVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @className: SampleController
 * @author: yigaohui
 * @date: 2025/8/18 17:57
 * @Version: 1.0
 * @description: 样品管理控制器
 */

@RestController
@RequestMapping("/sample")
@Api(tags = "样品-接口")
@Validated
public class SampleController {
    
    @Autowired
    private SampleService sampleService;

    @Autowired
    private InspectionOrderService inspectionOrderService;

    @ApiOperation("扫描样品二维码获取样品信息")
    @GetMapping("/scan")
    public ResponseInfo<SampleScanResultRespVO> scanSample(
            @ApiParam(value = "样品编号", required = true) @RequestParam @NotBlank String sampleNo) {

        SampleScanResultDTO scanResult = sampleService.scanSample(sampleNo);
        SampleScanResultRespVO respVO = BeanUtil.copyProperties(scanResult, SampleScanResultRespVO.class);

        return ResponseInfo.success(respVO);
    }


    @ApiOperation("获取样品标签信息")
    @PostMapping("/printTag")
    public ResponseInfo<SamplePrintTagResultDTO> samplePrintTag(@RequestBody SamplePrintTagReqVO reqVO) {
        SamplePrintTagResultDTO scanResult = sampleService.samplePrintTag(reqVO.getSampleNo());
        return ResponseInfo.success(scanResult);
    }


    @ApiOperation("根据样品编号查询请验单下所有样品（修改后的扫码接口）")
    @GetMapping("/division/scan-all/{sampleNo}")
    public ResponseInfo<List<SampleListRespVO>> getSamplesByOrderFromSampleNo(
            @ApiParam(value = "样品编号", required = true) @PathVariable @NotBlank String sampleNo) {
        List<SampleScanResultDTO> allSamples = sampleService.getSamplesByOrderFromSampleNo(sampleNo);
        List<SampleScanResultDTO> sampledOnly = allSamples.stream().filter(item-> !Objects.isNull(item.getReceived()))
                .filter(SampleScanResultDTO::getReceived)
                .collect(Collectors.toList());
        List<SampleListRespVO> respVOList = SampleWebConverter.INSTANCE
                .scanResultDtoListToListRespVOList(sampledOnly);
        return ResponseInfo.success(respVOList);
    }

    @ApiOperation("分页查询可领取的样品列表")
    @PostMapping("/collection/page")
    public ResponseInfo<CommonPage<SampleCollectionListRespVO>> getCollectionPageList(
            @ApiParam(value = "分页查询参数", required = true) @RequestBody @Valid SampleCollectionPageReqVO reqVO) {
        
        // 转换查询参数
        SampleCollectionPageQueryDTO queryDTO = SampleCollectionWebConverter.INSTANCE.voToPageQueryDTO(reqVO);
        
        // 执行查询
        CommonPage<SampleCollectionListDTO> pageResult = sampleService.getCollectionPageList(queryDTO);
        
        // 转换响应结果
        CommonPage<SampleCollectionListRespVO> respPage = new CommonPage<>();
        respPage.setTotal(pageResult.getTotal());
        respPage.setPageNum(pageResult.getPageNum());
        respPage.setPageSize(pageResult.getPageSize());
        respPage.setList(BeanUtil.copyToList(pageResult.getList(), SampleCollectionListRespVO.class));
        respPage.setList(SampleCollectionWebConverter.INSTANCE.dtoListToRespVOList(pageResult.getList()));
        
        return ResponseInfo.success(respPage);
    }

    @ApiOperation("批量领取样品")
    @PostMapping("/collection/batch")
    public ResponseInfo<Void> batchCollectSamples(
            @ApiParam(value = "批量领取参数", required = true) @RequestBody @Valid SampleBatchCollectionReqVO reqVO) {
        
        // 转换请求参数
        SampleBatchCollectionDTO batchCollectionDTO = SampleCollectionWebConverter.INSTANCE.voToBatchCollectionDTO(reqVO);
        
        // 执行批量领取
        sampleService.batchCollectSamples(batchCollectionDTO);
        
        return ResponseInfo.success();
    }

    

    @ApiOperation("在请验单下新增样品（支持实际取样量），并返回/{orderId}/sample-detail结构")
    @PostMapping("/orders/{orderId}/samples")
    public ResponseInfo<List<SampleRespVO>> addSamplesUnderOrder(
            @ApiParam(value = "请验单ID", required = true) @PathVariable Long orderId,
            @ApiParam(value = "新增样品参数列表", required = true) @RequestBody @Valid List<SampleAddDTO> sampleAddList) {

        // 规范：每条SampleAddDTO必须带上inspectionOrderId，与路径一致
        for (SampleAddDTO dto : sampleAddList) {
            dto.setInspectionOrderId(orderId);
        }

        // 执行新增
        List<SampleDTO> sampleDTOS = sampleService.addSamples(orderId, sampleAddList);

        List<SampleRespVO> sampleRespList = sampleDTOS.stream()
                .map(s -> BeanUtil.copyProperties(s, SampleRespVO.class))
                .collect(java.util.stream.Collectors.toList());


        return ResponseInfo.success(sampleRespList);
    }
}