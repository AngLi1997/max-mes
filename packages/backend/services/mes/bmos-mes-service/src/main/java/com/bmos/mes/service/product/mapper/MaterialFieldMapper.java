package com.bmos.mes.service.product.mapper;

import com.bmos.mes.service.product.model.MaterialField;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * (BmMaterialField)表数据库访问层
 *
 * @author makejava
 * @since 2024-07-23 13:39:29
 */
@Mapper
public interface MaterialFieldMapper extends BaseMapperX<MaterialField> {

    /**
     * 根据生产物料id查询物料自定义字段信息
     * @param materialId
     * @return
     */
    default List<MaterialField> selectByMaterialId(Long materialId){
        return selectList(new LambdaQueryWrapperX<MaterialField>().eq(MaterialField::getMaterialId, materialId));
    }

    /**
     * 根据生产物料id删除物料自定义字段信息
     * @param materialId
     */
    default void deleteByMaterialId(Long materialId){
        delete(new LambdaQueryWrapperX<MaterialField>().eq(MaterialField::getMaterialId, materialId));
    }

    /**
     * 根据物料id列表查询自定义字段信息
     * @param materialIds
     * @return
     */
    default List<MaterialField> selectByMaterialIdList(Collection<Long> materialIds){
        return selectList(new LambdaQueryWrapperX<MaterialField>().in(MaterialField::getMaterialId, materialIds));
    }
}

