package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.model.ProcedureModelMaterial;
import com.bmos.mes.service.process.vo.ProcedureModelMaterialVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProcedureModelMaterialService {


    void saveBatch(List<ProcedureModelMaterial> materials);

    Map<Long, List<Long>> getByProcedureModelIds(Set<Long> longs);

    void deleteByProcedureModelIds(List<Long> ids);

    List<Long> getByProcedureModelId(List<Long> procedureModelId);

    List<ProcedureModelMaterialVO> getMaterialListByProcedureModelIds(List<Long> procedureModelIdList);
}
