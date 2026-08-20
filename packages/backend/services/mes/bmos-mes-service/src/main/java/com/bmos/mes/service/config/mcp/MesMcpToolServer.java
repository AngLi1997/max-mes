package com.bmos.mes.service.config.mcp;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.service.mcp.dto.*;
import com.bmos.mes.service.mcp.service.IMcpDataService;
import org.noear.solon.ai.chat.annotation.ToolMapping;
import org.noear.solon.ai.chat.annotation.ToolParam;
import org.noear.solon.ai.mcp.server.annotation.McpServerEndpoint;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 21:50
 */
@McpServerEndpoint(sseEndpoint = "/api/app/mes/mcp/sse")
public class MesMcpToolServer {


    @ToolMapping(description = "查询配方(BOM)数据")
    public String getFormulaData(@ToolParam(name = "bomName", description = "BOM配方名称", required = false) String bomName,
                                              @ToolParam(name = "bomVersion", description = "BOM配方版本", required = false) String bomVersion,
                                              @ToolParam(name = "productName", description = "产品名称", required = false) String productName,
                                              @ToolParam(name = "productCode", description = "产品编码", required = false) String productCode) {
        FormulaDataQuery query = new FormulaDataQuery();
        query.setBomName(bomName);
        query.setBomVersion(bomVersion);
        query.setProductName(productName);
        query.setProductCode(productCode);
        try {
            return toJson(getMcpDataService().getFormulaData(query));
        }catch (Exception e){
            return toJson("接口请求失败");
        }
    }

    @ToolMapping(description = "查询配方物料数据")
    public String getFormulaMaterialData(@ToolParam(name = "bomName", description = "BOM配方名称") String bomName,
                                                              @ToolParam(name = "bomVersion", description = "BOM配方版本") String bomVersion) {
        FormulaMaterialDataQuery query = new FormulaMaterialDataQuery();
        query.setBomName(bomName);
        query.setBomVersion(bomVersion);
        try {
            return toJson(getMcpDataService().getFormulaMaterialData(query));
        }catch (Exception e){
            return toJson("接口请求失败");
        }
    }

    @ToolMapping(description = "查询工艺数据")
    public String getProcessData(@ToolParam(name = "processName", description = "BOM配方名称", required = false) String processName,
                                              @ToolParam(name = "processVersion", description = "BOM配方版本", required = false) String processVersion,
                                              @ToolParam(name = "productName", description = "产品名称", required = false) String productName,
                                              @ToolParam(name = "productCode", description = "产品编码", required = false) String productCode,
                                              @ToolParam(name = "bomName", description = "BOM配方名称", required = false) String bomName,
                                              @ToolParam(name = "bomVersion", description = "BOM配方版本", required = false) String bomVersion) {
        ProcessDataQuery query = new ProcessDataQuery();
        query.setProcessName(processName);
        query.setProcessVersion(processVersion);
        query.setProductName(productName);
        query.setProductCode(productCode);
        query.setBomName(bomName);
        query.setBomVersion(bomVersion);
        try {
            return toJson(getMcpDataService().getProcessData(query));
        }catch (Exception e){
            return toJson("接口请求失败");
        }
    }

    @ToolMapping(description = "查询Mes暂存间库存数据")
    public String getMesStorageInventoryData(@ToolParam(name = "materialName", description = "物料名称", required = false) String materialName,
                                                                      @ToolParam(name = "materialCode", description = "物料编码", required = false) String materialCode,
                                                                      @ToolParam(name = "materialBatchNo", description = "物料批号", required = false) String materialBatchNo,
                                                                      @ToolParam(name = "includeExpired", description = "是否查询包含已过期的数据", required = false) Boolean includeExpired) {
        MesStorageInventoryDataQuery query = new MesStorageInventoryDataQuery();
        query.setMaterialName(materialName);
        query.setMaterialCode(materialCode);
        query.setMaterialBatchNo(materialBatchNo);
        query.setIncludeExpired(includeExpired);
        try {
            return toJson(getMcpDataService().getMesStorageInventoryData(query));
        }catch (Exception e){
            return toJson("接口请求失败");
        }
    }

    @ToolMapping(description = "查询WMS暂存间库存数据")
    public String getWmsStorageInventoryData(@ToolParam(name = "materialName", description = "物料名称", required = false) String materialName,
                                                                      @ToolParam(name = "materialCode", description = "物料编码", required = false) String materialCode,
                                                                      @ToolParam(name = "materialBatchNo", description = "物料批号", required = false) String materialBatchNo,
                                                                      @ToolParam(name = "includeExpired", description = "是否查询包含已过期的数据", required = false) Boolean includeExpired) {
        WmsStorageInventoryDataQuery query = new WmsStorageInventoryDataQuery();
        query.setMaterialName(materialName);
        query.setMaterialCode(materialCode);
        query.setMaterialBatchNo(materialBatchNo);
        query.setIncludeExpired(includeExpired);
        try {
            return toJson(getMcpDataService().getWmsStorageInventoryData(query));
        }catch (Exception e){
            return toJson("接口请求失败");
        }
    }

    private IMcpDataService getMcpDataService() {
        return SpringUtil.getBean(IMcpDataService.class);
    }

    private String toJson(Object obj) {
        return JsonUtils.toJsonString(obj);
    }
}
