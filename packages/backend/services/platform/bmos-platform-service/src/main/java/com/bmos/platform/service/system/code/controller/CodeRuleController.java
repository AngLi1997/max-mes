package com.bmos.platform.service.system.code.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.code.dto.ConfirmNoInfoDTO;
import com.bmos.platform.service.system.code.dto.*;
import com.bmos.platform.service.system.code.service.CodeRuleService;
import com.bmos.platform.service.system.code.vo.BatchNextCodeVO;
import com.bmos.platform.service.system.code.vo.CodeRulePageVO;
import com.bmos.platform.service.system.code.vo.DetailCodeRuleVersionDetailVO;
import com.bmos.platform.service.system.code.vo.NextCodeVO;
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
@RequestMapping("/codeRule")
@Api(tags = "编码规则")
public class CodeRuleController {
    @Autowired
    private CodeRuleService codeRuleService;

    @GetMapping("/page")
    @ApiOperation("编码规则分页列表")
    public ResponseInfo<CommonPage<CodeRulePageVO>> page(CodeRulePageDTO dto) {
        return ResponseInfo.success(
            CommonPage.convertPage(codeRuleService.page(dto))
        );
    }

    @GetMapping("/list")
    @ApiOperation("编码规则列表")
    public ResponseInfo<List<CodeRulePageVO>> list(CodeRuleListDTO dto) {
        return ResponseInfo.success(codeRuleService.list(dto));
    }

    @GetMapping("/detail/{id}")
    @ApiParam(value = "id", name = "版本id", required = true)
    @ApiOperation("编码规则详情")
    public ResponseInfo<DetailCodeRuleVersionDetailVO> detail(@PathVariable Long id) {
        return ResponseInfo.success(codeRuleService.detail(id));
    }

    @PostMapping("/permission")
    @ApiOperation("数据权限")
    public ResponseInfo<Void> permission(@RequestBody @Validated CodeRulePermissionDTO dto) {
        codeRuleService.permission(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/permission/detail/{id}")
    @ApiOperation("数据权限查询")
    public ResponseInfo<List<Long>> permissionDetail(@PathVariable Long id) {
        return ResponseInfo.success(codeRuleService.permissionDetail(id));
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> save(@RequestBody @Validated CodeRuleSaveDTO dto) {
        dto.isValidated();
        codeRuleService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("更新")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> update(@RequestBody @Validated CodeRuleUpdateDTO dto) {
        dto.isValidated();
        codeRuleService.update(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/getNextNo")
    @ApiOperation("获取下一个编号 未确认使用的不包含")
    public ResponseInfo<NextCodeVO> getNextNo(@RequestBody @Validated NextUseCodeDTO dto) {
        return ResponseInfo.success(codeRuleService.getNextNo(dto));
    }

    @PostMapping("/getNextUseNo")
    @ApiOperation("获取下一个编号 未确认使用的编号会重复返回")
    public ResponseInfo<NextCodeVO> getNextUseNo(@RequestBody @Validated NextUseCodeDTO dto) {
        return ResponseInfo.success(codeRuleService.getNextUseNo(dto));
    }

    @PostMapping("/getBatchNextUseNo")
    @ApiOperation("批量获取下一个编号 未确认使用的编号会重复返回")
    public ResponseInfo<BatchNextCodeVO> getBatchNextUseNo(@RequestBody @Validated BatchNextUseCodeDTO dto) {
        return ResponseInfo.success(codeRuleService.getBatchNextUseNo(dto));
    }


    @PostMapping("/confirmNo")
    @ApiOperation("确认编号回调")
    public ResponseInfo<Void> confirmNo(@RequestBody @Validated ConfirmNoInfoDTO dto) {
        codeRuleService.confirmNo(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/batchConfirmNo")
    @ApiOperation("批量确认编号回调")
    public ResponseInfo<Void> batchConfirmNo(@RequestBody @Validated BatchConfirmNextUseCodeDTO dto) {
        codeRuleService.batchConfirmNo(dto);
        return ResponseInfo.success();
    }

}
