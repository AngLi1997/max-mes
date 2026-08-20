package com.bmos.lims2.server.material.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.material.converter.MaterialConvert;
import com.bmos.lims2.server.material.dto.*;
import com.bmos.lims2.server.material.entity.MaterialField;
import com.bmos.lims2.server.material.mapper.MaterialFieldMapper;
import com.bmos.lims2.server.material.service.MaterialFieldService;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.platform.facade.dict.enums.DictCodeConstants;
import com.bmos.platform.facade.dict.feign.DictFeign;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MaterialFieldServiceImpl implements MaterialFieldService {

    @Resource
    private MaterialFieldMapper materialFieldMapper;

    @Autowired
    private DictFeign dictFeign;


    /**
     * 物料自定义字段暂时先在此处进行描写
     * 若后期在平台维护专属物料自定义字段类型，在从平台测进行获取
     */
    public static final List<String> MATERIAL_FIELD_TYPE_LIST = Lists.newArrayList(DictCodeConstants.MATERIAL_BATCH_CUSTOM_FIELDS,
            DictCodeConstants.MATERIAL_CUSTOM_FIELDS,
            DictCodeConstants.MATERIAL_PIECE_CUSTOM_FIELDS);

    @Override
    public void saveBasicProductsFields(Long id, List<MaterialFieldSaveDTO> fieldSaveDTOList) {
        if (CollUtil.isEmpty(fieldSaveDTOList)) {
            return;
        }
        List<MaterialField> fields = MaterialConvert.INSTANCE.convert2ProductsField(fieldSaveDTOList, id);
        materialFieldMapper.insertBatch(fields);
    }

    @Override
    public List<MaterialFieldDTO> getByProductsId(Long id) {
        List<MaterialField> basicProductsFields = materialFieldMapper.selectByMaterialId(id);
        return MaterialConvert.INSTANCE.convert2ProductsFieldVO(basicProductsFields);
    }

    @Override
    public void deleteByProductsId(Long id) {
        materialFieldMapper.deleteByMaterialId(id);
    }


    @Override
    public List<MaterialFieldTypeDTO> getMaterialFieldList() {
        // 生产物料自定义字段需要获取的字典类型
        ResponseInfo<List<DictDetailFeignVO>> listResponseInfo = FeignUtils.handleRequest(data -> dictFeign.selectDictByCategory(data), MATERIAL_FIELD_TYPE_LIST);
        if (!listResponseInfo.isSuccess()){
            return new ArrayList<>();
        }
        return MaterialConvert.INSTANCE.convert2MaterialFieldTypeVO(listResponseInfo.getData());
    }



    @Override
    public List<MaterialFieldInfoDTO> getMaterialFieldInfo(Long id) {
        // 根据物料id获取物料自定义字段
        List<MaterialField> fieldList = materialFieldMapper.selectByMaterialId(id);
        if (CollUtil.isEmpty(fieldList)){
            return new ArrayList<>();
        }
        return MaterialConvert.INSTANCE.convertMaterialFieldInfoDTOList(fieldList);
    }

    @Override
    public List<MaterialFieldInfoDTO> getMaterialFieldInfo(MaterialFieldQueryDTO dto) {
        List<MaterialFieldInfoDTO> materialFieldInfo = getMaterialFieldInfo(dto.getMaterialId());
        if (StrUtil.isNotEmpty(dto.getFieldType())) {
            return materialFieldInfo.stream()
                    .filter(item -> StrUtil.equals(item.getFieldType(), dto.getFieldType()))
                    .collect(Collectors.toList());
        }
        return materialFieldInfo;
    }
}
