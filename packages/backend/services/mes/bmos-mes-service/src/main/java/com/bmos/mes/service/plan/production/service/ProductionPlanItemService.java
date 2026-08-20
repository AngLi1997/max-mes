package com.bmos.mes.service.plan.production.service;


import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.production.dto.ProductionPlanCalendarChangeDTO;
import com.bmos.mes.service.plan.production.dto.ProductionPlanCalendarQueryDTO;
import com.bmos.mes.service.plan.production.dto.ProductionPlanItemSaveDTO;
import com.bmos.mes.service.plan.production.dto.ProductionPlanMonthsCalendarQueryDTO;
import com.bmos.mes.service.plan.production.model.ProductionPlanItem;
import com.bmos.mes.service.plan.production.vo.ProductionPlanCalendarVO;
import com.bmos.mes.service.plan.production.vo.ProductionPlanItemDetailVO;
import com.bmos.mes.service.process.vo.ProcessDetailVO;
import com.bmos.mes.service.workflow.vo.ProcedureTimeVO;

import java.util.List;
import java.util.Set;

public interface ProductionPlanItemService {

    /**
     * 校验生产计划
     * @param id 生产计划id
     * @return
     */
     void checkStartingPlanList(Long id);

    void nullifyPlan(Long id);

    List<List<ProductionPlanItemDetailVO>> selectDetailByProductionPlanId(Long id);

    /**
     * 调整生产计划日历
     * @param dto
     * @return
     */
    void changeProductionItemCalendar(ProductionPlanCalendarChangeDTO dto);

    /**
     * 获取生产计划日历
     * @param dto
     * @return
     */
    List<ProductionPlanCalendarVO> getProductionPlanCalendar(ProductionPlanCalendarQueryDTO dto);

    /**
     * 下发生产计划详情
     * @param itemList 生产计划详情列表
     * @param id 生产计划id
     * @param planType 计划类型
     */
    void issueProductionPlanItem(List<ProductionPlanItemSaveDTO> itemList, Long id, String planType);

    List<ProcedureTimeVO> selectProcedureConfigByPlanIds(Set<Long> productionPlanItemId);

    /**
     * 获取生产计划日历
     * 可以查询多月份以及跨年查询
     * @param dto
     * @return
     */
    List<ProductionPlanCalendarVO> getProductionPlanMonthsCalendar(ProductionPlanMonthsCalendarQueryDTO dto);

    /**
     * 根据生产计划id列表查询其下生产批次项
     * @param productionPlanIds
     * @return
     */
    List<ProductionPlanItem> queryListByProductionPlanIdS(List<Long> productionPlanIds);

    /**
     * 保存直接创建指令单的生产批次项
     * @param plan
     * @param detail
     * @return
     */
    ProductionPlanItem SaveDirectlyCreatedPlanItem(Plan plan, ProcessDetailVO detail);
}
