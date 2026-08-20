package com.bmos.lims2.server.eln.record.service;


import com.bmos.lims2.server.eln.record.dto.ComponentDetailDTO;
import com.bmos.lims2.server.eln.record.dto.CopyVersionDTO;
import com.bmos.lims2.server.eln.record.dto.SaveFormulaDTO;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.util.Graph;
import com.bmos.lims2.server.eln.record.vo.ComponentListVO;
import com.bmos.lims2.server.eln.record.vo.ParseComponentVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface BatchRecordComponentService {


    List<BatchRecordComponent> selectByVersionId(Long versionId);

    List<BatchRecordComponent> selectComponentList(Long itemId, Long newItemId);

    List<BatchRecordComponent> getByIdList(List<Long> idList);

    void saveOrUpdateComponent(List<BatchRecordComponent> componentList);

    void deleteCompoenent(Long itemId,Long recordVersionId);

    ParseComponentVO listComponent(Long itemId, Long recordVersionId);

    Boolean saveFormula(SaveFormulaDTO dto);

    Graph<Long> getGraph(Long recordVersionId);

    Boolean deleteFormula(Long componentId);

    /**
     * @param recordVersionId 记录版本id
     * @param fields 组件id列表
     * @param isResult 是否是计算结果
     * @return
     */
    List<BatchRecordComponent> selectByRecordVersionIdAndFields(Long recordVersionId, Set<Long> fields, Boolean isResult);


    void deleteByIdList(List<Long> idInDb);

    List<BatchRecordComponent> selectByVersionAndItem(Long recordVersionId, Long recordItemId);
    ComponentListVO selectUsedComponentDetail(Long recordVersionId, Long recordItemId, Long componentId);

    BatchRecordComponent getById(Long componentId);

    List<ComponentListVO> selectAutoFillComponentTree(Long recordVersionId, Long recordItemId);

    List<BatchRecordComponent> selectByRecordVersionIdsAndFields(Collection<Long> longs, Collection<Long> fieldIdList);

    /**
     * 查询当前组件id的父组件id
     * @param componentId
     * @return
     */
    ComponentListVO selectParentComponent(Long componentId);

    /**
     * 刷新图缓存
     * @param recordVersionId
     */
    void refreshGraph(Long recordVersionId);

    Boolean copyComponent(CopyVersionDTO dto, List<Long> recordItemIdList, Long versionId);

    /**
     * 查询绑定了该fieldId的记录项id
     * @param fieldId
     * @return
     */
    Long getByFieldId(Long fieldId);

    List<BatchRecordComponent> getByFieldIdList(List<Long> fieldIdList);
}
