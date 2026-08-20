package com.bmos.mes.service.plan.production.mapper;

import com.bmos.mes.service.plan.production.dto.ProductionPageDTO;
import com.bmos.mes.service.plan.production.model.ProductionPlan;
import com.bmos.mes.service.plan.production.vo.ProductionPlanDetailVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductionPlanMapper extends BaseMapperX<ProductionPlan> {

    List<ProductionPlan> listPage(ProductionPageDTO dto);

    ProductionPlanDetailVO selectDetailById(@Param("id") Long id);

    default boolean existedProductionPlanName(String name){
        return exists(new LambdaQueryWrapperX<ProductionPlan>()
                .eq(ProductionPlan::getPlanName, name));
    }
}
