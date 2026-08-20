package com.bmos.mes.service.storage.manage.mapper;

import com.bmos.mes.service.storage.manage.entity.MaterialBatchField;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * (BmMaterialBatchField)表数据库访问层
 *
 * @author makejava
 * @since 2024-07-23 14:42:21
 */
@Mapper
public interface MaterialBatchFieldMapper extends BaseMapperX<MaterialBatchField> {

    /**
     * 根据物料批次id查询物料批次的自定义字段信息
     * @param materialBatchId
     * @return
     */
    default List<MaterialBatchField> selectMaterialBatchId(Long materialBatchId){
        return selectList(new LambdaQueryWrapperX<MaterialBatchField>()
                .eq(MaterialBatchField::getMaterialBatchId, materialBatchId));
    }

    /**
     * 删除物料批次绑定的物料批次自定义字段
     * @param materialBatchId
     */
    default void deleteByMaterialBatchId(Long materialBatchId){
        delete(new LambdaQueryWrapperX<MaterialBatchField>()
                .eq(MaterialBatchField::getMaterialBatchId, materialBatchId));
    }

    default MaterialBatchField selectMaterialBatchField(Long materialBatchId, String fieldData){
        return selectOne(new LambdaQueryWrapperX<MaterialBatchField>()
                .eq(MaterialBatchField::getMaterialBatchId, materialBatchId)
                .eq(MaterialBatchField::getField, fieldData));
    }

    default List<MaterialBatchField> selectByMaterialBatchIdList(Collection<Long> batchIds){
        return selectList(new LambdaQueryWrapperX<MaterialBatchField>()
                .in(MaterialBatchField::getMaterialBatchId, batchIds));
    }
}

