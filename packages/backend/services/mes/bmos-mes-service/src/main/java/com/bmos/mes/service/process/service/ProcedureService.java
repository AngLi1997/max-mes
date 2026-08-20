package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.dto.ProcedureValidateDTO;
import com.bmos.mes.service.process.dto.query.ProcedureHistoricQueryDTO;
import com.bmos.mes.service.process.model.Procedure;
import com.bmos.mes.service.query.dto.ProductScheduleProcedureConfigDTO;

import java.util.Collection;
import java.util.List;

public interface ProcedureService {
    List<Procedure> saveBatch(List<Procedure> procedures);

    Procedure getById(Long procedureId);

    List<Procedure> getHistoricList(ProcedureHistoricQueryDTO dto);

    /**
     * 保存生产进度的配置
     *
     * @param copyToList 配置列表
     */
    void saveProductScheduleProcedureConfig(List<ProductScheduleProcedureConfigDTO> copyToList);

    /**
     * 查询生产进度配置
     * @return 配置列表
     */
    List<ProductScheduleProcedureConfigDTO> getProductScheduleProcedureConfig();

    List<Procedure> selectByIds(Collection<Long> ids);

    /**
     * 保存或更新工序
     * @param procedures
     */
    void saveOrUpdateBatch(List<Procedure> procedures);

    /**
     * 校验历史工序名称是否已经存在
     * @param dto
     * @return
     */
    Boolean validateProcedureName(ProcedureValidateDTO dto);
}
