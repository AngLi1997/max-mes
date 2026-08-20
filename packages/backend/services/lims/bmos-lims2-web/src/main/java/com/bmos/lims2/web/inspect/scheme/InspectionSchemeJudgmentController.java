package com.bmos.lims2.web.inspect.scheme;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeJudgmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 检验方案判定配置Controller
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@RestController
@RequestMapping("/inspection-scheme/judgment")
@Api(tags = "检验方案判定配置-接口")
@Validated
public class InspectionSchemeJudgmentController {

    @Autowired
    private InspectionSchemeJudgmentService inspectionSchemeJudgmentService;

    @PostMapping("/test-expression")
    @ApiOperation("测试判定表达式")
    public ResponseInfo<Boolean> testJudgmentExpression(
            @ApiParam(value = "判定表达式", required = true) @RequestParam String expression,
            @ApiParam(value = "变量值映射", required = true) @RequestBody Map<String, Boolean> variables) {
        return ResponseInfo.success(inspectionSchemeJudgmentService.testJudgmentExpression(expression, variables));
    }

    @PostMapping("/evaluate-expression")
    @ApiOperation("计算判定表达式")
    public ResponseInfo<Boolean> evaluateJudgmentExpression(
            @ApiParam(value = "判定表达式", required = true) @RequestParam String expression,
            @ApiParam(value = "变量值映射", required = true) @RequestBody Map<String, Boolean> variables) {
        return ResponseInfo.success(inspectionSchemeJudgmentService.evaluateJudgmentExpression(expression, variables));
    }
} 