package com.bmos.platform.service.system.code.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.system.code.dto.CodeRulePageDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleSaveDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleUpdateDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionPageDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionSaveDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionUpdateDTO;
import com.bmos.platform.service.system.code.service.CodeRuleVersionService;
import com.bmos.platform.service.system.code.vo.CodeRulePageVO;
import com.bmos.platform.service.system.code.vo.CodeRuleVersionPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codeRuleVersion")
@Api(tags = "编码规则版本")
public class CodeRuleVersionController {
    @Autowired
    private CodeRuleVersionService codeRuleVersionService;

    @GetMapping("/page")
    @ApiOperation("公式列表")
    public ResponseInfo<CommonPage<CodeRuleVersionPageVO>> page(CodeRuleVersionPageDTO dto) {
        return ResponseInfo.success(
            CommonPage.convertPage(codeRuleVersionService.page(dto))
        );
    }

    @PutMapping("/confirm/{id}")
    @ApiParam(value = "id", name = "版本id", required = true)
    @ApiOperation("确认")
    @OperationLog
    public ResponseInfo<Void> confirm(@PathVariable Long id) {
        codeRuleVersionService.confirm(id);
        return ResponseInfo.success();
    }

    @PutMapping("/enabled/{id}")
    @ApiParam(value = "id", name = "版本id", required = true)
    @ApiOperation("启用")
    @OperationLog
    public ResponseInfo<Void> enabled(@PathVariable Long id) {
        codeRuleVersionService.enabled(id);
        return ResponseInfo.success();
    }

    @PutMapping("/disabled/{id}")
    @ApiParam(value = "id", name = "版本id", required = true)
    @ApiOperation("停用")
    @OperationLog
    public ResponseInfo<Void> disabled(@PathVariable Long id) {
        codeRuleVersionService.disabled(id);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiParam(value = "id", name = "版本id", required = true)
    @ApiOperation("删除")
    @OperationLog
    public ResponseInfo<Void> delete(@PathVariable Long id) {
        codeRuleVersionService.delete(id);
        return ResponseInfo.success();
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> save(@RequestBody @Validated CodeRuleVersionSaveDTO dto) {
        dto.isValidated();
        codeRuleVersionService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("更新")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> update(@RequestBody @Validated CodeRuleVersionUpdateDTO dto) {
        dto.isValidated();
        codeRuleVersionService.update(dto);
        return ResponseInfo.success();
    }
}
