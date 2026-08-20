package com.bmos.mes.service.plan.rule.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.plan.rule.dto.CodeRulePageDTO;
import com.bmos.mes.service.plan.rule.dto.CodeRuleSaveDTO;
import com.bmos.mes.service.plan.rule.dto.CodeRuleUpdateDTO;
import com.bmos.mes.service.plan.rule.service.CodeRuleService;
import com.bmos.mes.service.plan.rule.vo.CodeRulePageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plan/code/rule")
@Api(tags = "生产计划编码规则")
public class CodeRuleController {
    @Autowired
    private CodeRuleService codeRuleService;

    @ApiOperation("分页列表")
    @GetMapping("/page")
    public ResponseInfo<CommonPage<CodeRulePageVO>> page(CodeRulePageDTO dto) {
        return ResponseInfo.success(
            CommonPage.convertPage(codeRuleService.page(dto))
        );
    }

    @PostMapping("/detailCode/{code}")
    @ApiOperation("编码规则code查询工艺集合")
    @OperationLog
    public ResponseInfo<List<Long>> detailCode(@ApiParam(name = "code", value = "编码", required = true) @PathVariable String code) {

        return ResponseInfo.success(codeRuleService.detailCode(code));

    }

    @PostMapping("/save")
    @ApiOperation("批量配置")
    @OperationLog
    public ResponseInfo<Void> save(@RequestBody @Validated CodeRuleSaveDTO dto) {
        codeRuleService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("编辑")
    @OperationLog
    public ResponseInfo<Void> update(@RequestBody @Validated CodeRuleUpdateDTO dto) {
        codeRuleService.update(dto);
        return ResponseInfo.success();
    }
}
