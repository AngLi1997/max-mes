package com.bmos.mes.service.plan.info.service;

import com.bmos.mes.service.plan.info.dto.PlanRetraceInfoPageDTO;
import com.bmos.mes.service.plan.info.dto.ProductPlanBatchDTO;
import com.bmos.mes.service.plan.info.vo.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 批次追溯
 */
public interface PlanRetraceService {
    /**
     * 查询产品已完成的生产批次的分页信息
     * @param dto
     * @return
     */
    CommonPage<ProductPlanBatchPageVO> planBatchRetracePage(ProductPlanBatchDTO dto);

    /**
     * 查询产品已完成的生产批次的简单信息
     * @param dto
     * @return
     */
    PlanRetraceInfoVO detailInfo(PlanRetraceInfoPageDTO dto);

    /**
     * 批次追溯-生产批次的执行信息
     * @param dto
     * @return
     */
    CommonPage<PlanRetraceExecutePageVO> executeTracePage(PlanRetraceInfoPageDTO dto);

    /**
     * 批次追溯-生产批次的物料信息
     * @param dto
     * @return
     */
    CommonPage<PlanRetraceMaterialPageVO> materialTracePage(PlanRetraceInfoPageDTO dto);

    /**
     * 批次追溯-生产批次的设备使用日志
     * @param dto
     * @return
     */
    CommonPage<PlanRetraceEquipmentPageVO> equipmentTracePage(PlanRetraceInfoPageDTO dto);

    /**
     * 批次追溯-生产批次的房间清场信息
     * @param dto
     * @return
     */
    CommonPage<PlanRetraceRoomPageVO> roomTracePage(PlanRetraceInfoPageDTO dto);

    /**
     * 批次追溯-生产批次的偏差信息
     * @param dto
     * @return
     */
    CommonPage<PlanRetraceDeviationPageVO> procedureTracePage(PlanRetraceInfoPageDTO dto);

    /**
     * 根据生产计划id获取步骤任务执行列表
     * @param id
     * @return
     */
    List<ProcedureStepTaskExecuteVO> getProcedureStepTaskExecuteList(Long id);
}
