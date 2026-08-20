package com.bmos.lims2.web.operate.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.operate.dto.version.*;
import com.bmos.lims2.server.operate.model.OperateRule;
import com.bmos.lims2.server.operate.service.OperateRuleVersionService;
import com.bmos.lims2.server.operate.vo.OperateRuleAuditVO;
import com.bmos.lims2.server.operate.vo.OperateRuleVersionDetailsVO;
import com.bmos.lims2.server.operate.vo.OperateRuleVersionPageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author renjinguang
 */
@RestController
@RequestMapping("/operate/rule/version")
@Api(tags = "操作规程文件版本管理相关接口")
@Validated
public class OperateRuleVersionController {

    @Autowired
    private OperateRuleVersionService versionService;

    @GetMapping("/page/list")
    @ApiOperation(value = "查询操作规程版本管理列表")
    public ResponseInfo<CommonPage<OperateRuleVersionPageVO>> getRecordPage(@Validated VersionPageDTO dto) {
        return ResponseInfo.success(versionService.getRecordPage(dto));
    }

    @GetMapping("/details")
    @ApiOperation(value = "根据主键id查询版本详情")
    @ApiParam(name = "id", value = "主键id", required = true)
    public ResponseInfo<OperateRuleVersionDetailsVO> getDetailsById(@NotNull @Valid Long id) {
        return ResponseInfo.success(versionService.getDetailsById(id));
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增版本")
    public ResponseInfo<Void> save(@RequestBody @Validated SaveVersionDTO dto) {
        versionService.saveVersion(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "文件编辑")
    public ResponseInfo<Void> update(@RequestBody @Validated UpdateVersionDTO dto) {
        versionService.update(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/update/state")
    @ApiOperation(value = "编辑状态")
    public ResponseInfo<Void> update(@RequestBody @Validated UpdateStateDTO dto) {
        versionService.updateState(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update/valid/{versionId}")
    @ApiOperation(value = "直接生效")
    @ApiImplicitParam(name = "versionId",value = "版本id",required = true)
    public ResponseInfo<Void> updateValid(@Validated @PathVariable Long versionId){
        versionService.updateValid(versionId);
        return ResponseInfo.success();
    }

    @PostMapping("/start/flow")
    @ApiOperation(value = "发起流程审核：启用or停用")
    public ResponseInfo<Void> startFlow(@RequestBody @Validated VersionStartFlowDTO dto) {
        versionService.startFlow(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/page/todo/flow")
    @ApiOperation(value = "查询代办流程分页")
    public ResponseInfo<CommonPage<OperateRuleAuditVO>> pageTodoFlow(@Validated OperateVersionAuditDTO dto) {
        return ResponseInfo.success(versionService.pageTodoFlow(dto));
    }

    @GetMapping("/download")
    @ApiOperation("文件sop下载")
    @ApiParam(name = "versionId", value = "操作规程id", required = true)
    public void download(@NotNull @Valid Long versionId, HttpServletResponse response) throws Exception{
        versionService.downloadSop(versionId, response);
    }

    @GetMapping("/app/detail")
    @ApiOperation("app端查询文件信息")
    @ApiParam(name = "recordId",value = "",required = true)
    public ResponseInfo<List<OperateRule>> getAppDetail(@NotNull @Valid Long recordId){
        return ResponseInfo.success(versionService.getAppDetail(recordId));
    }
}
