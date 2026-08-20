package com.bmos.mes.service.plan.info.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.mes.service.audit.vo.AuditCategoryCountVO;
import com.bmos.mes.service.formula.dto.StorageMaterialReservedQuantityDTO;
import com.bmos.mes.service.formula.vo.PlanReservedMaterialQuantityInfoVO;
import com.bmos.mes.service.plan.info.dto.*;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.model.ProductPlanNoInfo;
import com.bmos.mes.service.plan.info.vo.*;
import com.bmos.mes.service.workflow.dto.query.WorkflowTodoPageDTO;
import com.bmos.mes.service.workflow.dto.AppPlanHistoryDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.Collection;
import java.util.List;

public interface PlanService extends IService<Plan> {
    /**
     * 分页列表
     *
     * @param dto dto
     * @return List<PlanPageVO>
     */
    List<PlanPageVO> page(PlanPageDTO dto);

    /**
     * 待办任务数量
     *
     * @return
     */
    List<AuditCategoryCountVO> waitTaskCount();

    CommonPage<PlanPageVO> pageTraceable(PlanTraceablePageDTO dto);

    /**
     * 审核分页
     *
     * @param dto dto
     * @return CommonPage<PlanAuditPageVO>
     */
    CommonPage<PlanAuditPageVO> auditPage(PlanAuditPageDTO dto);

    /**
     * 进行中的生产分页列表
     *
     * @param dto dto
     * @return List<PlanPageVO>
     */
    List<Plan> productManagePage(PlanPageDTO dto);


    /**
     * 分页列表
     *
     * @param dto dto
     * @return List<PlanPageVO>
     */
    List<PlanStartPageVO> startPage(PlanStartPageDTO dto);

    /**
     * 生产计划详情
     *
     * @param id id
     * @return PlanDetailVO
     */
    PlanDetailVO detail(Long id);


    /**
     * 生产计划更新
     *
     * @param dto dto
     */
    void update(PlanUpdateDTO dto);

    /**
     * 废弃
     *
     * @param id id
     */
    void discard(Long id);

    /**
     * 发起审核
     *
     * @param id id
     */
    void approve(Long id);

    /**
     * 审核成功回调
     *
     * @param processInstanceId processInstanceId
     */
    void auditSuccess(String processInstanceId);

    /**
     * 审核失败回调
     *
     * @param processInstanceId processInstanceId
     */
    void auditTermination(String remark, String processInstanceId, String nodeName, String comment, Long businessKey);

    /**
     * 节点审批记录日志
     */
    void auditPlanLog(String businessKey, String remark, String userId, String nodeName, String comment);

    /**
     * 生产计划执行完毕回调成功
     *
     * @param executeProcessInstanceId id
     */
    void executeCallBackSuccess(String executeProcessInstanceId);

    /**
     * 生产计划执行完毕回调终止
     *
     * @param executeProcessInstanceId id
     */
    void executeCallBackTermination(String executeProcessInstanceId);

    /**
     * 实例id查询生产计划信息
     *
     * @param processInstanceIds processInstanceIds
     * @return List<Plan>
     */
    List<Plan> selectByProcessInstanceIds(List<String> processInstanceIds);

    Plan getById(Long id);

    List<String> getAuditBusinessKey(List<Long> deptIdList);

    void pauseExecute(Long id);

    void recoveryExecute(Long id);

    /**
     * 获取所有未开始/已生产生产批次的简单信息
     *
     * @return
     */
    List<PlanEasyInfoVO> batchListByPlanStart();

    /**
     * @param dto
     * @return 单个物料预定校验量计算查询
     */
    PlanReservedMaterialQuantityInfoVO queryPlanReservedStorageMaterialQuantity(StorageMaterialReservedQuantityDTO dto);

    /**
     * @param productPlanId 生产计划id
     * @param materialList  配方物料及校验量信息列表
     * @return
     */
    List<PlanReservedMaterialQuantityInfoVO> queryPlanReservedStorageMaterialQuantity(Long productPlanId,
                                                                                      List<StorageMaterialReservedQuantityDTO> materialList);

    /**
     * 查询正在执行中的生产计划列表
     *
     * @param dto
     * @return
     */
    List<PlanEasyInfoVO> startPlanList(PlanStartQueryDTO dto);

    Plan selectByExecuteProcessInstanceId(String processInstanceId);

    List<Plan> productManagePageHistory(AppPlanHistoryDTO pageDTO);

    /**
     * 根据产品id和工艺id查询未终止的生产批次信息
     * @param productId 产品id
     * @param processId 工艺id
     * @return
     */
    List<PlanSimpleVO> queryBatchListByProductIdAndProcessId(Long productId, Long processId);

    /**
     * 生产计划批量提交审批
     * @param dto
     */
    void approveBatch(PlanApproveBatchDTO dto);

    /**
     * 根据工艺id列表和计划状态查询生产计划
     * @param processIdList
     * @param planStartEnumList
     * @return
     */
    List<Plan> selectByProcessIdList(List<Long> processIdList, List<String> planStartEnumList);

    /**
     * 根据生产计划id集合查询生产计划
     * @param idList
     * @return
     */
    List<Plan> getByIds(Collection<Long> idList);


    /**
     * 根据工艺信息查询生产批次列表
     * @param dto
     * @return
     */
    List<PlanListVO> queryPlanListByProcess(PlanListByProcessDTO dto);

    /**
     * 查询生产审核进度分页
     * @param dto
     * @return
     */
    CommonPage<ProductionAuditProgressPageVO> queryProductionAuditProgressPage(ProductionAuditProgressQueryDTO dto);

    /**
     * 查询生产审核进度详情列表
     * @param dto
     * @return
     */
    List<PlanAuditProgressDetailVO> queryPlanAuditDetailList(PlanAuditProgressDetailQueryDTO dto);

    /**
     * 根据产品id和工艺id查询未终止的生产批次信息列表
     * @param processId 工艺id
     * @return
     */
    List<PlanListVO> listUnTerminatePlanByProcessId(Long processId);

    /**
     * 查询生产计划所有关联的批次信息
     * @param planId
     * @return
     */
    List<PlanEasyInfoVO> relationPlan(Long planId);


    List<Plan> queryByProductionPlanIdS(List<Long> planItemIdS);

    /**
     * 批量保存生产指令单
     * @param saveDTOS
     */
    void batchSave(List<PlanSaveDTO> saveDTOS);

    /**
     * 更新批次关联关系
     * @param dto
     */
    void updateRelation(PlanRelationUpdateDTO dto);

    /**
     * 根据产品id查询所有生产批次(不区分状态)
     * @param productId
     * @return
     */
    List<PlanListVO> listAllPlanByProductId(Long productId);

    Plan getPlanByPoductionPlanItemId(Long productionPlanItemId);

    List<Plan> selectByProductionPlanItemId(List<Long> productionItemId);

    List<Plan> getPlanListByProcedureVersionId(Long processId, Collection<String> processVersionList, int n);

    List<PlanPageVO> getTodoPlanStart(WorkflowTodoPageDTO dto, List<Long> processIds,String todoType);

    Integer productManagePageCount(PlanPageDTO dto);

    void savePlanNoInfo(ProductPlanNoInfo productPlanNoInfo);
}
