package com.bmos.mes.service.trace.material.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.trace.material.service.IMaterialTraceService;
import com.bmos.mes.service.trace.material.vo.MaterialTraceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 物料追溯接口
 * @author liang
 * @version 1.0.0
 * @date 2024/11/21 09:54
 */
@RestController
@RequestMapping("/material/trace")
@Api(tags = "物料追溯接口")
public class MaterialTraceController {

    @Resource
    private IMaterialTraceService materialTraceService;

    @GetMapping("/data")
    @ApiOperation(value = "追溯数据")
    @ApiImplicitParam(name = "productPlanId", value = "生产计划id", required = true, dataType = "Long")
    public ResponseInfo<MaterialTraceVO> traceData(@RequestParam Long productPlanId) {
        return ResponseInfo.success(materialTraceService.traceData(productPlanId));
    }
}
