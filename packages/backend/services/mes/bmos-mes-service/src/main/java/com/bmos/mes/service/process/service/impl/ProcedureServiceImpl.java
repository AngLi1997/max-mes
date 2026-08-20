package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.dto.ProcedureValidateDTO;
import com.bmos.mes.service.process.dto.query.ProcedureHistoricQueryDTO;
import com.bmos.mes.service.process.mapper.ProcedureMapper;
import com.bmos.mes.service.process.mapper.ProductScheduleProcedureConfigMapper;
import com.bmos.mes.service.process.model.Procedure;
import com.bmos.mes.service.process.model.ProductScheduleProcedureConfig;
import com.bmos.mes.service.process.service.ProcedureService;
import com.bmos.mes.service.query.dto.ProductScheduleProcedureConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcedureServiceImpl implements ProcedureService {

    @Autowired
    private ProcedureMapper procedureMapper;

    @Autowired
    private ProductScheduleProcedureConfigMapper productScheduleProcedureConfigMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Procedure> saveBatch(List<Procedure> procedures) {
        if (CollUtil.isEmpty(procedures)) {
            return Collections.emptyList();
        }
        try {
            procedureMapper.insertBatch(procedures);
        } catch (DuplicateKeyException e) {
            throw new BmosException(MesResponseCode.PROCEDURE_EXIST);
        }
        return procedures;
    }

    @Override
    public Procedure getById(Long procedureId) {
        return procedureMapper.selectById(procedureId);
    }

    @Override
    public List<Procedure> getHistoricList(ProcedureHistoricQueryDTO dto) {
        return procedureMapper.selectHistoricList(dto);
    }

    /**
     * 保存生产进度的配置
     *
     * @param copyToList 配置列表
     */
    @Override
    public void saveProductScheduleProcedureConfig(List<ProductScheduleProcedureConfigDTO> copyToList) {
        if (CollUtil.isEmpty(copyToList)) {
            return;
        }
        List<ProductScheduleProcedureConfig> exits = productScheduleProcedureConfigMapper.selectList();
        // 每次保存都全量删除
        if (CollectionUtil.isNotEmpty(exits)) {
            productScheduleProcedureConfigMapper.deleteBatchIds(exits.stream().map(ProductScheduleProcedureConfig::getId).collect(Collectors.toList()));
        }
        List<ProductScheduleProcedureConfig> productScheduleProcedureConfigs = BeanUtil.copyToList(copyToList, ProductScheduleProcedureConfig.class);
        productScheduleProcedureConfigs.forEach(productScheduleProcedureConfig -> productScheduleProcedureConfig.setId(IdUtils.getSnowflake()));
        productScheduleProcedureConfigMapper.insertBatch(productScheduleProcedureConfigs);
    }

    @Override
    public List<ProductScheduleProcedureConfigDTO> getProductScheduleProcedureConfig() {
        return productScheduleProcedureConfigMapper.selectListWithProcesName();
    }

    @Override
    public List<Procedure> selectByIds(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)){
            return new ArrayList<>();
        }
        return procedureMapper.selectBatchIds(ids);
    }

    @Override
    public void saveOrUpdateBatch(List<Procedure> procedures) {
        if (CollUtil.isEmpty(procedures)) {
            return;
        }
        procedureMapper.saveOrUpdateBatch(procedures);
    }

    @Override
    public Boolean validateProcedureName(ProcedureValidateDTO dto) {
        Procedure procedure = procedureMapper.selectByProcessIdAndName(dto.getProcessId(), dto.getName());
        if (procedure != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
