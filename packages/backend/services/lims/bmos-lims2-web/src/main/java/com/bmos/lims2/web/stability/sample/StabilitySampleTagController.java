package com.bmos.lims2.web.stability.sample;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.stability.sample.dto.response.StabilitySamplePrintTagResultDTO;
import com.bmos.lims2.server.stability.sample.service.StabilitySampleManagementService;
import com.bmos.lims2.web.stability.sample.vo.req.StabilitySamplePrintTagReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 稳定性整体样品标签Controller
 */
@RestController
@RequestMapping("/sample/stability")
@Api(tags = "稳定性整体样品标签-接口")
@Validated
@Slf4j
public class StabilitySampleTagController {

    @Autowired
    private StabilitySampleManagementService stabilitySampleManagementService;

    @ApiOperation("获取稳定性整体样品打印标签信息")
    @PostMapping("/printTag")
    public ResponseInfo<StabilitySamplePrintTagResultDTO> getPrintTag(
            @RequestBody @Valid StabilitySamplePrintTagReqVO reqVO) {
        log.info("获取稳定性整体样品打印标签信息，样品编号：{}", reqVO.getSampleNo());
        return ResponseInfo.success(
                stabilitySampleManagementService.getPrintTagResult(reqVO.getSampleNo()));
    }

    @ApiOperation("获取稳定性周期任务取样打印标签信息")
    @PostMapping("/timepoint/printTag")
    public ResponseInfo<StabilitySamplePrintTagResultDTO> getTimepointPrintTag(
            @RequestBody @Valid StabilitySamplePrintTagReqVO reqVO) {
        log.info("获取稳定性周期任务取样打印标签信息，样品编号：{}", reqVO.getSampleNo());
        return ResponseInfo.success(
                stabilitySampleManagementService.getTimepointPrintTagResult(reqVO.getSampleNo()));
    }
}
