package com.bmos.mes.service.product.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.service.product.dto.FinishProductTreeQueryDTO;
import com.bmos.mes.service.product.dto.MaterialPrincipalQueryDTO;
import com.bmos.mes.service.product.dto.ProductMaterialChangeStatusDTO;
import com.bmos.mes.service.product.dto.ProductMaterialPageQueryDTO;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.vo.ProcessProductVO;
import com.bmos.mes.service.product.vo.ProductCategoryTreeNodeVO;
import com.bmos.mes.service.product.vo.ProductListVO;
import com.bmos.mes.service.product.vo.ProductMaterialPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface ProductMaterialMapper extends BaseMapperX<ProductMaterial> {

    default Boolean existsCategory(Long categoryId) {
        return exists(new LambdaQueryWrapperX<ProductMaterial>()
                .eq(ProductMaterial::getMaterialCategoryId, categoryId));
    }

    List<ProductMaterialPageVO> selectPageList(ProductMaterialPageQueryDTO dto);

    default void updateStatus(ProductMaterialChangeStatusDTO dto) {
        ProductMaterial productMaterial = new ProductMaterial();
        productMaterial.setId(dto.getId());
        productMaterial.setStatus(dto.getStatus());
        update(null, new LambdaUpdateWrapper<ProductMaterial>()
                .set(ProductMaterial::getStatus, dto.getStatus())
                .eq(ProductMaterial::getId, dto.getId()));
    }

    default boolean existsMultipleRecord(Long platformMaterialId) {
        return selectCount(new LambdaQueryWrapperX<ProductMaterial>()
                .eq(ProductMaterial::getPlatformMaterialId, platformMaterialId)) > 1;
    }

    default List<ProductMaterial> selectByPlatformMaterialIds(List<Long> materialIds) {
        return selectList(new LambdaQueryWrapperX<ProductMaterial>()
                .in(ProductMaterial::getPlatformMaterialId, materialIds));
    }


    List<ProductMaterial> selectByPlatformMaterialIdsAndType(@Param("list") List<Long> map, @Param("type") Integer categoryType);

    List<ProductMaterial> selectPrincipalList(MaterialPrincipalQueryDTO dto);


    List<ProductMaterial> selectByPlatformMaterialIdsAndTypes(@Param("ids") List<Long> materialsIds, @Param("types") List<Integer> businesses);

    List<ProductListVO> selectEnabledByType(Integer categoryType);

    List<ProductCategoryTreeNodeVO> selectEnabledTreeNodeByType(Integer categoryType);

    List<ProductMaterial> selectByIdsAndType(@Param("ids") Set<Long> ids, @Param("type") Integer type);

    List<ProductListVO> getFinishProductListVO(@Param("categoryType") Integer categoryType);

    List<ProductCategoryTreeNodeVO> selectEnabledTreeNodeByTypes(List<Integer> types);

    List<Long> selectEnabledIdListByCategoryIds(@Param("list") List<Long> allChildCategory, @Param("finished") Boolean finished);

    List<ProductCategoryTreeNodeVO> selectEnabledFinishProductTreeNodeByType(FinishProductTreeQueryDTO dto);

    default boolean existedMemberMaterial(Long id) {
        return exists(new LambdaQueryWrapperX<ProductMaterial>()
                .eq(ProductMaterial::getPrincipalMaterialId, id));
    }

    List<ProductMaterial> selectListByBatchIds(@Param("ids") Collection<Long> ids);

    ProductMaterial selectAllInfoById(Long id);

    default List<ProductMaterial> getIdListByCategoryIdList(List<Long> categoryIdList) {
        return selectList(new LambdaQueryWrapperX<ProductMaterial>()
                .in(ProductMaterial::getMaterialCategoryId, categoryIdList));
    }

    default List<ProductMaterial> selectSubMaterialById(Long materialId) {
        return selectList(new LambdaQueryWrapperX<ProductMaterial>().eq(ProductMaterial::getPrincipalMaterialId, materialId));
    }

    default List<ProductMaterial> selectSubMaterialByIds(Collection<Long> materialIds) {
        return selectList(new LambdaQueryWrapperX<ProductMaterial>()
                .in(ProductMaterial::getPrincipalMaterialId, materialIds));
    }

    List<ProcessProductVO> selectByProcessIdList(@Param("processIds") Collection<Long> processIds);

}
