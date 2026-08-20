package com.bmos.mes.service.inspect.controller;

import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigPageVO;
import com.bmos.mes.service.inspect.service.InspectConfigService;
import com.bmos.mes.service.inspect.service.dto.InspectConfigBindMaterialDTO;
import com.bmos.mes.service.inspect.service.dto.InspectConfigPageDTO;
import com.bmos.mes.service.inspect.service.dto.InspectConfigSaveDTO;
import com.bmos.mes.service.inspect.service.dto.InspectConfigUpdateDTO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inspect/config")
@Api(tags = "请验单配置相关接口")
public class InspectConfigController {

    @Autowired
    private InspectConfigService inspectConfigService;

    @PostMapping("/save")
    @ApiOperation("请验单配置保存")
    @OperationLog
    @DistributedLock(expression = "#dto.name")
    public ResponseInfo<Void> save(@RequestBody @Validated InspectConfigSaveDTO dto) {
        inspectConfigService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("请验单配置修改")
    @OperationLog
    @DistributedLock(expression = "#dto.name")
    public ResponseInfo<Void> update(@RequestBody @Validated InspectConfigUpdateDTO dto) {
        inspectConfigService.update(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除请验单")
    @ApiParam(name = "id", value = "分类id", required = true)
    @OperationLog
    @DistributedLock(expression = "#id")
    public ResponseInfo<Void> delete(@PathVariable("id") Long id) {
        inspectConfigService.delete(id);
        return ResponseInfo.success();
    }

    @PutMapping("/enable/{id}")
    @ApiOperation("启用请验单")
    @ApiParam(name = "id", value = "分类id", required = true)
    @OperationLog
    public ResponseInfo<Void> enable(@PathVariable("id") Long id) {
        inspectConfigService.enable(id);
        return ResponseInfo.success();
    }

    @PutMapping("/disable/{id}")
    @ApiOperation("停用请验单")
    @ApiParam(name = "id", value = "分类id", required = true)
    @OperationLog
    public ResponseInfo<Void> disable(@PathVariable("id") Long id) {
        inspectConfigService.disable(id);
        return ResponseInfo.success();
    }

    @GetMapping("/queryDetail/{id}")
    @ApiOperation("获取请验单详情")
    @ApiParam(name = "id", value = "请验单id", required = true)
    public ResponseInfo<InspectConfigDetailVO> queryDetail(@PathVariable("id") Long id) {
        return ResponseInfo.success(inspectConfigService.queryDetail(id));
    }

    @GetMapping("/queryList")
    @ApiOperation("获取请验单列表")
    public ResponseInfo<CommonPage<InspectConfigPageVO>> queryList(@Validated InspectConfigPageDTO dto) {
        return ResponseInfo.success(inspectConfigService.queryList(dto));
    }

    @PostMapping("/bind/material")
    @ApiOperation("请验单绑定物料")
    @OperationLog
    @DistributedLock(expression = "#dto.id")
    public ResponseInfo<Void> bindMaterial(@RequestBody @Validated InspectConfigBindMaterialDTO dto) {
        inspectConfigService.bindMaterial(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/query/material/{id}")
    @ApiOperation("获取请验单已经绑定的物料")
    @ApiParam(name = "id", value = "请验单id", required = true)
    public ResponseInfo<List<Long>> queryMaterial(@PathVariable("id") Long id) {
        return ResponseInfo.success(inspectConfigService.queryBindMaterial(id));
    }

    @GetMapping("/query/{formulaMaterialId}")
    @ApiOperation("根据配方物料id查询请验单配置数据以及请验单信息（一个物料可能对应多个请验单）")
    public ResponseInfo<List<InspectConfigDetailVO>> queryConfigByFormulaMaterialId(@PathVariable("formulaMaterialId") Long formulaMaterialId) {
        return ResponseInfo.success(inspectConfigService.queryConfigByFormulaMaterialId(formulaMaterialId));
    }

}
