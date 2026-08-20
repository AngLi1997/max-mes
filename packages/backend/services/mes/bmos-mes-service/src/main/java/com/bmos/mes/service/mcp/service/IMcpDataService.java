package com.bmos.mes.service.mcp.service;

import com.bmos.mes.service.mcp.dto.*;
import com.bmos.mes.service.mcp.vo.*;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 16:18
 */
public interface IMcpDataService {

    TextView<List<FormulaDataVO>> getFormulaData(FormulaDataQuery query);

    TextView<List<FormulaMaterialDataVO>> getFormulaMaterialData(FormulaMaterialDataQuery query);

    TextView<List<ProcessDataVO>> getProcessData(ProcessDataQuery query);

    TextView<List<MesStorageInventoryDataVO>> getMesStorageInventoryData(MesStorageInventoryDataQuery query);

    TextView<List<WmsStorageInventoryDataVO>> getWmsStorageInventoryData(WmsStorageInventoryDataQuery query);

    ChartView<List<PlasmaQuantityDataVO>> getPlasmaQuantity();

    ChartView<List<PlasmaQuantityChangeTrendVO>> getPlasmaQuantityChangeTrend();

    ChartView<List<PlasmaQualifiedPercentVO>> getPlasmaQualifiedPercent();
}
