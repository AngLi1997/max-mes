package com.bmos.mes.service.mcp.service.impl;

import com.bmos.mes.service.mcp.convert.WmsStorageInventoryFeignConverter;
import com.bmos.mes.service.mcp.dto.*;
import com.bmos.mes.service.mcp.mapper.McpDataMapper;
import com.bmos.mes.service.mcp.service.IMcpDataService;
import com.bmos.mes.service.mcp.vo.*;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.requisition.feign.WmsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 16:18
 */
@Service
@Slf4j
public class McpDataServiceImpl implements IMcpDataService {

    @Resource
    private McpDataMapper mcpDataMapper;

    @Resource
    private WmsFeignClient wmsFeignClient;

    @Override
    public TextView<List<FormulaDataVO>> getFormulaData(FormulaDataQuery query) {
        return new TextView<>(mcpDataMapper.getFormulaData(query));
    }

    @Override
    public TextView<List<FormulaMaterialDataVO>> getFormulaMaterialData(FormulaMaterialDataQuery query) {
        return new TextView<>(mcpDataMapper.getFormulaMaterialData(query));
    }

    @Override
    public TextView<List<ProcessDataVO>> getProcessData(ProcessDataQuery query) {
        return new TextView<>(mcpDataMapper.getProcessData(query));
    }

    @Override
    public TextView<List<MesStorageInventoryDataVO>> getMesStorageInventoryData(MesStorageInventoryDataQuery query) {
        return new TextView<>(mcpDataMapper.getMesStorageInventoryData(query));
    }

    @Override
    public TextView<List<WmsStorageInventoryDataVO>> getWmsStorageInventoryData(WmsStorageInventoryDataQuery query) {
        List<WmsStorageInventoryFeignVO> data = FeignUtils.handleRequest(q -> wmsFeignClient.queryInventoryData(q), query).getData();
        return new TextView<>(WmsStorageInventoryFeignConverter.INSTANCE.convertToData(data));
    }

    @Override
    public ChartView<List<PlasmaQuantityDataVO>> getPlasmaQuantity() {
        // mock
        List<PlasmaQuantityDataVO> list = new ArrayList<>();
        list.add(new PlasmaQuantityDataVO("2025", Arrays.asList("4.8", "4.9", "5.1", "5.7", "5.8", "5.5", "0", "0", "0", "0", "0", "0"), "吨"));
        list.add(new PlasmaQuantityDataVO("2024", Arrays.asList("5", "5.2", "5.3", "5.5", "5.6", "5.7", "5.8", "6.0", "5.5", "5.6", "5.8", "5.5"), "吨"));
        return new ChartView<>("bar", "采浆量数据", list);
    }

    @Override
    public ChartView<List<PlasmaQuantityChangeTrendVO>> getPlasmaQuantityChangeTrend() {
        // mock
        List<PlasmaQuantityChangeTrendVO> list = new ArrayList<>();
        list.add(new PlasmaQuantityChangeTrendVO("2025-01", "32", "吨"));
        list.add(new PlasmaQuantityChangeTrendVO("2025-02", "35", "吨"));
        list.add(new PlasmaQuantityChangeTrendVO("2025-03", "40", "吨"));
        list.add(new PlasmaQuantityChangeTrendVO("2025-04", "43", "吨"));
        list.add(new PlasmaQuantityChangeTrendVO("2025-05", "20", "吨"));
        return new ChartView<>("line", "生产的投浆量变换趋势", list);
    }

    @Override
    public ChartView<List<PlasmaQualifiedPercentVO>> getPlasmaQualifiedPercent() {
        // mock
        List<PlasmaQualifiedPercentVO> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("检疫期合格", "200");
        map.put("待检", "50");
        map.put("超期", "3");
        map.put("不合格", "1");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            PlasmaQualifiedPercentVO percentVO = new PlasmaQualifiedPercentVO();
            percentVO.setLabel(entry.getKey());
            percentVO.setQuantity(entry.getValue());
            percentVO.setUnit("t");
            list.add(percentVO);
        }
        return new ChartView<>("pie", "检疫期合格率", list);
    }
}
