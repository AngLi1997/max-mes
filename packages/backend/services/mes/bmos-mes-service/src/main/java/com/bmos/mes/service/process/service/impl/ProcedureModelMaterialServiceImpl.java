package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.process.mapper.ProcedureModelMaterialMapper;
import com.bmos.mes.service.process.model.ProcedureModelMaterial;
import com.bmos.mes.service.process.service.ProcedureModelMaterialService;
import com.bmos.mes.service.process.vo.ProcedureModelMaterialVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProcedureModelMaterialServiceImpl implements ProcedureModelMaterialService {

    @Autowired
    private ProcedureModelMaterialMapper procedureModelMaterialMapper;

    @Override
    public void saveBatch(List<ProcedureModelMaterial> materials) {
        if (CollUtil.isEmpty(materials)){
            return;
        }
        procedureModelMaterialMapper.insertBatch(materials);
    }

    @Override
    public Map<Long, List<Long>> getByProcedureModelIds(Set<Long> procedureModelIds) {
        if (CollUtil.isEmpty(procedureModelIds)){
            return Collections.emptyMap();
        }
        List<ProcedureModelMaterial> list = procedureModelMaterialMapper.selectByProcedureModelIds(procedureModelIds);
        return CollectionUtils.convertMultiMap(list, ProcedureModelMaterial::getProcedureModelId, ProcedureModelMaterial::getProductFormulaMaterialId);
    }

    @Override
    public void deleteByProcedureModelIds(List<Long> ids) {
        procedureModelMaterialMapper.deleteByProcedureModelIds(ids);
    }

    @Override
    public List<Long> getByProcedureModelId(List<Long> procedureModelId) {
        List<ProcedureModelMaterial> procedureModelMaterials = procedureModelMaterialMapper.selectByProcedureModelIds(procedureModelId);
        if (CollUtil.isEmpty(procedureModelMaterials)){
            return new ArrayList<>();
        }
        return CollectionUtils.convertList(procedureModelMaterials,ProcedureModelMaterial::getProductFormulaMaterialId);
    }

    @Override
    public List<ProcedureModelMaterialVO> getMaterialListByProcedureModelIds(List<Long> procedureModelIdList) {
        if (CollUtil.isEmpty(procedureModelIdList)){
            return new ArrayList<>();
        }
        return procedureModelMaterialMapper.getMaterialListByProcedureModelIds(procedureModelIdList);
    }
}
