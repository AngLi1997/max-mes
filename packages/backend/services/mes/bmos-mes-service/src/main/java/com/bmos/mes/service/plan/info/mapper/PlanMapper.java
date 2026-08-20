package com.bmos.mes.service.plan.info.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.common.enums.plan.ProductPlanInstructStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStatusEnum;
import com.bmos.mes.service.lotrelease.manage.dto.LotReleasePlanPageQuery;
import com.bmos.mes.service.plan.info.dto.*;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.vo.PlanPageVO;
import com.bmos.mes.service.plan.info.vo.PlanStartPageVO;
import com.bmos.mes.service.plan.info.vo.ProductionAuditProgressPageVO;
import com.bmos.mes.service.plan.production.dto.DirectlyCreatePlanDTO;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.workflow.dto.AppPlanHistoryDTO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper
public interface PlanMapper extends BaseMapperX<Plan> {
    /**
     * 分页查询
     *
     * @param dto dto
     * @return List<PlanPageVO>
     */
    List<PlanPageVO> page(PlanPageDTO dto);

    /**
     * 分页查询
     *
     * @param dto dto
     * @return List<PlanPageVO>
     */
    List<PlanPageVO> pageTraceable(PlanTraceablePageDTO dto);

    List<Long> selectAudit(PlanAuditPageDTO dto);

    default List<Plan> selectByProcessInstanceIds(List<String> processInstanceIds) {
        if (CollUtil.isEmpty(processInstanceIds)) {
            return Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<Plan>()
                .in(Plan::getProcessInstanceId, processInstanceIds)
        );
    }

    default Plan selectByExecuteProcessInstanceId(String executeProcessInstanceId) {
        return selectOne(new LambdaQueryWrapperX<Plan>()
                .in(Plan::getExecuteProcessInstanceId, executeProcessInstanceId)
        );
    }

    List<Plan> productManagePage(PlanPageDTO dto);

    List<PlanStartPageVO> startPage(PlanStartPageDTO dto);

    default void discard(Long id, String planNo, String batchNo) {
        updateById(Plan.builder().id(id).planNo(planNo).batchNo(batchNo).status(ProductPlanStatusEnum.DISCARD).build());
    }

    default void approve(Long id, String processInstanceId) {
        updateById(Plan.builder().id(id).status(ProductPlanStatusEnum.AUDIT).processInstanceId(processInstanceId).build());
    }

    default void auditSuccess(String processInstanceId) {
        update(null, new LambdaUpdateWrapper<Plan>()
                .eq(Plan::getProcessInstanceId, processInstanceId)
                .set(Plan::getStatus, ProductPlanStatusEnum.CONFIRM)
                .set(Plan::getConfirmTime, LocalDateTime.now())
        );
    }

    default void auditTermination(String processInstanceId) {
        update(null, new LambdaUpdateWrapper<Plan>()
                .eq(Plan::getProcessInstanceId, processInstanceId)
                .set(Plan::getStatus, ProductPlanStatusEnum.EDIT)
                .set(Plan::getProcessInstanceId, null)
        );
    }

    default void executeCallBackSuccess(String executeProcessInstanceId) {
        update(null, new LambdaUpdateWrapper<Plan>()
                .eq(Plan::getExecuteProcessInstanceId, executeProcessInstanceId)
                .set(Plan::getStart, ProductPlanStartEnum.END)
                .set(Plan::getEndTime, LocalDateTime.now())
        );
    }

    default void executeCallBackTermination(String executeProcessInstanceId) {
        update(null, new LambdaUpdateWrapper<Plan>()
                .eq(Plan::getExecuteProcessInstanceId, executeProcessInstanceId)
                .set(Plan::getStart, ProductPlanStartEnum.TERMINATION)
                .set(Plan::getEndTime, LocalDateTime.now())
        );
    }

    default void updateInstructStatus(Long id, ProductPlanInstructStatusEnum status) {
        updateById(Plan.builder().id(id).instructStatus(status).build());
    }

