package com.bmos.mes.service.mcp.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.mcp.service.IMcpDataService;
import com.bmos.mes.service.mcp.vo.ChartView;
import com.bmos.mes.service.mcp.vo.PlasmaQualifiedPercentVO;
import com.bmos.mes.service.mcp.vo.PlasmaQuantityChangeTrendVO;
import com.bmos.mes.service.mcp.vo.PlasmaQuantityDataVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 演示图表数据查询接口
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 14:03
 */
@RestController("/mcp/mes/charts/demo")
public class McpChatsDemoController {

    @Resource
    private IMcpDataService mcpDataService;

    @GetMapping("/getPlasmaQuantityOfYear")
    @ApiOperation(value = "查询采浆量")
    public ResponseInfo<ChartView<List<PlasmaQuantityDataVO>>> getPlasmaQuantityOfYear() {
        return ResponseInfo.success(mcpDataService.getPlasmaQuantity());
    }

    @GetMapping("/getPlasmaQuantityChangeTrend")
    @ApiOperation(value = "查询生产的投浆量变换趋势")
    public ResponseInfo<ChartView<List<PlasmaQuantityChangeTrendVO>>> getPlasmaQuantityChangeTrend() {
        return ResponseInfo.success(mcpDataService.getPlasmaQuantityChangeTrend());
    }

    @GetMapping("/getPlasmaQualifiedPercent")
    @ApiOperation(value = "查询在库血浆的检疫期合格和不合格的比例")
    public ResponseInfo<ChartView<List<PlasmaQualifiedPercentVO>>> getPlasmaQualifiedPercent() {
        return ResponseInfo.success(mcpDataService.getPlasmaQualifiedPercent());
    }
}
