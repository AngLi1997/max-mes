package com.bmos.lims2.server.material.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.lims2.server.material.entity.MaterialField;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaterialFieldMapper extends BaseMapperX<MaterialField> {

    default List<MaterialField> selectByMaterialId(Long id) {
        return selectList(new LambdaQueryWrapperX<MaterialField>()
                .eq(MaterialField::getMaterialId, id));
    }

    default void deleteByMaterialId(Long id) {
        delete(new LambdaUpdateWrapper<MaterialField>()
                .eq(MaterialField::getMaterialId, id));
    }
}
