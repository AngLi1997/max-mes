package com.bmos.platform.service.material.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.platform.service.material.dto.MaterialBindExtendUnitDTO;
import com.bmos.platform.service.material.mapper.MaterialExtendUnitMapper;
import com.bmos.platform.service.material.mapper.MaterialMapper;
import com.bmos.platform.service.material.model.MaterialExtendUnit;
import com.bmos.platform.service.material.service.MaterialExtendUnitService;
import com.bmos.platform.service.material.vo.MaterialBoundExtendUnitListVO;
import com.bmos.platform.service.unit.constant.UnitConstant;
import com.bmos.platform.service.unit.mapper.UnitExtendMapper;
import com.bmos.unit.service.UnitCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MaterialExtendUnitServiceImpl implements MaterialExtendUnitService {


    @Autowired
    private MaterialExtendUnitMapper materialExtendUnitMapper;

    @Autowired
    private UnitExtendMapper unitExtendMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Resource
    private UnitCache unitCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindExtendUnit(MaterialBindExtendUnitDTO dto) {
        materialExtendUnitMapper.deleteByMaterialId(dto.getMaterialId());
        if(CollUtil.isNotEmpty(dto.getExtendUnitIdList())){
            List<MaterialExtendUnit> materialExtendUnits = dto.getExtendUnitIdList().stream().map(id -> {
                MaterialExtendUnit materialExtendUnit = new MaterialExtendUnit();
                materialExtendUnit.setMaterialId(dto.getMaterialId());
                materialExtendUnit.setExtendUnitId(id);
                return materialExtendUnit;
            }).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(materialExtendUnits)){
                materialExtendUnitMapper.insertBatch(materialExtendUnits);
            }
        }
    }

    @Override
    public List<MaterialBoundExtendUnitListVO> getMaterialBoundExtendUnitList(Long materialId) {
        List<MaterialBoundExtendUnitListVO> list = materialExtendUnitMapper.selectMaterialBoundExtendUnitList(materialId);
        if(CollUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        list.forEach(vo->{
            vo.setExpression(UnitConstant.ONE + vo.getExtendUnitName() + UnitConstant.SYMOL +
                    vo.getExpressionValue() + vo.getUnitName());
        });
        return list;
    }

    @Override
    public void deleteBoundRelationByMaterialId(Long materialId) {
        materialExtendUnitMapper.deleteByMaterialId(materialId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindExtendUnitBatch(List<MaterialBindExtendUnitDTO> list) {
        if (CollUtil.isEmpty(list)){
            return;
        }
        Set<Long> materialIdList = CollectionUtils.convertSet(list, MaterialBindExtendUnitDTO::getMaterialId);
        materialExtendUnitMapper.deleteByMaterialIdList(materialIdList);
        List<MaterialExtendUnit> materialExtendUnits = new ArrayList<>();
        list.forEach(item->{
            if(CollUtil.isEmpty(item.getExtendUnitIdList())){
                return;
            }
            List<MaterialExtendUnit> extendUnits = item.getExtendUnitIdList().stream().map(id -> {
                MaterialExtendUnit materialExtendUnit = new MaterialExtendUnit();
                materialExtendUnit.setMaterialId(item.getMaterialId());
                materialExtendUnit.setExtendUnitId(id);
                return materialExtendUnit;
            }).collect(Collectors.toList());
            materialExtendUnits.addAll(extendUnits);
        });
        if(CollUtil.isNotEmpty(materialExtendUnits)){
                materialExtendUnitMapper.insertBatch(materialExtendUnits);
        }
    }
}
