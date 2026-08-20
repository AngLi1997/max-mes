package com.bmos.mes.service.trace.material.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplateCreateDTO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplateEditDTO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplatePageQuery;
import com.bmos.mes.service.trace.material.service.IMaterialTraceTemplateService;
import com.bmos.mes.service.trace.material.vo.MaterialTraceTemplateDetailVO;
import com.bmos.mes.service.trace.material.vo.MaterialTraceTemplatePageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 物料追溯模板配置接口
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 15:09
 */
@RestController
@RequestMapping("/material/trace/template")
@Api(tags = "物料追溯模板配置接口")
public class MaterialTraceTemplateController {

    @Resource
    private IMaterialTraceTemplateService materialTraceTemplateService;

    @ApiOperation(value = "分页查询物料追溯模板信息")
    @GetMapping("/queryPage")
    public ResponseInfo<CommonPage<MaterialTraceTemplatePageVO>> queryPage(@Validated MaterialTraceTemplatePageQuery pageQuery) {
        return ResponseInfo.success(materialTraceTemplateService.queryPage(pageQuery));
    }

    @ApiOperation(value = "查询物料追溯模板信息")
    @GetMapping("/queryDetail")
    @ApiImplicitParam(name = "id", value = "模板id", required = true, dataType = "Long")
    public ResponseInfo<MaterialTraceTemplateDetailVO> queryDetail(@Validated @RequestParam Long id) {
        return ResponseInfo.success(materialTraceTemplateService.queryDetail(id));
    }

    @ApiOperation(value = "新增模板")
    @PostMapping("/create")
    @OperationLog
    public ResponseInfo<Void> createTemplate(@Validated @RequestBody MaterialTraceTemplateCreateDTO dto) {
        materialTraceTemplateService.createTemplate(dto);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "编辑模板")
    @PostMapping("/edit")
    @OperationLog
    public ResponseInfo<Void> editTemplate(@Validated @RequestBody MaterialTraceTemplateEditDTO dto) {
        materialTraceTemplateService.editTemplate(dto);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "启用模板")
    @PutMapping("/enable")
    @OperationLog
    @ApiImplicitParam(name = "id", value = "模板id", required = true, dataType = "Long")
    public ResponseInfo<Void> enableTemplate(@Validated @RequestParam Long id) {
        materialTraceTemplateService.enableTemplate(id);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "停用模板")
    @PutMapping("/disable")
    @OperationLog
    @ApiImplicitParam(name = "id", value = "模板id", required = true, dataType = "Long")
    public ResponseInfo<Void> disableTemplate(@Validated @RequestParam Long id) {
        materialTraceTemplateService.disableTemplate(id);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "删除模板")
    @DeleteMapping("/delete")
    @OperationLog
    @ApiImplicitParam(name = "id", value = "模板id", required = true, dataType = "Long")
    public ResponseInfo<Void> deleteTemplate(@Validated @RequestParam Long id) {
        materialTraceTemplateService.deleteTemplate(id);
        return ResponseInfo.success();
    }
}
