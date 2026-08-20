package com.bmos.mes.service.plan.production.service;

import com.bmos.mes.service.plan.production.dto.*;
import com.bmos.mes.service.plan.production.vo.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface ProductionPlanService {


    CommonPage<ProductionPlanPageVO> listPage(ProductionPageDTO dto);

    void planNullify(Long id);

    ProductionPlanDetailVO listPlanDetail(Long id);

    List<List<ProductionPlanItemDetailVO>> buildPlan(BuildPlanDTO dto);

    PlanBatchNextNoMessageVO buildBatchNo(List<List<PlanBatchNoDTO>> dto);

    /**
     * 下发生产计划
     * @param dto
     */
    ProductionPlanIssueResVO issueProductionPlan(ProductionPlanIssueDTO dto);

    /**
     * 直接创建指令单：生成编号
     * @param dto
     */
    DirectlyCreateBuildNoVO buildPlanNoAndBatchNo(DirectlyCreateBuildNoDTO dto);

    /**
     * 直接创建指令单：指令单保存
     * @param dto
     */
    void directlyCreatePlan(DirectlyCreatePlanDTO dto);
}