    default void relation(List<Long> planIds) {
        if (CollUtil.isEmpty(planIds)) {
            return;
        }
        update(Plan.builder().relation(BooleanEnum.TRUE).build(), new LambdaQueryWrapperX<Plan>()
                .in(Plan::getId, planIds)
        );
    }

    List<Plan> getAuditBusinessKey(@Param("deptIdList") List<Long> deptIdList);

    Long selectPlanFormulaVersionId(@Param("productPlanId") Long productPlanId);

    default List<Plan> batchListByPlanStart() {
        return selectList(new LambdaQueryWrapperX<Plan>()
                .eq(Plan::getStart, ProductPlanStartEnum.STARTING.getValue()));
    }

    /**
     * 查询正在执行中的生产计划
     *
     * @param dto
     * @return
     */
    default List<Plan> selectByStartList(PlanStartQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<Plan>()
                .eq(Plan::getStart, ProductPlanStartEnum.STARTING.getValue())
                .eq(Plan::getProductId, dto.getProductId()));
    }

    List<Plan> productManagePageHistory(AppPlanHistoryDTO pageDTO);

    /**
     * 根据产品id和工艺id查询未终止的生产批次信息
     *
     * @param productId 产品id
     * @param processId 工艺id
     * @return
     */
    default List<Plan> queryBatchListByProductIdAndProcessId(Long productId, Long processId) {
        return selectList(new LambdaQueryWrapperX<Plan>()
                .eq(productId != null, Plan::getProductId, productId)
                .eq(processId != null, Plan::getProcessId, processId)
                .eq(Plan::getInstructStatus, ProductPlanInstructStatusEnum.SEND)
                .eq(Plan::getStart, ProductPlanStartEnum.STARTING)
                .orderByAsc(Plan::getCreateTime)
        );
    }

    /**
     * 查询可以确认的(指令单全部确认)生产计划
     * @param productPlanIds
     * @return
     */
    List<Plan> selectConfirmableByIds(@Param("productPlanIds") Collection<Long> productPlanIds);

    /**
     * 根据工艺id和工艺版本查询最近n次的生产计划
     * @param processId
     * @param processVersion
     * @param n
     * @return
     */
    default List<Plan> getPlanListByProcedureId(Long processId, String processVersion, int n){
        return selectList(new LambdaQueryWrapperX<Plan>()
                .eq(Plan::getProcessId, processId)
                .eq(Plan::getProcessVersion, processVersion)
                .orderByDesc(Plan::getCreateTime)
                .last("limit " + n)
        );
    }

    default List<Plan> selectByProcessIdList(List<Long> processIdList, List<String> planStartEnumList){
        return selectList(new LambdaQueryWrapperX<Plan>()
                .in(Plan::getProcessId, processIdList)
                .in(Plan::getStart, planStartEnumList)
        );
    }

    default List<Plan> selectByProcessInfo(PlanListByProcessDTO dto){
        return selectList(new LambdaQueryWrapperX<Plan>()
                .eq(Plan::getProcessId, dto.getProcessId())
                .eq(StrUtil.isNotEmpty(dto.getProcessVersion()), Plan::getProcessVersion, dto.getProcessVersion()));
    }

    List<ProductionAuditProgressPageVO> selectProductionAuditProgressPage(ProductionAuditProgressQueryDTO dto);


    /**
     * 根据工艺id查询未非终止状态的生产批次信息
     * @param processIds 工艺id
     * @return
     */
    default List<Plan> selectByProcessIdsNotTermination(List<Long> processIds, LotReleasePlanPageQuery pageQuery) {
        if (CollectionUtil.isEmpty(processIds)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<Plan>()
                .in(Plan::getProcessId, processIds)
                .eq(Plan::getInstructStatus, ProductPlanInstructStatusEnum.SEND)
                .ne(Plan::getStart, ProductPlanStartEnum.TERMINATION)
                .eq(pageQuery.getProductId() != null, Plan::getProductId, pageQuery.getProductId())
                .like(StrUtil.isNotBlank(pageQuery.getProcessName()), Plan::getProcessName, pageQuery.getProcessName())
                .like(StrUtil.isNotBlank(pageQuery.getProcessName()), Plan::getProcessName, pageQuery.getProcessName())
                .like(StrUtil.isNotBlank(pageQuery.getBatchNo()), Plan::getBatchNo, pageQuery.getBatchNo())
                .between(StrUtil.isNotBlank(pageQuery.getStartDate()) && StrUtil.isNotBlank(pageQuery.getEndDate()),
                        Plan::getEndTime, pageQuery.getStartDate() + " 00:00:00", pageQuery.getEndDate() + " 23:59:59")
                .orderByDesc(StrUtil.isEmpty(pageQuery.getOrderBy()), Plan::getStartTime)
        );
    }

    default List<Plan> selectByProcessIdNotTermination(Long processId){
        if (processId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<Plan>()
                .eq(Plan::getProcessId, processId)
                .eq(Plan::getInstructStatus, ProductPlanInstructStatusEnum.SEND)
                // 未终止
                .ne(Plan::getStart, ProductPlanStartEnum.TERMINATION)
                // 未作为
                .ne(Plan::getStatus, ProductPlanStatusEnum.DISCARD)
                .orderByAsc(Plan::getCreateTime)
        );
    }
    default List<Plan> queryByProductionPlanIdS(List<Long> planItemIdS){
        return selectList(new LambdaQueryWrapperX<Plan>()
                .in(Plan::getProductionPlanItemId,planItemIdS)
        );
    }

    default List<Plan> selectByProductId(Long productId){
        return selectList(new LambdaQueryWrapperX<Plan>().eq(Plan::getProductId, productId));
    }

    default Plan getPlanByPoductionPlanItemId(Long productionPlanItemId){
        return selectOne(new LambdaQueryWrapperX<Plan>().eq(Plan::getProductionPlanItemId,productionPlanItemId));
    }

    default List<Plan> selectByProductionPlanItemId(List<Long> productionItemId){
        return selectList(new LambdaQueryWrapperX<Plan>().in(Plan::getProductionPlanItemId,productionItemId));
    }

    List<Plan> selectPlanListByProcedureVersionList(@Param("procedureVersionList") List<ProcessVersion> procedureVersionList, @Param("n") int n);

    default List<Plan> selectPlanListByProcedureVersionId(Long processId, Collection<String> processVersionList,int n){
        return selectList(new LambdaQueryWrapperX<Plan>()
                .eq(Plan::getProcessId, processId)
                .in(Plan::getProcessVersion, processVersionList)
                .orderByDesc(Plan::getStartTime)
                .last("limit " + n));
    }

    default List<Plan> selectByPlanNos(Collection<String> planNos){
        return selectList(new LambdaQueryWrapperX<Plan>().in(Plan::getPlanNo, planNos));
    }

    /**
     * 根据全批号匹配
     * 全批号: 工艺id-batchNo
     * @param currentBatchNos
     * @return
     */
    List<Plan> selectByFullBatchNos(@Param("fullBatchNos") Collection<String> currentBatchNos);

    List<PlanPageVO> getTodoPlanStart(@Param("todoType") String todoType,@Param("batchNo") String batchNo,
                                      @Param("processIds") List<Long> processIds,@Param("productIds") List<Long> productIds,
                                      @Param("lineIds") List<Long> lineIds);

    List<Long> productManagePageCount(@Param("dto") PlanPageCountDTO dto);

    default boolean planNoExists(String planNo) {
        return exists(new LambdaQueryWrapperX<Plan>()
                .eq(Plan::getPlanNo, planNo));
    }

    default boolean batchNoExists(Long processId, String batchNo) {
        return exists(new LambdaQueryWrapperX<Plan>().eq(Plan::getProcessId, processId)
                .eq(Plan::getBatchNo, batchNo));
    }
}
