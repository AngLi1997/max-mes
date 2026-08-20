package com.bmos.mes.service.plan.info.mapper;

import com.bmos.mes.service.plan.info.model.ProductPlanNoInfo;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductPlanNoInfoMapper extends BaseMapperX<ProductPlanNoInfo> {

    default ProductPlanNoInfo selectByProductPlanId(Long id) {
        return selectOne(new LambdaQueryWrapperX<ProductPlanNoInfo>()
                .eq(ProductPlanNoInfo::getProductPlanId, id));
    }
}
