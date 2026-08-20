package com.bmos.mes.service.mcp.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.mcp.dto.*;
import com.bmos.mes.service.mcp.service.IMcpDataService;
import com.bmos.mes.service.mcp.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 14:03
 */
@RestController("/mcp/mes")
public class McpController {

    @Resource
    private IMcpDataService mcpService;

    @GetMapping("/getFormulaData")
    @ApiOperation(value = "查询配方数据")
    public ResponseInfo<TextView<List<FormulaDataVO>>> getFormulaData(FormulaDataQuery query) {
        return ResponseInfo.success(mcpService.getFormulaData(query));
    }

    @GetMapping("/getFormulaMaterialData")
    @ApiOperation(value = "查询配方物料数据")
    public ResponseInfo<TextView<List<FormulaMaterialDataVO>>> getFormulaMaterialData(FormulaMaterialDataQuery query) {
        return ResponseInfo.success(mcpService.getFormulaMaterialData(query));
    }

    @GetMapping("/getProcessData")
    @ApiOperation(value = "查询工艺数据")
    public ResponseInfo<TextView<List<ProcessDataVO>>> getProcessData(ProcessDataQuery query) {
        return ResponseInfo.success(mcpService.getProcessData(query));
    }

    @GetMapping("/getMesStorageInventoryData")
    @ApiOperation(value = "查询Mes暂存间库存数据")
    public ResponseInfo<TextView<List<MesStorageInventoryDataVO>>> getMesStorageInventoryData(MesStorageInventoryDataQuery query) {
        return ResponseInfo.success(mcpService.getMesStorageInventoryData(query));
    }

    @GetMapping("/getWmsStorageInventoryData")
    @ApiOperation(value = "查询WMS暂存间库存数据")
    public ResponseInfo<TextView<List<WmsStorageInventoryDataVO>>> getWmsStorageInventoryData(WmsStorageInventoryDataQuery query) {
        return ResponseInfo.success(mcpService.getWmsStorageInventoryData(query));
    }
}
