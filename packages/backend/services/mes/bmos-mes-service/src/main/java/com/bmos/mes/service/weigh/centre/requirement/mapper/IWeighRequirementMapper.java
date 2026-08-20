package com.bmos.mes.service.weigh.centre.requirement.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighProcess;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.weigh.centre.execute.vo.WeighExecuteRequirementDetailVO;
import com.bmos.mes.service.weigh.centre.execute.vo.WeighExecuteRequirementVO;
import com.bmos.mes.service.weigh.centre.requirement.dto.WeighRequirementPageQuery;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighRequirement;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementProgram;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 17:34
 */
@Mapper
public interface IWeighRequirementMapper extends BaseMapperX<WeighRequirement> {

    /**
     * 分页查询需求列表
     *
     * @param pageQuery 参数
     * @param deptIds   部门
     * @return
     */
    List<WeighRequirementVO> queryList(@Param("pageQuery") WeighRequirementPageQuery pageQuery, @Param("deptIds") List<Long> deptIds);

    default boolean existProductPlanId(Long productPlanId) {
        if (productPlanId == null) {
            return false;
        }
        return exists(new LambdaQueryWrapperX<WeighRequirement>()
                .eq(WeighRequirement::getProductPlanId, productPlanId)
        );
    }

    /**
     * 根据称量中心id查询称量需求列表
     *
     * @param weighCentreId 称量中心id
     * @return
     */
    default List<WeighRequirement> listByWeighCentreId(Long weighCentreId) {
        if (weighCentreId == null) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<WeighRequirement>()
                .eq(WeighRequirement::getWeighCentreId, weighCentreId)
        );
    }

    /**
     * 根据需求id查询待规划的需求列表
     *
     * @param requirementIds
     * @return
     */
    List<WeighRequirementProgram> selectRequirementProgramListByIds(@Param("requirementIds") List<Long> requirementIds);

    /**
     * 根据任务id分页查询需求列表
     *
     * @param taskId 任务id
     * @return
     */
    List<WeighRequirementVO> queryListByTaskId(@Param("taskId") Long taskId);

    /**
     * 查询物料、称量中心、单位详情相同的未规划的称量需求列表
     *
     * @param materialId        物料id
     * @param unitId            配单位id
     * @param weighCentreId     称量中心id
     * @param deptIds           部门id列表
     * @param requirementStatus 规划状态
     * @return
     */
    List<WeighRequirementVO> queryUnPlanedRequirementList(@Param("materialId") Long materialId,
                                                          @Param("unitId") Long unitId,
                                                          @Param("weighCentreId") Long weighCentreId,
                                                          @Param("deptIds") List<Long> deptIds,
                                                          @Param("requirementStatus") RequirementStatusEnum requirementStatus);

    /**
     * 释放需求
     *
     * @param requirements
     */
    default void releaseRequirement(List<WeighRequirement> requirements) {
        if (CollectionUtil.isEmpty(requirements)) {
            return;
        }
        update(null, new LambdaUpdateWrapper<WeighRequirement>()
                .set(WeighRequirement::getWeighRequirementTaskId, null)
                .set(WeighRequirement::getProgramTime, null)
                .set(WeighRequirement::getRequirementStatus, RequirementStatusEnum.UN_PLANNED)
                .in(WeighRequirement::getId, requirements.stream().map(WeighRequirement::getId).collect(Collectors.toList()))
        );
    }

    /**
     * 根据任务id查询需求列表
     *
     * @param taskId 任务id
     * @return 需求列表
     */
    default List<WeighRequirement> selectListByTaskId(Long taskId) {
        if (taskId == null) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<WeighRequirement>()
                .eq(WeighRequirement::getWeighRequirementTaskId, taskId)
        );
    }

    /**
     * 根据需求id查询需求详情
     *
     * @param requirementId
     * @return
     */
    WeighExecuteRequirementDetailVO selectWeighExecuteRequirementDetailById(@Param("requirementId") Long requirementId);

    /**
     * 清除物料需求当前正在称量的批次信息
     *
     * @param requirementId 需求id
     */
    default void clearBatch(Long requirementId, RequirementWeighProcess process) {
        if (requirementId == null) {
            return;
        }
        update(null, new LambdaUpdateWrapper<WeighRequirement>()
                .set(WeighRequirement::getStorageMaterialBatchId, null)
                .set(WeighRequirement::getWeighProcess, process)
                .eq(WeighRequirement::getId, requirementId)
        );
    }

    /**
     * 根据任务id查询需求列表
     *
     * @param taskId 任务id
     * @return
     */
    List<WeighExecuteRequirementVO> selectExecuteRequirementListByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据任务id列表查询需求列表
     *
     * @param taskIds 任务id列表
     * @return
     */
    default List<WeighRequirement> selectListByTaskIds(Collection<Long> taskIds) {
        if (CollectionUtil.isEmpty(taskIds)) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<WeighRequirement>()
                .in(WeighRequirement::getWeighRequirementTaskId, taskIds)
        );
    }

    /**
     * 查询过期日期之前的未规划且未开始的需求列表
     * @param date
     * @return
     */
    default List<WeighRequirement> selectExpiredRequirement(LocalDate date) {
        List<RequirementStatusEnum> status = new ArrayList<>();
        // 未规划
        status.add(RequirementStatusEnum.UN_PLANNED);
        // 未开始
        status.add(RequirementStatusEnum.UN_WEIGHED);
        return selectList(new LambdaQueryWrapperX<WeighRequirement>()
                .in(WeighRequirement::getRequirementStatus, status)
                .lt(WeighRequirement::getExpiredDate, date)
        );
    }

    default List<WeighRequirement> selectListByProcedureStepConfigId(Long procedureStepConfigId) {
        if (procedureStepConfigId == null) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<WeighRequirement>()
                .eq(WeighRequirement::getProcedureStepConfigId, procedureStepConfigId)
        );
    }

    default List<Long> selectRequirementIdListByProcedureStepConfigId(Long procedureStepConfigId) {
        if (procedureStepConfigId == null) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<WeighRequirement>()
                .eq(WeighRequirement::getProcedureStepConfigId, procedureStepConfigId)
        ).stream().map(WeighRequirement::getId).collect(Collectors.toList());
    }

    default List<WeighRequirement> selectListByComponentInstanceId(BusinessComponentInstance componentInstance){
        if (componentInstance == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<WeighRequirement>()
                .eq(WeighRequirement::getBusinessComponentInstanceId, componentInstance.getId())
        );
    }
}
