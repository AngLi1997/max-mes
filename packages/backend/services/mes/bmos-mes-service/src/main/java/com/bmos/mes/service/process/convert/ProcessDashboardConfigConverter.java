package com.bmos.mes.service.process.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcessDashboardConfig;
import com.bmos.mes.service.process.model.ProcessDashboardConfigData;
import com.bmos.mes.service.process.vo.ProcessDashboardProcedureVO;
import com.bmos.mes.service.process.vo.ProcessDashboardVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/31 17:09
 */
@Mapper
public interface ProcessDashboardConfigConverter {

    ProcessDashboardConfigConverter INSTANCE = Mappers.getMapper(ProcessDashboardConfigConverter.class);

    ProcessDashboardVO convertToVO(ProcessDashboardConfig config);


    @Mapping(target = "procedureName", source = "name")
    ProcessDashboardProcedureVO convertToProcedureModel(ProcedureModel procedureModel);

    List<ProcessDashboardProcedureVO> convertTOProcedureModelList(List<ProcedureModel> procedureModels);

    default List<ProcessDashboardConfigData> covert2ConfigDataList(List<ProcessDashboardProcedureVO> procedureList, Long dashboardConfigId){
        if (CollUtil.isEmpty(procedureList)){
            return new ArrayList<>();
        }
        List<ProcessDashboardConfigData> result = new ArrayList<>();
        for (ProcessDashboardProcedureVO processDashboardProcedureVO : procedureList) {
            ProcessDashboardConfigData processDashboardConfigData = new ProcessDashboardConfigData();
            processDashboardConfigData.setDashboardConfigId(dashboardConfigId);
            processDashboardConfigData.setProcedureId(processDashboardProcedureVO.getProcedureId());
            processDashboardConfigData.setProcedureName(processDashboardProcedureVO.getProcedureName());
            processDashboardConfigData.setCustomName(processDashboardProcedureVO.getCustomName());
            processDashboardConfigData.setEffect(processDashboardProcedureVO.getEffect());
            processDashboardConfigData.setModelCode(processDashboardProcedureVO.getModelCode());
            processDashboardConfigData.setSort(processDashboardProcedureVO.getSort());
            result.add(processDashboardConfigData);
        }
        return result;
    }
}
