package com.bmos.mes.service.plan.instruction.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.enums.plan.InstructionStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanInstructStatusEnum;
import com.bmos.mes.common.enums.plan.ProductPlanInstructStatusEvent;
import com.bmos.mes.common.enums.plan.ProductPlanStatusEnum;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.components.BusinessComponentManager;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.plan.info.convert.PlanConverter;
import com.bmos.mes.service.plan.info.dto.PlanPageDTO;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.vo.PlanPageVO;
import com.bmos.mes.service.plan.instruction.convert.InstructionConverter;
import com.bmos.mes.service.plan.instruction.dto.InstructionSaveDTO;
import com.bmos.mes.service.plan.instruction.dto.InstructionUpdateDTO;
import com.bmos.mes.service.plan.instruction.dto.TeamDetailQueryDTO;
import com.bmos.mes.service.plan.instruction.mapper.InstructionMapper;
import com.bmos.mes.service.plan.instruction.model.Instruction;
import com.bmos.mes.service.plan.instruction.service.InstructionService;
import com.bmos.mes.service.plan.instruction.vo.*;
import com.bmos.mes.service.plan.team.convert.InstructionTeamConverter;
import com.bmos.mes.service.plan.team.mapper.InstructionTeamMapper;
import com.bmos.mes.service.plan.team.model.InstructionTeam;
import com.bmos.mes.service.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.mes.service.process.mapper.ProcedureStepRoleRelationMapper;
import com.bmos.mes.service.process.mapper.ProcessProductionLineMapper;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.ProcedureStepRole;
import com.bmos.mes.service.process.model.ProcessProductionLine;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.process.vo.ProcedureModelDetailVO;
import com.bmos.mes.service.utils.QueryProcessConfigSortUtils;
import com.bmos.mes.service.weigh.centre.requirement.service.IWeighRequirementService;
import com.bmos.mes.service.workflow.change.service.ProductChangeTeamService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InstructionServiceImpl implements InstructionService {

    @Value("${parameter.record.schedule-confirm}")
    private String recordScheduleConfirm;

    @Autowired
    private InstructionMapper instructionMapper;
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private InstructionTeamMapper instructionTeamMapper;
    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;
    @Autowired
    @Lazy
    private ProcedureModelService procedureModelService;
    @Autowired
    @Lazy
    private ProcessService processService;

    @Resource
    @Lazy
    private IWeighRequirementService weighRequirementService;

    @Resource
    private BusinessComponentManager componentManager;

    @Resource
    private ProductChangeTeamService productChangeTeamService;

    @Resource
    private ProcedureStepRoleRelationMapper roleRelationMapper;

    @Autowired
    private PlatformParameterClientImpl platformParameterClientImpl;

    @Resource
    private ProcedureStepModelService stepModelService;

    @Resource
    private ProcessProductionLineMapper lineMapper;

    @Override
    public List<InstructionPageVO> page(PlanPageDTO dto) {
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            // 指令单分解到角色 通过角色查询确认列表
            List<Long> roleIds = platformApiAdaptor.roleIds();
            dto.setPrincipalRoleIds(roleIds);
            // 工艺权限
            List<Long> processIdList = processService.getIdListByDeptIds();
            if(CollUtil.isEmpty(processIdList)){
                return Collections.emptyList();
            }
            dto.setProcessIds(processIdList);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        return instructionMapper.page(dto);
    }

    @Override
    public Integer waitTaskCount(String userId) {
        List<Long> principalIds = new ArrayList<>();
        List<Long> processIds = new ArrayList<>();
        if (!AdminUtil.isAdminUser(userId)){
            List<Long> roleIds = platformApiAdaptor.roleIds();
            // 工艺权限
            List<Long> processIdList = processService.getIdListByDeptIds();
            if (CollUtil.isEmpty(roleIds) || CollUtil.isEmpty(processIdList)){
                return 0;
            }
            principalIds.addAll(roleIds);
            processIds.addAll(processIdList);
        }
        return instructionMapper.waitTaskCount(principalIds,processIds);
    }

    @Override
    public List<PlanPageVO> startPage(PlanPageDTO dto) {
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())){
            List<Long> processIdList = processService.getIdListByDeptIds();
            if (CollUtil.isEmpty(processIdList)){
                return Collections.emptyList();
            }
            dto.setProcessIds(processIdList);
        }
        return instructionMapper.startPage(dto);
    }

    @Override
    public InstructionDetailVO detail(Long id) {
        // 查询指令单信息
        List<Instruction> instructions = instructionMapper.selectByProductPlanId(id);
        List<Long> procedureModelId = CollectionUtils.convertList(instructions, Instruction::getProcedureModelId);
        Map<Long, Integer> procedureModelMap = QueryProcessConfigSortUtils.queryProcedureModelSortByIdList(procedureModelId);
        instructions.forEach(item-> item.setSort(procedureModelMap.get(item.getProcedureModelId())));
        List<InstructionVO> instructionVOS = InstructionConverter.INSTANCE.convertVOs(instructions);
        instructionVOS.sort(Comparator.comparingInt(InstructionVO::getSort));
        // 查询以过滤的指令单拼接班组信息
        fillInstructionTeam(instructionVOS);
        return InstructionDetailVO.builder()
            .planDetailVO(PlanConverter.INSTANCE.convertVO(planMapper.selectById(id)))
            .instructions(instructionVOS)
            .build();
    }

    private void fillInstructionTeam(List<InstructionVO> instructionVOS) {
        List<InstructionTeam> teamList = instructionTeamMapper.selectByInstructionIds(
                instructionVOS.stream()
                        .filter(vo -> vo.getStatus() == InstructionStatusEnum.CONFIRM)
                        .map(InstructionVO::getId)
                        .collect(Collectors.toList()));
        if (CollUtil.isEmpty(teamList)){
            return;
        }
        List<Long> stepModelIdList = CollectionUtils.convertList(teamList, InstructionTeam::getProcedureStepModelId);
        Map<Long, Integer> stepModelSortMap = QueryProcessConfigSortUtils.queryProcedureStepModelSortByIdList(stepModelIdList);
        teamList.forEach(item-> item.setSort(stepModelSortMap.get(item.getProcedureStepModelId())));
        Map<Long, List<InstructionTeam>> instructionIdTeamMap = CollectionUtils.convertMultiMap(teamList, InstructionTeam::getInstructionId);
        instructionVOS.forEach(vo -> {
            List<InstructionTeam> teams = instructionIdTeamMap.getOrDefault(vo.getId(), Collections.emptyList());
            teams.sort(Comparator.comparingInt(InstructionTeam::getSort));
            vo.setTeams(InstructionTeamConverter.INSTANCE.convertList(teams));
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(InstructionSaveDTO dto) {
        saveAndUpdateValidated(dto.getProductPlanId());
        // 指令单保存
        Instruction instruction = instructionMapper.selectByCondition(dto.getNodeId(), dto.getProductPlanId());
        if (Objects.nonNull(instruction)) {
            throw new BmosException(MesResponseCode.INSTRUCTION_RESOLVED);
        }
        instruction =  InstructionConverter.INSTANCE.convertDO(dto);
        instructionMapper.insert(instruction);
        return instruction.getId();
    }

    private void saveAndUpdateValidated(Long productPlanId) {
        Plan plan = planMapper.selectById(productPlanId);
        if (Objects.isNull(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        if (ProductPlanInstructStatusEnum.WAIT_DECOMPOSE != plan.getInstructStatus()) {
            throw new BmosException(MesResponseCode.WAIT_DECOMPOSE_CAN_OPERATOR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(InstructionUpdateDTO dto) {
        saveAndUpdateValidated(dto.getProductPlanId());
        instructionMapper.resolve(dto.getId(), dto.getPrincipal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generate(Long id, boolean autoConfirm) {
        Plan plan = planMapper.selectById(id);
        if (Objects.isNull(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        if (!(ProductPlanStatusEnum.CONFIRM == plan.getStatus()
            && ProductPlanInstructStatusEnum.WAIT_DECOMPOSE == plan.getInstructStatus())) {
            throw new BmosException(MesResponseCode.CONFIRM_AND_WAIT_DECOMPOSE);
        }
        String processVersion = plan.getProcessVersion();
        Long processId = plan.getProcessId();
        // 自动分解指令单
        List<ProcedureModel> procedureModels = procedureModelService.getByProcessIdAndVersion(processId, processVersion);
        List<Instruction> collect = procedureModels.stream().map(e -> {
            Instruction instruction = new Instruction();
            instruction.setId(IdUtils.getSnowflake());
            instruction.setStatus(InstructionStatusEnum.RESOLVE);
            instruction.setPrincipal(e.getPrincipal());
            instruction.setProductPlanId(id);
            instruction.setNodeId(e.getNodeId());
            instruction.setProcedureId(e.getProcedureId());
            instruction.setProcedureModelId(e.getId());
            instruction.setProcedureModelName(e.getName());
            instruction.setProcedureModelCode(e.getStageCode());
            // todo 搞清楚sort干嘛的
            instruction.setSort(0);
            return instruction;
        }).collect(Collectors.toList());
        //自动分解指令单参数配置
        String valueByCode = platformParameterClientImpl.getValueByCode(recordScheduleConfirm);
        boolean scheduleConfirm = BooleanUtil.toBoolean(valueByCode);
        scheduleConfirm = scheduleConfirm && !autoConfirm;
        //当参数配置为false时指令单生成需要自动确认
        if (!scheduleConfirm){
            confirmSaveTeam(procedureModels,collect,plan);
        }
        instructionMapper.insertBatch(collect);
        //自动确定需走到待下发
        planMapper.updateInstructStatus(id,
                scheduleConfirm ?
                        ProductPlanInstructStatusEnum.getNextStatus(plan.getInstructStatus(), ProductPlanInstructStatusEvent.WAIT_CONFIRM) :
                        ProductPlanInstructStatusEnum.WAIT_SEND
        );
    }

    /**
     * 当为自动确定时构建参数保存班组信息
     * @param procedureModels 工序模型
     * @param collect 生产指令单
     * @param plan 计划信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmSaveTeam(List<ProcedureModel> procedureModels,List<Instruction> collect,Plan plan){
        //根据工序id查询工序步骤
        List<Long> procedureModelIdS = CollectionUtils.convertList(procedureModels, ProcedureModel::getId);
        List<ProcedureModelDetailVO> procedureModelDetail = procedureModelService.selectByIds(procedureModelIdS);
        if (CollUtil.isEmpty(procedureModelDetail)) {
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_EXIST);
        }
        List<InstructionTeam> teams = InstructionTeamConverter.INSTANCE.convertToSaveTeamList(collect, procedureModelDetail, plan);
        instructionTeamMapper.insertBatch(teams);
        //指令单状态更新为确定
        collect.forEach(item->item.setStatus(InstructionStatusEnum.CONFIRM));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(Long id) {
        Plan plan = planMapper.selectById(id);
        if (Objects.isNull(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        if (!(ProductPlanStatusEnum.CONFIRM == plan.getStatus()
            && ProductPlanInstructStatusEnum.WAIT_SEND == plan.getInstructStatus())) {
            throw new BmosException(MesResponseCode.CONFIRM_AND_WAIT_SEND);
        }
        planMapper.updateInstructStatus(id,
            ProductPlanInstructStatusEnum.getNextStatus(plan.getInstructStatus(), ProductPlanInstructStatusEvent.SEND)
        );

        // 业务组件实例初始化
        List<BusinessComponentInstance> instances = componentManager.initComponentInstance(id, componentManager.getInitComponentTypes());

        List<BusinessComponentInstance> materialInputComponents = instances.stream()
                .filter(item -> Objects.equals(item.getComponentType(), BusinessComponentTypeEnum.MATERIAL_INPUT))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(materialInputComponents)){
            // 称量需求创建
            weighRequirementService.createRequirement(id, materialInputComponents);
        }
    }

    @Override
    public List<InstructionProcedureVO> teamDetail(TeamDetailQueryDTO dto) {
        //获取产线id
        Plan plan = planMapper.selectById(dto.getPlanId());
        //查询工序信息
        List<InstructionProcedureVO> procedureVOS = InstructionConverter.INSTANCE.convertProcedureTeamVO(
                instructionMapper.queryListByPlanIdAndProcedureModelId(dto));
        if (CollUtil.isEmpty(procedureVOS)){
            return Collections.emptyList();
        }
        procedureVOS.sort(Comparator.comparingInt(InstructionProcedureVO::getSort));
        //默认为第一次换班
        List<InstructionTeamVO> teamList = InstructionTeamConverter.INSTANCE.convertChangeTeamList(
                instructionTeamMapper.selectListByPlanIdS(Collections.singletonList(dto.getPlanId())));
        if (dto.getChangeTeamNumber() != 0){
            teamList = productChangeTeamService.selectListByPlanId(dto.getPlanId(),dto.getNodeFunction(),dto.getChangeTeamNumber());
        }
        Set<Long> stepModelId = CollectionUtils.convertSet(teamList, InstructionTeamVO::getProcedureStepModelId);
        Map<Long, Integer> stepModelMap = QueryProcessConfigSortUtils.queryProcedureStepModelSortByIdList(stepModelId);
        teamList.forEach(item->item.setSort(stepModelMap.get(item.getProcedureStepModelId())));
        teamList.sort(Comparator.comparingInt(InstructionTeamVO::getSort));
        //判断工步是否配置班组信息

        List<ProcedureStepRole> stepRoles = roleRelationMapper.selectListByProcedureStepIds(stepModelId);
        List<Long> stepRoleIdS = CollectionUtils.convertList(stepRoles, ProcedureStepRole::getProcedureStepId);
        teamList.forEach(item->{
            if (CollUtil.isNotEmpty(stepRoleIdS) && stepRoleIdS.contains(item.getProcedureStepModelId())){
                item.setIsFlay(true);
                return;
            }
            item.setIsFlay(false);
        });
        return InstructionConverter.INSTANCE.convertChangeTeamVo(procedureVOS,teamList,plan.getProductionLineId());
    }
}
