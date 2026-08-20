package com.bmos.mes.service.plan.team.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.plan.InstructionStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanInstructStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanInstructStatusEvent;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.info.convert.PlanConverter;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.ProductPlanRelationService;
import com.bmos.mes.service.plan.info.vo.PlanDetailVO;
import com.bmos.mes.service.plan.instruction.convert.InstructionConverter;
import com.bmos.mes.service.plan.instruction.mapper.InstructionMapper;
import com.bmos.mes.service.plan.instruction.model.Instruction;
import com.bmos.mes.service.plan.team.convert.InstructionTeamConverter;
import com.bmos.mes.service.plan.team.dto.*;
import com.bmos.mes.service.plan.team.mapper.InstructionTeamMapper;
import com.bmos.mes.service.plan.team.mapper.ProductPlanTeamMapper;
import com.bmos.mes.service.plan.team.mapper.TeamProductionLineMapper;
import com.bmos.mes.service.plan.team.model.InstructionTeam;
import com.bmos.mes.service.plan.team.model.TeamProductionLine;
import com.bmos.mes.service.plan.team.service.InstructionTeamService;
import com.bmos.mes.service.plan.team.vo.InstructionTeamDetailVO;
import com.bmos.mes.service.plan.team.vo.ProcedureStepChangeVO;
import com.bmos.mes.service.process.convert.ProcessConfirmConverter;
import com.bmos.mes.service.process.dto.ProcedureStepGroupUserDTO;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcessConfirmService;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.condition.event.TaskInitType;
import com.bmos.mes.service.process.service.task.ProcedureConditionInstanceService;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceService;
import com.bmos.mes.service.process.vo.ProcedureModelDetailVO;
import com.bmos.mes.service.process.vo.ProcedureStepModelVO;
import com.bmos.mes.service.utils.QueryProcessConfigSortUtils;
import com.bmos.mes.service.workflow.change.mapper.ProductChangeTeamMapper;
import com.bmos.mes.service.workflow.change.model.ProductChangeTeam;
import com.bmos.mes.service.workflow.change.vo.TeamListVO;
import com.bmos.mes.service.workflow.service.WorkflowExecutor;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.unit.service.UnitCache;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InstructionTeamServiceImpl implements InstructionTeamService {
    @Autowired
    private InstructionTeamMapper instructionTeamMapper;
    @Autowired
    private ProductPlanTeamMapper productPlanTeamMapper;
    @Autowired
    private InstructionMapper instructionMapper;
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    @Lazy
    private WorkflowExecutor workflowExecutor;
    @Autowired
    private ProductPlanRelationService productPlanRelationService;
    @Autowired
    private ProcessConfirmService confirmService;
    @Autowired
    private ProcedureTaskInstanceService taskInstanceService;
    @Autowired
    private ITaskConditionCalculator taskConditionCalculator;
    @Autowired
    private ProcedureConditionInstanceService conditionInstanceService;
    @Autowired
    private ProcedureModelService procedureModelService;
    @Autowired
    private TeamProductionLineMapper teamProductionLineMapper;
    @Resource
    private ProductChangeTeamMapper changeTeamMapper;

    @Resource
    private UnitCache unitCache;

    @Override
    public InstructionTeamDetailVO detail(Long id) {
        // 查询指令单信息
        //todo 添加排序
        Instruction instruction = instructionMapper.selectById(id);
        PlanDetailVO planDetailVO = PlanConverter.INSTANCE.convertVO(planMapper.selectById(instruction.getProductPlanId()));
        planDetailVO.setUnitName(unitCache.getGlobalUnitName(planDetailVO.getUnitId()));
        InstructionTeamDetailVO result = InstructionTeamDetailVO.builder()
                .planDetailVO(planDetailVO)
                .instructionVO(InstructionConverter.INSTANCE.convertVO(instruction))
                .build();
        List<InstructionTeam> teams = instructionTeamMapper.selectByInstructionId(id);
        // 未保存班组配置返回工序默认配置
        if (CollUtil.isEmpty(teams)) {
            ProcedureModelDetailVO detail = procedureModelService.getDetail(instruction.getProcedureModelId());
            teams = buildInstructionTeams(detail, instruction);
        }
        List<Long> stepModelId = CollectionUtils.convertList(teams, InstructionTeam::getProcedureStepModelId);
        Map<Long, Integer> stepModelSortMap = QueryProcessConfigSortUtils.queryProcedureStepModelSortByIdList(stepModelId);
        teams.forEach(item->item.setSort(stepModelSortMap.get(item.getProcedureStepModelId())));
        result.getInstructionVO().setTeams(InstructionTeamConverter.INSTANCE.convertList(
                teams.stream().sorted(Comparator.comparing(InstructionTeam::getSort))
                        .collect(Collectors.toList())));
        return result;
    }

    private List<InstructionTeam> buildInstructionTeams(ProcedureModelDetailVO detail, Instruction instruction) {
        return detail.getSteps().stream().map(item -> {
                    InstructionTeam instructionTeam = new InstructionTeam();
                    instructionTeam.setInstructionId(instruction.getId());
                    instructionTeam.setProductPlanId(instruction.getProductPlanId());
                    instructionTeam.setNodeId(item.getNodeId());
                    instructionTeam.setProcedureId(detail.getProcedureId());
                    instructionTeam.setProcedureModelId(item.getProcedureModelId());
                    instructionTeam.setNodeStepId(item.getNodeId());
                    instructionTeam.setProcedureStepId(item.getProcedureStepId());
                    instructionTeam.setProcedureStepModelId(item.getId());
                    instructionTeam.setProcedureStepModelName(item.getName());
                    instructionTeam.setProcedureStepTime(item.getDuration());
                    instructionTeam.setProcedureStepTimeUnit(item.getTimeUnit());
                    instructionTeam.setTeamIds(CollUtil.isEmpty(item.getGroupIds()) ? detail.getGroupIds() : item.getGroupIds());
                    return instructionTeam;
                }
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(InstructionTeamConfirmDTO dto) {
        save(dto);
        // 指令单状态确认
        instructionMapper.confirm(dto.getInstructionId(), SysUserHolder.getUser().getUserId());
        // 如果不存在已分解状态的指令单 生产计划不修改状态
        if (!instructionMapper.existsResolve(dto.getProductPlanId())) {
            planMapper.updateInstructStatus(dto.getProductPlanId(),
                    ProductPlanInstructStatusEnum.getNextStatus(
                            ProductPlanInstructStatusEnum.WAIT_CONFIRM,
                            ProductPlanInstructStatusEvent.CONFIRM)
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(InstructionTeamConfirmDTO dto) {
        Instruction instruction = instructionMapper.selectById(dto.getInstructionId());
        if (Objects.isNull(instruction)) {
            throw new BmosException(MesResponseCode.INSTRUCTION_NOT_EXISTS);
        }
        if (InstructionStatusEnum.RESOLVE != instruction.getStatus()) {
            throw new BmosException(MesResponseCode.INSTRUCTION_RESOLVE_CAN_CONFIRM);
        }
        instructionTeamMapper.deleteByInstructionId(dto.getInstructionId());
        // 删除以前存储的，保留当下存储的
        instructionTeamMapper.insertBatch(InstructionTeamConverter.INSTANCE.convertList(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startConfirm(InstructionTeamProductStartConfirmDTO dto) {
        Plan plan = planMapper.selectById(dto.getPlanId());
        if (ProductPlanInstructStatusEnum.SEND != plan.getInstructStatus()) {
            throw new BmosException(MesResponseCode.SEND_INTRUCTION_CAN_CONFIRM);
        }
        if (ProductPlanStartEnum.WAIT != plan.getStart()) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_CAN_NOT_CONFIRM);
        }
        instructionTeamMapper.deleteByProductPlanId(dto.getPlanId());
        instructionTeamMapper.insertBatch(
                dto.getTeamConfirmDTO().stream()
                        .map(InstructionTeamConverter.INSTANCE::convertList)
                        .flatMap(List::stream)
                        .collect(Collectors.toList())
        );
        // 发起流程
        Plan needUpdatePlan = plan.toBuilder()
                .executeProcessInstanceId(workflowExecutor.startWorkflow(PlanConverter.INSTANCE.convertVO2(plan)))
                .start(ProductPlanStartEnum.STARTING)
                .startTime(LocalDateTime.now())
                .build();
        planMapper.updateById(needUpdatePlan);
        // 生产计划批量更新状态
        planMapper.relation(
                dto.getRelationPlan()
                        .stream()
                        .map(InstructionTeamProductStartConfirmDetailDTO::getPlanIds)
                        .flatMap(List::stream)
                        .distinct()
                        .collect(Collectors.toList()));
        // 保存生产计划关联数据
        productPlanRelationService.save(dto);
        //保存生产前确定的审核结论数据
        confirmService.saveProcessConfirm(ProcessConfirmConverter.INSTANCE.convertToDto(needUpdatePlan));
        // 初始化步骤的条件实例
        conditionInstanceService.initConditionInstance(needUpdatePlan);
        //初始化任务
        taskInstanceService.initTaskInstance(needUpdatePlan);
        // 计算任务条件
        taskConditionCalculator.refreshConditionResult(new TaskInitType(needUpdatePlan.getId()));
    }

    @Override
    public List<Long> getTeamIds(Long productPlanId, String nodeStepId) {
        return instructionTeamMapper.selectTeamIds(productPlanId, nodeStepId);
    }

    @Override
    public List<String> findInstructionPeople(ProcedureStepGroupUserDTO dto) {
        InstructionTeam instructionTeam = instructionTeamMapper.selectListByPlanIdAndNodeStepId(dto.getProductPlanId(), dto.getNodeId());
        List<Long> teamIds = instructionTeam.getTeamIds();
        if (ProcedureStepNodeFunctionEnum.changeTeamFlag(dto.getNodeFunction()) &&
                (dto.getProcessChangeNumber() != 0 || dto.getProcedureChangeNumber() != 0)){
            Integer changeNumber = dto.getNodeFunction().equals(ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue()) ?
                    dto.getProcedureChangeNumber() : dto.getProcessChangeNumber();
            ProductChangeTeam team = changeTeamMapper.selectOneByChangeNumberAndChangeType(instructionTeam.getId(), changeNumber, dto.getNodeFunction());
            teamIds = ObjectUtil.isEmpty(team) || CollUtil.isEmpty(team.getTeamIds()) ? null : team.getTeamIds();
        }
        return productPlanTeamMapper.selectTeamPeople(teamIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchConfirm(InstructionBatchConfirmDTO dto) {
        List<Long> instructionIds = new ArrayList<>();
        Set<Long> productPlanIds = new HashSet<>();
        for (InstructionConfirmDTO instructionConfirmDTO : dto.getInstructionInfoList()) {
            instructionIds.add(instructionConfirmDTO.getInstructionId());
            productPlanIds.add(instructionConfirmDTO.getProductPlanId());
        }
        // 处理指令单班组信息
        handleInstructionTeam(instructionIds, productPlanIds);
        // 指令单状态批量确认
        instructionMapper.batchConfirm(instructionIds, SysUserHolder.getUser().getUserId());
        // 生产计划状态修改
        // 查询可确认的生产计划
        List<Plan> confirmablePlans = planMapper.selectConfirmableByIds(productPlanIds);
        confirmablePlans.forEach(e->{
            e.setInstructStatus(ProductPlanInstructStatusEnum.WAIT_SEND);
        });
        // 批量修改
        if (CollUtil.isNotEmpty(confirmablePlans)) {
            planMapper.updateBatch(confirmablePlans);
        }
    }

    @Override
    public List<Long> getChangeTeamIds(Integer procedureNumber, Long planId, String nodeStepId,Map<Long,List<Long>> changeTeamId,
                                       String changeType) {
        InstructionTeam instructionTeam = instructionTeamMapper.selectListByPlanIdAndNodeStepId(planId, nodeStepId);
        List<Long> teamIds = changeTeamMapper.queryByInstructionIdAndChangeType(instructionTeam.getId(), procedureNumber,changeType);
        if (CollUtil.isEmpty(teamIds)){
            return changeTeamId.get(instructionTeam.getId());
        }
        return teamIds;
    }

    @Override
    public List<InstructionTeam> queryByPlanId(List<Long> planId) {
        //班组信息查询
        return instructionTeamMapper.getInstructionDetailByUserTeamId(planId);
    }

    @Override
    public List<TeamListVO> getHistoryChangeTeam(Long planId,List<Long> teamIdS) {
        List<InstructionTeam> instructionTeams = instructionTeamMapper.selectListByPlanIdS(Collections.singletonList(planId));
        List<TeamListVO> teamList = new ArrayList<>();
        //先判断换班前用户拥有的班组信息
        instructionTeams.forEach(item->{
            //当前登录用户班组在班组集合当中
            if (CollUtil.isNotEmpty(CollectionUtils.filterList(item.getTeamIds(), teamIdS::contains))) {
                TeamListVO vo = BeanUtil.toBean(item, TeamListVO.class);
                vo.setChangeTeamNumber(0);
                vo.setChangeTeamType(ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue());
                teamList.add(vo);
            }
        });
        //是否存在换班获取最新班组信息，工序换班优先
        List<Long> instructionId = CollectionUtils.convertList(instructionTeams, InstructionTeam::getId);
        Map<Long, InstructionTeam> instructionTeamMap = CollectionUtils.convertMap(instructionTeams, InstructionTeam::getId);
        List<ProductChangeTeam> productChangeTeams = changeTeamMapper.selectListByInstructionId(instructionId);
        productChangeTeams.forEach(changeTeam->{
            //当前登录用户班组在班组集合当中
            if (CollUtil.isNotEmpty(CollectionUtils.filterList(changeTeam.getTeamIds(), teamIdS::contains))) {
                InstructionTeam instructionTeam = instructionTeamMap.get(changeTeam.getProductInstructionTeamId());
                if (ObjectUtil.isNotEmpty(instructionTeam)) {
                    TeamListVO vo = new TeamListVO();
                    vo.setNodeId(instructionTeam.getNodeId());
                    vo.setNodeStepId(instructionTeam.getNodeStepId());
                    vo.setProcedureModeId(instructionTeam.getProcedureModelId());
                    vo.setProcedureStepModelId(instructionTeam.getProcedureStepModelId());
                    vo.setTeamIdS(changeTeam.getTeamIds());
                    vo.setChangeTeamNumber(changeTeam.getChangeTeamNumber());
                    vo.setChangeTeamType(changeTeam.getChangeTeamType());
                    teamList.add(vo);
                }
            }
        });
        return teamList;
    }

    @Override
    public List<ProcedureStepChangeVO> queryByPlanIdAndStepIds(Long productPlanId, List<Long> stepIds) {
        if (CollUtil.isEmpty(stepIds)) {
            return new ArrayList<>();
        }
        List<InstructionTeam> instructionTeams = instructionTeamMapper.selectByPlanIdAndStepIds(productPlanId, stepIds);
        List<Long> instructionTeamIds = CollectionUtils.convertList(instructionTeams, InstructionTeam::getId);
        List<ProductChangeTeam> productChangeTeams = changeTeamMapper.selectListByInstructionId(instructionTeamIds);
        Map<Long, List<ProductChangeTeam>> changeTeamMap = CollectionUtils.convertMultiMap(productChangeTeams, ProductChangeTeam::getProductInstructionTeamId);
        return handleChangeTeamList(instructionTeams, changeTeamMap);
    }

    @Override
    public List<InstructionTeam> getInstructionDetailByUserTeamId(List<Long> team) {
        List<InstructionTeam> teamList = instructionTeamMapper.getInstructionDetailByUserTeamId(Collections.emptyList());
        Map<Long, List<InstructionTeam>> teamMap = CollectionUtils.convertMultiMap(teamList, InstructionTeam::getId);
        //先判断换班前用户拥有的班组信息
        List<InstructionTeam> list = new ArrayList<>();
        teamMap.forEach((key,value)->{
            for (InstructionTeam item : value) {
                //当前登录用户班组在班组集合当中
                if (CollUtil.isNotEmpty(CollectionUtils.filterList(item.getTeamIds(), team::contains))) {
                    list.add(item);
                    return;
                }
            }
        });
        return list;
    }

    private List<ProcedureStepChangeVO> handleChangeTeamList(List<InstructionTeam> instructionTeams, Map<Long, List<ProductChangeTeam>> changeTeamMap) {
        List<ProcedureStepChangeVO> result = new ArrayList<>();
        for (InstructionTeam instructionTeam : instructionTeams) {
            ProcedureStepChangeVO current = new ProcedureStepChangeVO();
            int processStart  = 0;
            int procedureStart = 0;
            current.setProcedureStepId(instructionTeam.getProcedureStepId());
            current.setProcessChangeNumber(processStart);
            current.setProcedureChangeNumber(procedureStart);
            result.add(current);
            List<ProductChangeTeam> list = changeTeamMap.get(instructionTeam.getId());
            if (CollUtil.isEmpty(list)) {
                continue;
            }
            list.sort(Comparator.comparing(BaseDO::getCreateTime));
            for (ProductChangeTeam change : list) {
                ProcedureStepChangeVO vo = new ProcedureStepChangeVO();
                vo.setProcedureStepId(instructionTeam.getProcedureStepId());
                if (Objects.equals(change.getChangeTeamType(), ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue())) {
                    processStart++;
                    procedureStart = 0;
                } else {
                    procedureStart++;
                }
                vo.setProcessChangeNumber(processStart);
                vo.setProcedureChangeNumber(procedureStart);
                result.add(vo);
            }
        }
        return result;
    }

    private void handleInstructionTeam(List<Long> instructionIds, Set<Long> productPlanIds) {
        List<InstructionTeam> instructionTeams = instructionTeamMapper.selectByInstructionIds(instructionIds);
        List<Plan> plans = planMapper.selectBatchIds(productPlanIds);
        List<Long> productionLineIds = CollectionUtils.convertList(plans, Plan::getProductionLineId);
        Map<Long, Long> planLineMap = CollectionUtils.convertMap(plans, Plan::getId, Plan::getProductionLineId);
        // 查询出产线班组关系进行过滤
        List<TeamProductionLine> teamProductionLines = teamProductionLineMapper.selectByProductionLineIds(productionLineIds);
        Map<Long, List<TeamProductionLine>> teamMap = CollectionUtils.convertMultiMap(teamProductionLines, TeamProductionLine::getProductionLineId);
        List<Instruction> instructions = instructionMapper.selectBatchIds(instructionIds);
        Map<Long, Instruction> instructionMap = CollectionUtils.convertMap(instructions, Instruction::getId);
        Map<Long, List<InstructionTeam>> instructionTeamMap = CollectionUtils.convertMultiMap(instructionTeams, InstructionTeam::getInstructionId);
        for (Long instructionId : instructionIds) {
            List<InstructionTeam> teams = instructionTeamMap.get(instructionId);
            if (CollUtil.isEmpty(teams)) {
                Instruction instruction = instructionMap.get(instructionId);
                Long lineId = planLineMap.get(instruction.getProductPlanId());
                List<TeamProductionLine> currentPlanTeams = teamMap.get(lineId);
                InstructionTeamConfirmDTO teamConfirmDTO = getInstructionTeamConfirmDTO(instruction, currentPlanTeams);
                save(teamConfirmDTO);
            }
        }

    }

    @NotNull
    private InstructionTeamConfirmDTO getInstructionTeamConfirmDTO(Instruction instruction,
                                                                   List<TeamProductionLine> teamProductionLineList) {
        if (CollUtil.isEmpty(teamProductionLineList)) {
            throw new BmosException(MesResponseCode.INSTRUCTION_TEAM_CANT_BE_EMPTY);
        }
        ProcedureModelDetailVO detail = procedureModelService.getDetail(instruction.getProcedureModelId());
        InstructionTeamConfirmDTO teamConfirmDTO = new InstructionTeamConfirmDTO();
        teamConfirmDTO.setInstructionId(instruction.getId());
        teamConfirmDTO.setProductPlanId(instruction.getProductPlanId());
        teamConfirmDTO.setNodeId(instruction.getNodeId());
        teamConfirmDTO.setProcedureId(detail.getProcedureId());
        teamConfirmDTO.setProcedureModelId(detail.getId());
        List<ProcedureStepModelVO> steps = detail.getSteps();
        List<Long> teamIds = CollectionUtils.convertList(teamProductionLineList, TeamProductionLine::getTeamId);
        detail.getGroupIds().removeIf(e -> !teamIds.contains(e));
        int count = 1;
        List<InstructionTeamConfirmDetailDTO> stepConfirms = new ArrayList<>();
        for (ProcedureStepModelVO e : steps) {
            InstructionTeamConfirmDetailDTO stepConfirm = new InstructionTeamConfirmDetailDTO();
            stepConfirm.setNodeStepId(e.getNodeId());
            stepConfirm.setProcedureStepId(e.getProcedureStepId());
            stepConfirm.setProcedureStepModelId(e.getId());
            stepConfirm.setProcedureStepTime(e.getDuration());
            stepConfirm.setProcedureStepTimeUnit(e.getTimeUnit());
            // 工步无配置则使用工序经过产线过滤后的班组
            // 工步有配置则使用工步经过产线过滤后的班组
            List<Long> tempList = CollUtil.isEmpty(e.getGroupIds()) ? detail.getGroupIds() :
                    e.getGroupIds().stream().filter(teamIds::contains).collect(Collectors.toList());
            if (CollUtil.isEmpty(tempList)) {
                throw new BmosException(MesResponseCode.INSTRUCTION_TEAM_CANT_BE_EMPTY);
            }
            stepConfirm.setTeamIds(tempList);
            stepConfirm.setProcedureStepModelName(e.getName());
            stepConfirm.setSort(count++);
            stepConfirms.add(stepConfirm);
        }
        teamConfirmDTO.setDetails(stepConfirms);
        return teamConfirmDTO;
    }
}
