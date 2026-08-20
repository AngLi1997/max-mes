package com.bmos.mes.service.output.finished.mapper;

import com.bmos.mes.service.output.finished.model.FinishedProductOutputResult;
import com.bmos.mes.service.output.finished.vo.FinishedProductOutputResultVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface FinishedProductOutputResultMapper extends BaseMapperX<FinishedProductOutputResult> {


    default List<FinishedProductOutputResult> selectByFinishedOutputId(Long id) {
        return selectList(new LambdaQueryWrapperX<FinishedProductOutputResult>()
                .eq(FinishedProductOutputResult::getOutputFinishedProductId, id));
    }

    List<FinishedProductOutputResultVO> selectByProductPlanIds(@Param("productPlanIds") Collection<Long> productPlanIds);
}
