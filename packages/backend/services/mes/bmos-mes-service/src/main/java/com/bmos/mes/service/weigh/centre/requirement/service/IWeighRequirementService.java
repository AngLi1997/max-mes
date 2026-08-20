package com.bmos.mes.service.weigh.centre.requirement.service;

import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.weigh.centre.requirement.dto.WeighRequirementPageQuery;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighRequirement;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementProgram;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.Collection;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 17:50
 */
public interface IWeighRequirementService {


    /**
     * 创建称量需求
     * @param productPlanId 生产计划id
     * @param componentInstances 物料投入组件列表
     */
    void createRequirement(Long productPlanId, List<BusinessComponentInstance> componentInstances);


    /**
     * 分页查询未规划的称量需求
     * @param pageQuery 分页查询参数
     * @return 分页查询结果
     */
    CommonPage<WeighRequirementVO> queryPage(WeighRequirementPageQuery pageQuery);

    /**
     * 查询自动规划的称量需求
     * @return 称量需求id列表
     */
    List<Long> listAutoProgramRequirements();

    /**
     * 根据需求id列表查询规划数据
     * @param requirementIds 需求id列表
     * @return
     */
    List<WeighRequirementProgram> selectRequirementProgramListByIds(List<Long> requirementIds);

    void updateBatch(Collection<WeighRequirement> requirements);

    List<WeighRequirement> selectByIds(Collection<Long> requirementIds);

    /**
     * 根据任务id 查询称量需求
     * @param taskId 查询参数
     * @return 查询结果
     */
    List<WeighRequirementVO> queryListByTaskId(Long taskId);

    /**
     * 查询物料、称量中心、单位详情相同的未规划的称量需求列表
     * @param materialId 物料id
     * @param unitId 配单位id
     * @param weighCentreId 称量中心id
     * @param deptIds 部门id列表
     * @param requirementStatus 规划状态
     * @return
     */
    List<WeighRequirementVO> queryUnPlanedRequirementList(Long materialId,
                                                          Long unitId,
                                                          Long weighCentreId,
                                                          List<Long> deptIds,
                                                          RequirementStatusEnum requirementStatus);

    /**
     * 释放需求
     * @param requirements 需求列表
     */
    void releaseRequirement(List<WeighRequirement> requirements);

    /**
     * 根据任务id查询需求列表
     * @param taskId 称量任务id
     * @return 该任务下的需求列表
     */
    List<WeighRequirement> selectListByTaskId(Long taskId);
}
