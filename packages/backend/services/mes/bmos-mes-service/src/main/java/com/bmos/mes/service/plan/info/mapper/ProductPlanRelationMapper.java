package com.bmos.mes.service.plan.info.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.service.plan.info.model.ProductPlanRelation;
import com.bmos.mes.service.plan.info.vo.ProductPlanRelationListVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ProductPlanRelationMapper extends BaseMapperX<ProductPlanRelation> {
    default List<ProductPlanRelation> selectProductPlanId(List<Long> productPlanIdList) {
        if (CollectionUtil.isEmpty(productPlanIdList)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<ProductPlanRelation>()
                .in(ProductPlanRelation::getProductPlanId, productPlanIdList));
    }

    default List<ProductPlanRelation> selectByRelationProductPlanId(List<Long> productPlanIdList) {
        if (CollectionUtil.isEmpty(productPlanIdList)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<ProductPlanRelation>()
                .in(ProductPlanRelation::getRelationProductPlanId, productPlanIdList));
    }

    default List<ProductPlanRelation> selectByProductPlanId(Long productPlanId) {
        return selectList(ProductPlanRelation::getProductPlanId, productPlanId);
    }

    List<ProductPlanRelationListVO> detail(@Param("productPlanId") Long productPlanId);

    default void deleteByProductPlanId(Long id) {
        delete(new LambdaUpdateWrapper<ProductPlanRelation>()
                .eq(ProductPlanRelation::getProductPlanId, id));
    }

    /**
     * 删除间接关联关系
     * eg:
     * @param deleteIds 要删除的关联关系id
     * @param sourcePlanId 间接关联的来源id
     */
    default void deleteIndirectRelations(List<Long> deleteIds, Long sourcePlanId){
        delete(new LambdaQueryWrapperX<ProductPlanRelation>()
                .eq(ProductPlanRelation::getSourceProductPlanId, sourcePlanId)
                .in(ProductPlanRelation::getRelationProductPlanId, deleteIds)
                .eq(ProductPlanRelation::getIsDirectRelation, BooleanEnum.FALSE.getValue()));
    }

    default void deleteDirectRelation(Long id, List<Long> deleteIds){
        delete(new LambdaQueryWrapperX<ProductPlanRelation>()
                .eq(ProductPlanRelation::getProductPlanId, id)
                .in(ProductPlanRelation::getRelationProductPlanId, deleteIds)
                .eq(ProductPlanRelation::getIsDirectRelation, BooleanEnum.TRUE.getValue()));
    }

    /**
     * 查询批次的直接关联
     * @param productPlanId
     * @return
     */
    default List<ProductPlanRelation> selectDirectByProductPlanId(Long productPlanId){
        return selectList(new LambdaQueryWrapperX<ProductPlanRelation>()
                .eq(ProductPlanRelation::getProductPlanId, productPlanId)
                .eq(ProductPlanRelation::getIsDirectRelation, BooleanEnum.TRUE.getValue()));
    }

    default void deleteBySourceIds(List<Long> deleteIds, Long id){
        delete(new LambdaQueryWrapperX<ProductPlanRelation>()
                .eq(ProductPlanRelation::getProductPlanId, id)
                .in(ProductPlanRelation::getSourceProductPlanId, deleteIds)
                .eq(ProductPlanRelation::getIsDirectRelation, BooleanEnum.FALSE.getValue()));
    }

    default List<ProductPlanRelation> selectDirectByPlanIdList(List<Long> collect){
        return selectList(new LambdaQueryWrapperX<ProductPlanRelation>()
                .in(ProductPlanRelation::getProductPlanId, collect)
                .eq(ProductPlanRelation::getIsDirectRelation, BooleanEnum.TRUE.getValue()));
    }
}
