package com.bmos.lims2.web.inspect.receive;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.service.InspectionOrderService;
import com.bmos.lims2.server.inspect.receive.dto.RetentionReceivePageQueryDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleCollectionListDTO;
import com.bmos.lims2.server.inspect.receive.service.SampleReceiveService;
import com.bmos.lims2.server.inspect.sample.dto.SampleDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleScanResultDTO;
import com.bmos.lims2.server.inspect.sample.service.SampleService;
import com.bmos.lims2.web.inspect.receive.vo.req.ReceiverOptionsReqVO;
import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.lims2.web.inspect.receive.vo.req.RetentionReceiveBatchReqVO;
import com.bmos.lims2.web.inspect.receive.vo.req.RetentionReceivePageReqVO;
import com.bmos.lims2.web.inspect.receive.vo.req.SampleBatchReceiveReqVO;
import com.bmos.lims2.web.inspect.sample.converter.SampleCollectionWebConverter;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleCollectionListRespVO;
import com.bmos.lims2.web.inspect.sample.converter.SampleWebConverter;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleListRespVO;
import com.bmos.lims2.web.inspect.sampling.vo.response.InspectionOrderSamplingRespVO;
import com.bmos.lims2.web.inspect.sampling.vo.response.SampleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 样品接收控制器
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
@RestController
@RequestMapping("/sample-receive")
@Api(tags = "样品接收-接口")
@Validated
@Slf4j
public class SampleReceiveController {

    @Autowired
    private SampleReceiveService sampleReceiveService;
    @Autowired
    private InspectionOrderService inspectionOrderService;

    @Autowired
    private SampleService sampleService;

    @ApiOperation("批量接收不同检验单的样品")
    @PostMapping("/samples/batch-receive")
    public ResponseInfo<Void> batchReceiveSamples(
            @RequestBody @Valid SampleBatchReceiveReqVO reqVO) {

        sampleReceiveService.batchReceiveSamples(reqVO.getSampleIds());
        return ResponseInfo.success();
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
        // 过滤出非留样且已取样的样品
        samples = samples.stream()
                .filter(item -> !Objects.equals(item.getInspectItemId(), 2000000000000000001L))
                .filter(item-> !Objects.isNull(item.getSampled()))
                .filter(SampleDTO::getSampled)
                .collect(Collectors.toList());

        // 构建返回结果
        InspectionOrderSamplingRespVO respVO = BeanUtil.copyProperties(orderDTO, InspectionOrderSamplingRespVO.class);
        respVO.setSamples(samples.stream()
                .map(sample -> BeanUtil.copyProperties(sample, SampleRespVO.class))
                .collect(Collectors.toList()));

        return ResponseInfo.success(respVO);
    }

    @ApiOperation("样品接收扫码-根据样品编号查询同请验单下已取样的样品")
    @GetMapping("/scan-all/{sampleNo}")
    public ResponseInfo<List<SampleListRespVO>> scanReceivedSamples(
            @ApiParam(value = "样品编号", required = true) @PathVariable @NotBlank String sampleNo) {

        List<SampleScanResultDTO> allSamples = sampleService.getSamplesByOrderFromSampleNo(sampleNo);
        List<SampleScanResultDTO> sampledOnly = allSamples.stream().filter(item-> !Objects.isNull(item.getSampled()))
                .filter(SampleScanResultDTO::getSampled)
                .collect(Collectors.toList());
        List<SampleListRespVO> respVOList = SampleWebConverter.INSTANCE
                .scanResultDtoListToListRespVOList(sampledOnly);
        return ResponseInfo.success(respVOList);
    }

    @ApiOperation("批量查询多个请验单的班组人员交集（领样人下拉）")
    @PostMapping("/receiver-options")
    public ResponseInfo<List<UserInfoVO>> listReceiverOptions(@RequestBody @Valid ReceiverOptionsReqVO reqVO) {
        List<String> userIds = sampleReceiveService.listReceiversBySamples(reqVO.getSampleIds());
        List<UserInfoVO> result = userIds.stream().map(uid -> {
            BaseUserDO user = UserUtils.getUser(uid);
            UserInfoVO vo = new UserInfoVO();
            vo.setUserId(uid);
            if (user != null) {
                vo.setUserName(user.getUserName());
                vo.setLoginName(user.getLoginName());
            }
            return vo;
        }).collect(Collectors.toList());
        return ResponseInfo.success(result);
    }

    @ApiOperation("分页查询留样接收列表")
    @PostMapping("/retention/page")
    public ResponseInfo<CommonPage<SampleCollectionListRespVO>> getRetentionReceivePage(
            @RequestBody @Valid RetentionReceivePageReqVO reqVO) {

        // 转换参数
        RetentionReceivePageQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, RetentionReceivePageQueryDTO.class);

        // 查询留样接收列表
        CommonPage<SampleCollectionListDTO> pageData = sampleReceiveService.getRetentionReceivePageList(queryDTO);

        // 转换结果
        List<SampleCollectionListRespVO> respList = SampleCollectionWebConverter.INSTANCE
                .dtoListToRespVOList(pageData.getList());

        CommonPage<SampleCollectionListRespVO> result = new CommonPage<>();
        result.setTotal(pageData.getTotal());
        result.setPageNum(pageData.getPageNum());
        result.setPageSize(pageData.getPageSize());
        result.setList(respList);

        return ResponseInfo.success(result);
    }

    @ApiOperation("批量接收留样样品")
    @PostMapping("/retention/batch-receive")
    public ResponseInfo<Void> batchReceiveRetentionSamples(
            @RequestBody @Valid RetentionReceiveBatchReqVO reqVO) {

        sampleReceiveService.batchReceiveRetentionSamples(reqVO.getSampleIds(), reqVO.getStorageLocation());
        return ResponseInfo.success();
    }

    @ApiOperation("扫描留样样品二维码获取样品信息")
    @GetMapping("/retention/scan")
    public ResponseInfo<SampleCollectionListRespVO> scanRetentionSample(
            @ApiParam(value = "样品编号", required = true) @RequestParam @NotBlank(message = "样品编号不能为空") String sampleNo) {

        SampleCollectionListDTO dto = sampleReceiveService.scanRetentionSample(sampleNo);
        SampleCollectionListRespVO respVO = SampleCollectionWebConverter.INSTANCE.dtoToRespVO(dto);

        return ResponseInfo.success(respVO);
    }
}
