package com.bmos.lims2.web.inspect.scheme;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeSamplingDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingSaveDTO;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeSamplingService;
import com.bmos.lims2.web.inspect.scheme.vo.request.InspectionSchemeSamplingSaveReqVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.InspectionSchemeSamplingRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检验方案取样量配置Controller
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@RestController
@RequestMapping("/inspection-scheme/sampling")
@Api(tags = "检验方案取样量配置-接口")
@Validated
public class InspectionSchemeSamplingController {

    @Autowired
    private InspectionSchemeSamplingService inspectionSchemeSamplingService;

    @GetMapping("/list/{schemeId}/{versionId}")
    @ApiOperation("获取检验方案取样量配置列表")
    public ResponseInfo<List<InspectionSchemeSamplingRespVO>> listInspectionSchemeSamplings(@PathVariable Long schemeId, @PathVariable Long versionId) {
        List<InspectionSchemeSamplingDTO> dtos = inspectionSchemeSamplingService.listInspectionSchemeSamplings(schemeId, versionId);
        List<InspectionSchemeSamplingRespVO> respVOs = BeanUtil.copyToList(dtos, InspectionSchemeSamplingRespVO.class);
        return ResponseInfo.success(respVOs);
    }

    @DeleteMapping("/{schemeId}/{versionId}")
    @ApiOperation("删除检验方案取样量配置")
    public ResponseInfo<Void> deleteInspectionSchemeSamplings(@PathVariable Long schemeId,@PathVariable Long versionId) {
        inspectionSchemeSamplingService.deleteInspectionSchemeSamplings(schemeId,versionId);
        return ResponseInfo.success();
    }
} 