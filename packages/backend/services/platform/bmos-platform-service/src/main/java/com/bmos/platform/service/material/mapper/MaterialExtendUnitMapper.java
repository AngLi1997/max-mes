package com.bmos.platform.service.material.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.material.model.MaterialExtendUnit;
import com.bmos.platform.service.material.vo.MaterialBoundExtendUnitListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface MaterialExtendUnitMapper extends BaseMapperX<MaterialExtendUnit> {


    default void deleteByMaterialId(Long materialId){
        delete(new LambdaQueryWrapperX<MaterialExtendUnit>().eq(MaterialExtendUnit::getMaterialId, materialId));
    }

    List<MaterialBoundExtendUnitListVO> selectMaterialBoundExtendUnitList(Long materialId);

    default void deleteByMaterialIdList(Set<Long> materialIdList){
        delete(new LambdaQueryWrapperX<MaterialExtendUnit>().in(MaterialExtendUnit::getMaterialId, materialIdList));
    }
}
