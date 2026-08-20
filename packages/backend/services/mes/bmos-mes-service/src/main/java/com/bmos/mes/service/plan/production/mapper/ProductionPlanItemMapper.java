package com.bmos.mes.service.plan.production.mapper;

import com.bmos.mes.service.plan.production.dto.ProductionPlanCalendarQueryDTO;
import com.bmos.mes.service.plan.production.dto.ProductionPlanMonthsCalendarQueryDTO;
import com.bmos.mes.service.plan.production.model.ProductionPlanItem;
import com.bmos.mes.service.plan.production.vo.ProductionPlanCalendarVO;
import com.bmos.mes.service.plan.production.vo.ProductionPlanItemDetailVO;
import com.bmos.mes.service.plan.production.vo.ProductionPlanItemVO;
import com.bmos.mes.service.workflow.vo.ProcedureTimeVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ProductionPlanItemMapper extends BaseMapperX<ProductionPlanItem> {


    default List<ProductionPlanItem> queryByProductionPlanId(Long id){
        return selectList(new LambdaQueryWrapperX<ProductionPlanItem>()
                .eq(ProductionPlanItem::getProductionPlanId,id));
    }

    List<ProductionPlanItemDetailVO> selectDetailByProductionPlanId(@Param("productionPlanId") Long id);

    List<ProductionPlanCalendarVO> selectProductionPlanCalendar(ProductionPlanCalendarQueryDTO dto);

    List<ProductionPlanItemVO> selectProductPlanInfoByItemIdList(@Param("idList") List<Long> idList);

    List<ProcedureTimeVO> selectProcedureConfigByPlanIds(@Param("planItemIds") Set<Long> productionPlanItemId);

    default List<ProductionPlanItem> queryListByProductionPlanIdS(List<Long> productionPlanIds){
        return selectList(new LambdaQueryWrapperX<ProductionPlanItem>()
                .in(ProductionPlanItem::getProductionPlanId,productionPlanIds)
                .orderByDesc(ProductionPlanItem::getEndTime));
    }

    List<ProductionPlanCalendarVO> selectProductionPlanMonthsCalendar(ProductionPlanMonthsCalendarQueryDTO dto);
}
