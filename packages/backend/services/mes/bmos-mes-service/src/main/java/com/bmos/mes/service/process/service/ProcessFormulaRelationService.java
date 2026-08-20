package com.bmos.mes.service.process.service;

public interface ProcessFormulaRelationService {


    /**
     * 工艺升版、复制版本、编辑修改了配方时对配方物料的处理
     * @param processVersionId 工艺版本Id
     * @param oldFormulaVersionId 旧的配方版本id
     */
    void replaceFormulaMaterial(Long processVersionId, Long oldFormulaVersionId);

    /**
     * 校验配置物料是否匹配配置的版本
     * @param id
     */
    void validateFormulaMaterialMatch(Long id);
}
