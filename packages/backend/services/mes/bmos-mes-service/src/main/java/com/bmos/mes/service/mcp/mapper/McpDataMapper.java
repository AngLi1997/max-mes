package com.bmos.mes.service.mcp.mapper;

import com.bmos.mes.service.mcp.dto.FormulaDataQuery;
import com.bmos.mes.service.mcp.dto.FormulaMaterialDataQuery;
import com.bmos.mes.service.mcp.dto.MesStorageInventoryDataQuery;
import com.bmos.mes.service.mcp.dto.ProcessDataQuery;
import com.bmos.mes.service.mcp.vo.FormulaDataVO;
import com.bmos.mes.service.mcp.vo.FormulaMaterialDataVO;
import com.bmos.mes.service.mcp.vo.MesStorageInventoryDataVO;
import com.bmos.mes.service.mcp.vo.ProcessDataVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 17:26
 */
@Mapper
public interface McpDataMapper {

    List<FormulaDataVO> getFormulaData(FormulaDataQuery query);

    List<ProcessDataVO> getProcessData(ProcessDataQuery query);

    List<MesStorageInventoryDataVO> getMesStorageInventoryData(MesStorageInventoryDataQuery query);

    List<FormulaMaterialDataVO> getFormulaMaterialData(FormulaMaterialDataQuery query);
}
