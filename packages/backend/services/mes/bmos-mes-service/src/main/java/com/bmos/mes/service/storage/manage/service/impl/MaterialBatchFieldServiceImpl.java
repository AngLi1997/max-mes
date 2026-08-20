package com.bmos.mes.service.storage.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.common.model.component.CustomFieldDetailInfo;
import com.bmos.mes.service.product.mapper.MaterialFieldMapper;
import com.bmos.mes.service.storage.manage.convert.StorageMaterialConverter;
import com.bmos.mes.service.storage.manage.dto.MaterialBatchFieldDTO;
import com.bmos.mes.service.storage.manage.entity.MaterialBatchField;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.mapper.MaterialBatchFieldMapper;
import com.bmos.mes.service.storage.manage.service.MaterialBatchFieldService;
import com.bmos.mes.service.storage.manage.vo.MaterialBatchFieldVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MaterialBatchFieldServiceImpl implements MaterialBatchFieldService {

    @Autowired
    MaterialBatchFieldMapper materialBatchFieldMapper;

    @Autowired
    MaterialFieldMapper materialFieldMapper;

    @Autowired
    IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Override
    public void save(Long materialBatchId, List<MaterialBatchFieldDTO> materialBatchFieldVOList) {
        List<MaterialBatchField> materialBatchFields = StorageMaterialConverter.INSTANCE.convert2BatchField(materialBatchFieldVOList, materialBatchId);
        if (CollUtil.isEmpty(materialBatchFields)){
            return ;
        }
        materialBatchFieldMapper.insertBatch(materialBatchFields);
    }

    @Override
    public void delete(Long materialBatchId) {
        materialBatchFieldMapper.deleteByMaterialBatchId(materialBatchId);
    }

    @Override
    public List<MaterialBatchFieldVO> queryMaterialField(Long materialBatchId) {
        List<MaterialBatchField> materialBatchFields = materialBatchFieldMapper.selectMaterialBatchId(materialBatchId);
        return StorageMaterialConverter.INSTANCE.convert2BatchFieldVOList(materialBatchFields);
    }

    @Override
    public MaterialBatchFieldVO queryMaterialBatchField(Long materialBatchId, String fieldData) {
        MaterialBatchField field = materialBatchFieldMapper.selectMaterialBatchField(materialBatchId, fieldData);
        return StorageMaterialConverter.INSTANCE.convert2BatchFieldVO(field);
    }

    @Override
    public List<CustomFieldDetailInfo> queryMaterialAndBatchField(Collection<Long> batchIds) {
        if (CollUtil.isEmpty(batchIds)) {
            return new ArrayList<>();
        }
        List<MaterialBatchField> batchFields = materialBatchFieldMapper.selectByMaterialBatchIdList(batchIds);
        return StorageMaterialConverter.INSTANCE.convert2CustomFieldDetailInfo(batchFields);
    }
}
