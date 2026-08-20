package com.bmos.lims2.web.inspect.retention;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.retention.dto.RetentionSamplePrintTagResultDTO;
import com.bmos.lims2.server.inspect.retention.service.RetentionSampleManageService;
import com.bmos.lims2.web.inspect.retention.vo.req.RetentionSamplePrintTagReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Description: 留样样品标签控制器
 * @Author: yigaohui
 * @Date: 2026/02/12
 */
@RestController
@RequestMapping("/sample/retention")
@Api(tags = "留样样品标签-接口")
@Validated
@Slf4j
public class RetentionSampleTagController {

    @Autowired
    private RetentionSampleManageService retentionSampleManageService;

    @ApiOperation("获取留样样品打印标签信息")
    @PostMapping("/printTag")
    public ResponseInfo<RetentionSamplePrintTagResultDTO> getRetentionSamplePrintTag(
            @RequestBody @Valid RetentionSamplePrintTagReqVO reqVO) {

        log.info("获取留样样品打印标签信息，样品编号：{}", reqVO.getSampleNo());

        RetentionSamplePrintTagResultDTO resultDTO =
            retentionSampleManageService.getRetentionSamplePrintTag(reqVO.getSampleNo());

        return ResponseInfo.success(resultDTO);
    }
}
