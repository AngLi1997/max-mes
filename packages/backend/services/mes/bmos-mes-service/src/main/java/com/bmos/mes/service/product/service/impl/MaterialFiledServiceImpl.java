package com.bmos.mes.service.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.material.vo.MaterialFieldInfoFeignVO;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.product.convert.ProductMaterialConverter;
import com.bmos.mes.service.product.dto.MaterialFieldQueryDTO;
import com.bmos.mes.service.product.dto.MaterialFieldSaveDTO;
import com.bmos.mes.service.product.mapper.MaterialFieldMapper;
import com.bmos.mes.service.product.model.MaterialField;
import com.bmos.mes.service.product.service.MaterialFieldService;
import com.bmos.mes.service.product.vo.MaterialFieldInfoVO;
import com.bmos.mes.service.product.vo.MaterialFieldTypeVO;
import com.bmos.platform.facade.dict.enums.DictCodeConstants;
import com.bmos.platform.facade.dict.feign.DictFeign;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialFiledServiceImpl implements MaterialFieldService {

    /**
     * 物料自定义字段暂时先在此处进行描写
     * 若后期在平台维护专属物料自定义字段类型，在从平台测进行获取
     */
    public static final List<String> MATERIAL_FIELD_TYPE_LIST = Lists.newArrayList(DictCodeConstants.MATERIAL_BATCH_CUSTOM_FIELDS,
            DictCodeConstants.MATERIAL_CUSTOM_FIELDS,
            DictCodeConstants.MATERIAL_PIECE_CUSTOM_FIELDS);

    @Autowired
    private MaterialFieldMapper materialFieldMapper;

    @Autowired
    private DictFeign dictFeign;

    @Override
    public void saveMaterialFields(Long materialId, List<MaterialFieldSaveDTO> fieldSaveDTOList) {
        List<MaterialField> materialFields = ProductMaterialConverter.INSTANCE.convert2MaterialField(fieldSaveDTOList, materialId);
        if (CollUtil.isEmpty(materialFields)){
            return ;
        }
        materialFieldMapper.insertBatch(materialFields);
    }

    @Override
    public void deleteByMaterialId(Long materialId) {
        materialFieldMapper.deleteByMaterialId(materialId);
    }

    @Override
    public List<MaterialFieldTypeVO> getMaterialFieldList() {
        // 生产物料自定义字段需要获取的字典类型
        ResponseInfo<List<DictDetailFeignVO>> listResponseInfo = FeignUtils.handleRequest(data -> dictFeign.selectDictByCategory(data), MATERIAL_FIELD_TYPE_LIST);
        if (!listResponseInfo.isSuccess()){
            return new ArrayList<>();
        }
        return ProductMaterialConverter.INSTANCE.convert2MaterialFieldTypeVO(listResponseInfo.getData());
    }

    @Override
    public List<MaterialFieldInfoVO> getMaterialFieldInfo(Long id) {
        // 根据物料id获取物料自定义字段
        List<MaterialField> fieldList = materialFieldMapper.selectByMaterialId(id);
        if (CollUtil.isEmpty(fieldList)){
            return new ArrayList<>();
        }
        return ProductMaterialConverter.INSTANCE.convertMaterialFieldInfoVOList(fieldList);
    }

    @Override
    public List<MaterialFieldInfoVO> getMaterialFieldInfo(Collection<Long> materialIds) {
        return ProductMaterialConverter.INSTANCE.convertMaterialFieldInfoVOList(materialFieldMapper.selectByMaterialIdList(materialIds));
    }

    @Override
    public List<MaterialFieldInfoFeignVO> getMaterialFieldFeignInfo(Long materialId) {
        List<MaterialField> fieldList = materialFieldMapper.selectByMaterialId(materialId);
        if (CollUtil.isEmpty(fieldList)){
            return new ArrayList<>();
        }
        return ProductMaterialConverter.INSTANCE.convertMaterialFieldInfoFeignVOList(fieldList);
    }

    @Override
    public List<MaterialFieldInfoVO> getMaterialFieldInfo(MaterialFieldQueryDTO dto) {
        List<MaterialFieldInfoVO> materialFieldInfo = getMaterialFieldInfo(dto.getMaterialId());
        if (StrUtil.isNotEmpty(dto.getFieldType())) {
            return materialFieldInfo.stream()
                    .filter(item -> StrUtil.equals(item.getFieldType(), dto.getFieldType()))
                    .collect(Collectors.toList());
        }
        return materialFieldInfo;
    }
}
