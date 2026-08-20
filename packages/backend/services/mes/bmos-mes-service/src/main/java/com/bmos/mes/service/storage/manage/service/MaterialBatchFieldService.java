package com.bmos.mes.service.storage.manage.service;

import com.bmos.mes.common.model.component.CustomFieldDetailInfo;
import com.bmos.mes.service.storage.manage.dto.MaterialBatchFieldDTO;
import com.bmos.mes.service.storage.manage.vo.MaterialBatchFieldVO;

import java.util.Collection;
import java.util.List;

/**
 * 物料批次自定义字段
 */
public interface MaterialBatchFieldService {

    /**
     * 根据物料批次id绑定物料批次自定义字段
     * @param
     * @param materialBatchFieldVOList
     */
    void save(Long materialBatchId, List<MaterialBatchFieldDTO> materialBatchFieldVOList);

    /**
     * 删除物料批次绑定的物料批次自定义字段
     * @param materialBatchId
     */
    void delete(Long materialBatchId);

    /**
     * 根据物料批次id查询物料的自定义字段
     * @param materialBatchId
     * @return
     */
    List<MaterialBatchFieldVO> queryMaterialField(Long materialBatchId);

    /**
     * 根据批次id和字段code查询具体的物料自定义字段
     * @param materialBatchId 物料批次id
     * @param fieldData 自定义字段code
     * @return
     */
    MaterialBatchFieldVO queryMaterialBatchField(Long materialBatchId, String fieldData);

    /**
     * @param batchIds 物料批次id列表
     * @return
     */
    List<CustomFieldDetailInfo> queryMaterialAndBatchField(Collection<Long> batchIds);
}
