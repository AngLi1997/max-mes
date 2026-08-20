package com.bmos.lims2.web.inspect.sampling;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.service.InspectionOrderService;
import com.bmos.lims2.server.inspect.sample.dto.SampleAddDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleBatchUpdateDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleUpdateDTO;
import com.bmos.lims2.server.inspect.sample.service.SampleService;
import com.bmos.lims2.web.inspect.sampling.vo.request.SampleAddVO;
import com.bmos.lims2.web.inspect.sampling.vo.request.SampleBatchUpdateReqVO;
import com.bmos.lims2.web.inspect.sampling.vo.request.SampleUpdateReqVO;
import com.bmos.lims2.web.inspect.sampling.vo.response.SampleRespVO;
import com.bmos.lims2.web.inspect.sampling.vo.response.InspectionOrderSamplingRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 取样管理控制器
 *
 * @author yigaohui
 * @since 2025/01/29 16:00
 */
@Api(tags = "取样管理")
@RestController
@RequestMapping("/inspect/sampling")
@Validated
public class SamplingController {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private InspectionOrderService inspectionOrderService;

    @ApiOperation("获取请验单样品列表")
    @GetMapping("/orders/{orderId}/samples")
    public ResponseInfo<List<SampleRespVO>> getSamplesByOrderId(
            @PathVariable @NotNull Long orderId) {

        List<SampleDTO> samples = sampleService.getSamplesByInspectionOrderId(orderId);
        List<SampleRespVO> respVOList = samples.stream()
                .map(sample -> BeanUtil.copyProperties(sample, SampleRespVO.class))
                .collect(Collectors.toList());

        return ResponseInfo.success(respVOList);
    }

    @ApiOperation("批量更新样品信息（支持新增、更新、删除）")
    @PutMapping("/samples/batch")
    public ResponseInfo<Void> batchUpdateSamples(
            @RequestBody @Valid SampleBatchUpdateReqVO batchUpdateReqVO) {

        // 手动转换DTO
        SampleBatchUpdateDTO batchUpdateDTO = new SampleBatchUpdateDTO();
        batchUpdateDTO.setInspectionOrderId(batchUpdateReqVO.getInspectionOrderId());

        // 转换样品信息列表
        List<SampleBatchUpdateDTO.SampleInfoDTO> sampleInfos = batchUpdateReqVO.getSamples().stream()
                .map(vo -> {
                    SampleBatchUpdateDTO.SampleInfoDTO dto = new SampleBatchUpdateDTO.SampleInfoDTO();
                    dto.setSampleId(vo.getSampleId());
                    dto.setSampleNo(vo.getSampleNo());
                    dto.setInspectionItemId(vo.getInspectionItemId());
                    dto.setInspectionItemName(vo.getInspectionItemName());
                    dto.setPlanQuantity(vo.getPlannedQuantity());
                    dto.setActualQuantity(vo.getActualQuantity());
                    dto.setUnitId(vo.getUnitId());
                    dto.setRemark(vo.getRemark());
                    return dto;
                })
                .collect(Collectors.toList());

        batchUpdateDTO.setSamples(sampleInfos);
        sampleService.batchUpdateSamples(batchUpdateDTO);
        return ResponseInfo.success();
    }

    @ApiOperation("更新样品取样信息")
    @PostMapping("/sample")
    public ResponseInfo<Void> updateSampleInfo(
            @RequestBody @Valid SampleUpdateReqVO reqVO) {
        sampleService.updateSampleInfo(BeanUtil.copyProperties(reqVO, SampleUpdateDTO.class));
        return ResponseInfo.success();
    }

    @ApiOperation("取样确定")
    @PostMapping("/confirm")
    public ResponseInfo<Void> sampling(
            @RequestBody @Valid SampleUpdateReqVO reqVO) {
        // 标记样品为已取样

        sampleService.samplingConfirm(BeanUtil.copyProperties(reqVO, SampleUpdateDTO.class));
        return ResponseInfo.success();
    }

    @ApiOperation("标签打印确认（批量）")
    @PostMapping("/label/confirm")
    public ResponseInfo<Void> confirmLabelPrinted(
            @RequestBody @Valid SampleUpdateReqVO reqVO) {
        sampleService.confirmTagPrinted(BeanUtil.copyProperties(reqVO, SampleUpdateDTO.class));
        return ResponseInfo.success();
    }

    @ApiOperation("新增样品（支持实际取样量），仅返回新增的样品列表")
    @PostMapping("/orders/{orderId}/samples")
    public ResponseInfo<List<SampleRespVO>> addSample(
            @PathVariable @NotNull Long orderId,
            @RequestBody @Valid List<SampleAddVO> sampleAddVOS) {

        // 执行新增并直接返回新增结果
        List<SampleAddDTO> sampleAddDTOS = BeanUtil.copyToList(sampleAddVOS, SampleAddDTO.class);
        sampleAddDTOS.forEach(dto -> dto.setInspectionOrderId(orderId));
        List<SampleDTO> created = sampleService.addSamples(orderId, sampleAddDTOS);

        List<SampleRespVO> createdResp = created.stream()
                .map(s -> BeanUtil.copyProperties(s, SampleRespVO.class))
                .collect(java.util.stream.Collectors.toList());

        return ResponseInfo.success(createdResp);
    }

    @ApiOperation("删除未取样样品")
    @DeleteMapping("/samples/{sampleId}")
    public ResponseInfo<Void> deleteUnsampledSample(
            @ApiParam(value = "样品ID", required = true) @PathVariable @NotNull Long sampleId) {
        sampleService.deleteUnsampledSample(sampleId);
        return ResponseInfo.success();
    }

}
