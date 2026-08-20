package com.bmos.lims2.web.inspect.division;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.division.dto.SampleDivisionDTO;
import com.bmos.lims2.server.inspect.division.dto.SampleDivisionRemainDTO;
import com.bmos.lims2.server.inspect.division.service.SampleDivisionService;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.service.InspectionOrderService;
import com.bmos.lims2.server.inspect.sample.dto.SampleDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleScanResultDTO;
import com.bmos.lims2.server.inspect.sample.service.SampleService;
import com.bmos.lims2.web.inspect.division.vo.req.SampleDivisionReqVO;
import com.bmos.lims2.web.inspect.division.vo.resp.SampleDivisionRemainRespVO;
import com.bmos.lims2.web.inspect.sample.converter.SampleWebConverter;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleListRespVO;
import com.bmos.lims2.web.inspect.sampling.vo.response.InspectionOrderSamplingRespVO;
import com.bmos.lims2.web.inspect.sampling.vo.response.SampleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @className: DivisionController
 * @author: yigaohui
 * @date: 2025/8/19 13:52
 * @Version: 1.0
 * @description:
 */

@RestController
@RequestMapping("/division")
@Api(tags = "分样-接口")
public class DivisionController {

    @Autowired
    private SampleDivisionService sampleDivisionService;

    @Autowired
    private InspectionOrderService inspectionOrderService;
    @Autowired
    private SampleService sampleService;

    @ApiOperation("计算分样剩余量")
    @PostMapping("/calculate-remaining")
    public ResponseInfo<SampleDivisionRemainRespVO> calculateRemaining(
            @ApiParam(value = "原样品ID", required = true) @RequestParam @NotNull Long originalSampleId,
            @RequestBody List<SampleDivisionReqVO.DivisionResultVO> divisionResults) {

        // 转换为DTO
        List<SampleDivisionDTO.DivisionResultDTO> divisionResultDTOs = divisionResults.stream()
                .map(vo -> BeanUtil.copyProperties(vo, SampleDivisionDTO.DivisionResultDTO.class))
                .collect(Collectors.toList());

        SampleDivisionRemainDTO remainDTO =
                sampleDivisionService.calculateRemaining(originalSampleId, divisionResultDTOs);

        SampleDivisionRemainRespVO respVO = BeanUtil.copyProperties(remainDTO, SampleDivisionRemainRespVO.class);
        return ResponseInfo.success(respVO);
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
        // 过滤初已接收的样品
        samples = samples.stream().filter(item-> !Objects.isNull(item.getReceived()))
                .filter(SampleDTO::getReceived)
                .collect(Collectors.toList());

        // 构建返回结果
        InspectionOrderSamplingRespVO respVO = BeanUtil.copyProperties(orderDTO, InspectionOrderSamplingRespVO.class);
        respVO.setSamples(samples.stream()
                .map(sample -> BeanUtil.copyProperties(sample, SampleRespVO.class))
                .collect(Collectors.toList()));

        return ResponseInfo.success(respVO);
    }

    @ApiOperation("保存分样结果")
    @PostMapping("/save")
    public ResponseInfo<List<SampleDTO>> saveSampleDivision(
            @RequestBody @Valid SampleDivisionReqVO reqVO) {

        // 转换为DTO
        SampleDivisionDTO divisionDTO = new SampleDivisionDTO();
        divisionDTO.setOriginalSampleId(reqVO.getOriginalSampleId());

        List<SampleDivisionDTO.DivisionResultDTO> divisionResultDTOs = reqVO.getDivisionResults().stream()
                .map(vo -> BeanUtil.copyProperties(vo, SampleDivisionDTO.DivisionResultDTO.class))
                .collect(Collectors.toList());
        divisionDTO.setDivisionResults(divisionResultDTOs);

        List<SampleDTO> samples = sampleDivisionService.saveSampleDivision(divisionDTO);
        return ResponseInfo.success(samples);
    }
}
