package com.bmos.mes.service.formula.mapper;

import com.bmos.mes.service.formula.dto.ListProcedureMaterialDTO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductFormulaMaterialMapper extends BaseMapperX<ProductFormulaMaterial> {

    default List<ProductFormulaMaterial> selectByVersionId(Long versionId) {
        return selectList(new LambdaQueryWrapperX<ProductFormulaMaterial>()
                .eq(ProductFormulaMaterial::getVersionId, versionId));
    }

    default void deleteByVersionId(Long id) {
        delete(new LambdaQueryWrapperX<ProductFormulaMaterial>()
                .eq(ProductFormulaMaterial::getVersionId, id));
    }

    default boolean existedFormulaMaterial(Long materialId) {
        return exists(new LambdaQueryWrapperX<ProductFormulaMaterial>().eq(ProductFormulaMaterial::getMaterialId, materialId));
    }

    List<ProductFormulaMaterial> selectByProcedureId(@Param("procedureModelId") Long procedureModelId);

    default List<ProductFormulaMaterial> selectByIdList(List<Long> formulaMaterialIdList){
        return selectList(new LambdaQueryWrapperX<ProductFormulaMaterial>()
                .in(ProductFormulaMaterial::getId, formulaMaterialIdList));
    }

    List<ProductFormulaMaterial> selectProcedureMaterial(ListProcedureMaterialDTO dto);

    List<ProductFormulaMaterial> selectListByDisabledIds(@Param("idList") List<Long> disabledId);
}
