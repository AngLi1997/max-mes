package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.dto.IntactMergeListQueryDTO;
import com.bmos.mes.service.execute.vo.IntactFormDataVO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.team.service.InstructionTeamService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.process.convert.ProcedureStepModelConverter;
import com.bmos.mes.service.process.convert.ProcessStepConverter;
import com.bmos.mes.service.process.convert.Task.ProcedureConditionConverter;
import com.bmos.mes.service.process.convert.Task.ProcedureExpressionConverter;
import com.bmos.mes.service.process.dto.*;
import com.bmos.mes.service.process.dto.query.CalculateDataQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcedureStepHistoricQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessRecordOrderQueryDTO;
import com.bmos.mes.service.process.dto.save.ProcedureStepConfigSaveDTO;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.service.*;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.service.task.ProcedureConditionService;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import com.bmos.mes.service.record.base.util.BasicComponentUtils;
import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;
import com.bmos.mes.service.process.vo.Task.NodeVO;
import com.bmos.mes.service.process.vo.Task.ProcedureStepAndTaskVO;
import com.bmos.mes.service.record.mapper.BatchRecordItemMapper;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.model.BatchRecordParse;
import com.bmos.mes.service.record.service.BatchRecordParseService;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mes.service.workflow.vo.ProcedureStepDurationVO;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import com.bmos.platform.facade.factory.vo.StationPermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

@Service
public class ProcedureStepModelServiceImpl extends ServiceImpl<ProcedureStepModelMapper, ProcedureStepModel> implements ProcedureStepModelService {

    @Autowired
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private ProcedureStepRoleRelationService procedureStepRoleRelationService;

    @Autowired
    private ProcedureStepService procedureStepService;

    @Autowired
    private InstructionTeamService instructionTeamService;

    @Autowired
    private FactoryFeign factoryFeign;

    @Autowired
    private ProcedureExpressionService expressionService;

    @Autowired
    private ProcedureConditionService conditionService;
    @Autowired
    private PlanService planService;
    @Autowired
    private BatchRecordItemMapper recordItemMapper;
    @Resource
    private BatchRecordParseService parseService;

    @Resource
    private ProcedureStepRoleRelationService relationService;

    @Resource
    private ProcedureStepSopService sopService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(ProcedureModel procedureModel, List<ProcedureStepDTO> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        List<ProcedureStepRole> roles = new ArrayList<>();
        List<ProcedureStepSop> sopList = new ArrayList<>();
        List<ProcedureStepModel> steps = new ArrayList<>(list.size());
        Integer maxSort = procedureStepModelMapper.selectByMaxSort(procedureModel.getId(), procedureModel.getProcessVersion(),
                procedureModel.getProcessId());
        AtomicInteger sort = new AtomicInteger(maxSort);
        for (ProcedureStepDTO procedureStepDTO : list) {
            Long stepModelId = IdUtils.getSnowflake();
            if (CollUtil.isNotEmpty(procedureStepDTO.getRoles())) {
                for (Long roleId : procedureStepDTO.getRoles()) {
                    roles.add(ProcedureStepRole.builder().procedureStepId(stepModelId).roleId(roleId).build());
                }
            }
            //操作规程
            if (CollUtil.isNotEmpty(procedureStepDTO.getOperationSopId())){
                List<ProcedureStepSop> sops = procedureStepDTO.getOperationSopId().stream().map(item -> {
                    ProcedureStepSop sop = new ProcedureStepSop();
                    sop.setOperationSopId(item);
                    sop.setStepModelId(stepModelId);
                    return sop;
                }).collect(Collectors.toList());
                sopList.addAll(sops);
            }
            ProcedureStepModel model = ProcedureStepModelConverter.INSTANCE.convert(procedureModel, procedureStepDTO,
                    stepModelId,sort.getAndIncrement());
            steps.add(model);
            //处理逻辑表达式与条件
            this.handleCondition(procedureStepDTO, model);
        }
        try {
            procedureStepModelMapper.insertBatch(steps);
        } catch (DuplicateKeyException e) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_EXIST);
        }
        //保存角色
        procedureStepRoleRelationService.saveBatch(roles);
        sopService.saveBatch(sopList);
    }

    public void handleCondition(ProcedureStepDTO stepDTO, ProcedureStepModel model) {
        ExpressionSaveDTO executeCondition = stepDTO.getExecuteCondition();
        ExpressionSaveDTO completeCondition = stepDTO.getCompleteCondition();
        List<ProcedureExpression> expressionList = new ArrayList<>();
        List<ProcedureCondition> conditionList = new ArrayList<>();
        if (executeCondition != null && StrUtil.isNotBlank(executeCondition.getExpression())) {
            this.saveExpressionAndCondition(executeCondition,expressionList,conditionList,model,ExpressionTypeEnum.EXECUTE_CONDITION);
        }
        if (completeCondition != null && StrUtil.isNotBlank(completeCondition.getExpression())) {
            this.saveExpressionAndCondition(completeCondition,expressionList,conditionList,model,ExpressionTypeEnum.EXECUTE_CONDITION);
        }
        expressionService.insertBatch(expressionList);
        conditionService.insertBatch(conditionList);
    }

    /**
     * 添加表达式以及条件
     * @param saveDTO 需要保存的数据
     * @param expressionList 初始化表达式集合
     * @param conditionLis 初始化条件集合
     * @param model 工步数据
     * @param typeEnum 表达式类型
     */
    private void saveExpressionAndCondition(ExpressionSaveDTO saveDTO,List<ProcedureExpression> expressionList,
                                            List<ProcedureCondition> conditionLis,ProcedureStepModel model,
                                            ExpressionTypeEnum typeEnum){
        ProcedureExpression expression = ProcedureExpressionConverter.INSTANCE.convertExpression(saveDTO
                , model, typeEnum.getValue());
        expressionList.add(expression);
        List<ProcedureCondition> condition =
                ProcedureConditionConverter.INSTANCE.convertConditionList(saveDTO.getConditionList(),
                        expression.getId(), model.getId(), NodeTypeEnum.STEP_OR_TASK.getValue());
        conditionLis.addAll(condition);
    }

    @Override
    public ProcedureStepAndTaskVO getByProcedureModelId(String recordVersionIds, Long procedureModelId) {
        List<String> versionIdList = StrUtil.split(recordVersionIds, StrUtil.C_COMMA);
        List<ProcedureStepModel> procedureStepModels =
                procedureStepModelMapper.selectByProcedureModelId(procedureModelId);
        Set<Long> ids = CollectionUtils.convertSet(procedureStepModels, ProcedureStepModel::getId);
        List<ProcedureStepRole> roles = procedureStepRoleRelationService.getListByProcedureStepIds(ids);
        List<ProcedureStepSop> stepSops = sopService.queryListByStepModelId(ids);
        List<ProcedureStep> steps = procedureStepService.getByIds(CollectionUtils.convertSet(procedureStepModels, ProcedureStepModel::getProcedureStepId));
        Map<Long, ProcedureStep> stepMap = CollectionUtils.convertMap(steps, ProcedureStep::getId);
        List<ProcedureStepVO> procedureStepVo =
                ProcedureStepModelConverter.INSTANCE.convertVOList(procedureStepModels, roles,stepSops, stepMap);
        if (CollUtil.isEmpty(procedureStepModels)) {
            return new ProcedureStepAndTaskVO();
        }
        if (CollUtil.isNotEmpty(versionIdList)){
            //查询记录项
            List<BatchRecordItem> items = recordItemMapper.queryItemListByVersionIdList(
                    versionIdList.stream().map(Long::valueOf).collect(Collectors.toList()));
            Map<Long, List<Long>> itemMap = CollectionUtils.convertMultiMap(items, BatchRecordItem::getRecordVersionId,
                    BatchRecordItem::getItemId);
            procedureStepVo.forEach(item->{
                if (ObjectUtil.isNull(item.getRecordVersionId()) || ObjectUtil.isNull(item.getRecordItemId())){
                    return;
                }
                if(!versionIdList.contains(String.valueOf(item.getRecordVersionId()))){
                    item.setRecordVersionId(null);
                    item.setRecordItemId(null);
                }
                List<Long> list = itemMap.get(item.getRecordVersionId());
                if (CollUtil.isEmpty(list) || !list.contains(item.getRecordItemId())){
                    item.setRecordVersionId(null);
                    item.setRecordItemId(null);
                }
            });
        }
        this.wrapperCondition(procedureModelId, procedureStepVo);
        ProcedureStepAndTaskVO vo = new ProcedureStepAndTaskVO();
        Map<StepTaskTypeEnum, List<ProcedureStepVO>> stepTaskTypeEnumListMap =
                procedureStepVo.stream().collect(Collectors.groupingBy(ProcedureStepVO::getStepTaskType));
        vo.setStepList(stepTaskTypeEnumListMap.get(StepTaskTypeEnum.STEP));
        vo.setTaskList(stepTaskTypeEnumListMap.get(StepTaskTypeEnum.TASK));
        return vo;
    }

    private void wrapperCondition(Long procedureModelId, List<ProcedureStepVO> procedureStepVo) {
        //找到表达式与条件
        Map<String, List<ExpressionDetailVO>> detailMap = expressionService.getListByProcedureModelId(procedureModelId);
        if (CollUtil.isEmpty(detailMap)) {
            return;
        }
        Map<Long, ExpressionDetailVO> executeMap =
                CollectionUtils.convertMap(detailMap.get(ExpressionTypeEnum.EXECUTE_CONDITION.getValue()),
                        ExpressionDetailVO::getStepTaskId);
        Map<Long, ExpressionDetailVO> completeMap =
                CollectionUtils.convertMap(detailMap.get(ExpressionTypeEnum.COMPLETE_CONDITION.getValue()),
                        ExpressionDetailVO::getStepTaskId);
        procedureStepVo.forEach(item -> {
            item.setExecuteCondition(executeMap.get(item.getId()));
            item.setCompleteCondition(completeMap.get(item.getId()));
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(ProcedureStepConfigSaveDTO dto) {
        if (CollUtil.isEmpty(dto.getComponents())) {
            return;
        }
        Long procedureStepModelId = dto.getProcedureStepModelId();
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(procedureStepModelId);
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        dto.setRecordVersionId(procedureStepModel.getRecordVersionId());
        if (dto.getReusable()) {
            procedureStepConfigService.deleteReuse(dto);
        } else {
            procedureStepConfigService.deleteByProcedureStepModelId(procedureStepModelId);
        }
        List<ProcedureStepConfig> configs = ProcedureStepModelConverter.INSTANCE.convertConfigLit(dto);
        procedureStepConfigService.saveBatch(configs);
    }

    @Override
    public List<ComponentConfigVO> getConfigList(ProcedureStepConfigListQueryDTO dto) {
        Long procedureStepModelId = dto.getProcedureStepModelId();
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(procedureStepModelId);
        //如果是无记录页配置的节点直接返回
        if (ProcedureStepNodeFunctionEnum.notRecordNode(procedureStepModel.getNodeFunction())){
            return Collections.emptyList();
        }
        List<ProcedureStepConfig> configs = procedureStepConfigService.getListByProcedureStepModel(procedureStepModel);
        if (CollUtil.isEmpty(configs)) {
            return Collections.emptyList();
        }
        return ProcedureStepModelConverter.INSTANCE.convertComponentVO(configs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdNotIn(Long procedureModelId, List<Long> ids) {
        procedureStepModelMapper.deleteByIdNotIn(procedureModelId, ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchRoles(List<ProcedureStepDTO> items) {
        procedureStepRoleRelationService.deleteByProcedureStepIds(CollectionUtils.convertList(items,
                ProcedureStepDTO::getId));
        List<ProcedureStepRole> roles = new ArrayList<>();
        items.forEach(item -> {
            if (CollUtil.isNotEmpty(item.getRoles())) {
                roles.addAll(item.getRoles().stream().map(e -> ProcedureStepRole.builder().procedureStepId(item.getId()).roleId(e).build())
                        .collect(Collectors.toList()));
            }
        });
        procedureStepRoleRelationService.saveBatch(roles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchById(ProcedureModel procedureModel, List<ProcedureStepDTO> items) {
        List<ProcedureStepModel> stepModels = ProcedureStepModelConverter.INSTANCE.convertList(procedureModel, items);
        procedureStepModelMapper.updateBatch(stepModels);
        // 更新体哦阿健
        this.updateCondition(procedureModel, items);
    }

    private void updateCondition(ProcedureModel procedureModel, List<ProcedureStepDTO> items) {
        List<ProcedureStepDTO> execute = CollectionUtils.filterList(items, item ->
                ObjectUtil.isNotEmpty(item.getExecuteCondition()));
        List<ProcedureStepDTO> complete = CollectionUtils.filterList(items, item ->
                ObjectUtil.isNotEmpty(item.getCompleteCondition()));
        List<ProcedureExpression> expressions = new ArrayList<>();
        List<ProcedureCondition> conditionsList = new ArrayList<>();
        List<Long> expressionRemoveId = new ArrayList<>();
        execute.forEach(item -> {
            if (ObjectUtil.isNotEmpty(item.getExecuteCondition())) {
                ProcedureExpression expression = ProcedureExpressionConverter.INSTANCE.convertSaveExpression(
                        item.getExecuteCondition(), item, ExpressionTypeEnum.EXECUTE_CONDITION.getValue(),
                        procedureModel.getId());
                if (StrUtil.isBlank(expression.getExpression()) || CollUtil.isEmpty(item.getExecuteCondition().getConditionList())){
                    expressionRemoveId.add(expression.getId());
                    return;
                }
                List<ProcedureCondition> conditions =
                        ProcedureConditionConverter.INSTANCE.convertConditionList(item.getExecuteCondition().getConditionList(),
                                expression.getId(),item.getId(),NodeTypeEnum.STEP_OR_TASK.getValue());
                expressions.add(expression);
                conditionsList.addAll(conditions);
            }
        });
        complete.forEach(item -> {
            if (ObjectUtil.isNotEmpty(item.getCompleteCondition())) {
                ProcedureExpression expression = ProcedureExpressionConverter.INSTANCE.convertSaveExpression(
                        item.getCompleteCondition(), item, ExpressionTypeEnum.COMPLETE_CONDITION.getValue(),
                        procedureModel.getId());
                if (StrUtil.isBlank(expression.getExpression()) || CollUtil.isEmpty(item.getCompleteCondition().getConditionList())){
                    expressionRemoveId.add(expression.getId());
                    return;
                }
                List<ProcedureCondition> conditions =
                        ProcedureConditionConverter.INSTANCE.convertConditionList(item.getCompleteCondition().getConditionList(),
                                expression.getId(), item.getId(),NodeTypeEnum.STEP_OR_TASK.getValue());

                expressions.add(expression);
                conditionsList.addAll(conditions);
            }
        });
        // 获取已有的表达式和条件
        List<ProcedureExpression> exitsExpressions =
                expressionService.getByProcedureStepModelIds(items.stream().map(ProcedureStepDTO::getId).collect(Collectors.toList()));
        List<ProcedureCondition> procedureConditions =
                conditionService.getByProcedureStepModelIds(items.stream().map(ProcedureStepDTO::getId).collect(Collectors.toList()));
        Set<Long> exitsExpressionIdSet = exitsExpressions.stream().map(ProcedureExpression::getId).collect(Collectors.toSet());
        Set<Long> exitsConditionIdSet =
                procedureConditions.stream().map(ProcedureCondition::getId).collect(Collectors.toSet());
        Set<Long> nowExpressionIdSet = expressions.stream().map(ProcedureExpression::getId).collect(Collectors.toSet());
        Set<Long> nowConditionIdSet =
                conditionsList.stream().map(ProcedureCondition::getId).collect(Collectors.toSet());
        // 计算差集，差集结果就是删除的表达式和条件
        exitsExpressionIdSet.removeAll(nowExpressionIdSet);
        exitsConditionIdSet.removeAll(nowConditionIdSet);
        if (CollectionUtil.isNotEmpty(exitsConditionIdSet)){
            conditionService.removeBatchByIds(exitsConditionIdSet);
        }
        if (CollectionUtil.isNotEmpty(exitsExpressionIdSet)){
            expressionService.removeBatchByIds(exitsExpressionIdSet);
        }
        if (CollUtil.isNotEmpty(expressionRemoveId)){
            expressionService.removeBatchByIds(expressionRemoveId);
        }
        // 保存修改的和新增的
        if (CollUtil.isEmpty(expressions)){
            return;
        }
        expressionService.saveOrUpdateBatch(expressions);
        conditionService.saveOrUpdateBatch(conditionsList);
    }

    @Override
    public List<HistoricVO> getHistoricStepList(ProcedureStepHistoricQueryDTO dto) {
        List<HistoricVO> historic =
                ProcessStepConverter.INSTANCE.convertHistoric(procedureStepService.getHistoricList(dto));
        if (CollUtil.isNotEmpty(historic)) {
            return historic.stream().collect(
                    Collectors.collectingAndThen(
                            toCollection(() -> new TreeSet<>(Comparator.comparing(HistoricVO::getName))),
                            ArrayList::new));
        }
        return historic;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<ProcedureStepModel> procedureStepModels) {
        if (CollUtil.isEmpty(procedureStepModels)) {
            return;
        }
        procedureStepModelMapper.insertBatch(procedureStepModels);
    }

    @Override
    public List<ProcedureStepModel> getByProcedureModelIds(List<Long> procedureModelIds) {
        if (CollUtil.isEmpty(procedureModelIds)) {
            return Collections.emptyList();
        }
        return procedureStepModelMapper.selectByProcedureModelIds(procedureModelIds);
    }

    @Override
    public List<ProcessRecordVO> getRecords(ProcessRecordOrderQueryDTO dto) {
        return procedureStepModelMapper.selectRecords(dto);
    }

    @Override
    public ProcedureStepRecordItemVO getRecordItem(ProcedureStepRecordItemQueryDTO dto) {
        ProcedureStepModel stepModel = procedureStepModelMapper.selectOneByCondition(dto);
        if (ProcedureStepNodeFunctionEnum.notRecordNode(stepModel.getNodeFunction())){
            return null;
        }
        // 查询生产计划
        // 关联批记录组件表查询数据
        List<ComponentConfigDetailVO> configs = procedureStepConfigService.getComponentsByProcedureStepModel(stepModel);
        if (CollectionUtil.isNotEmpty(configs)) {
            Plan plan = planService.getById(dto.getProductPlanId());
            configs.forEach(item -> setHasRight(item, true));
            Map<Long, List<Long>> stations = BasicComponentUtils.getStations(configs, plan.getProductionLineId());
            if (CollectionUtil.isNotEmpty(stations)) {
                handStationRight(configs, stations, dto.getProductPlanId());
            }
        }
        return ProcedureStepModelConverter.INSTANCE.convert(stepModel, configs);
    }

    private void setHasRight(ComponentConfigDetailVO item, boolean hasRight) {
        item.setHasRight(hasRight);
        if (CollUtil.isNotEmpty(item.getChildren())) {
            item.getChildren().forEach(e->{setHasRight(e, hasRight);});
        }
    }

    private void handStationRight(List<ComponentConfigDetailVO> configs, Map<Long, List<Long>> stations, Long planId) {
        Plan plan = planService.getById(planId);
        if (ObjectUtil.isEmpty(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        Long planLineId = plan.getProductionLineId();
        // 查询产线下的所有工位
        ResponseInfo<List<FactoryStationFeignVO>> listResponseInfo =
                FeignUtils.handleRequest(data -> factoryFeign.getStationInfoByLineId(data), planLineId);
        List<FactoryStationFeignVO> lineStationList = listResponseInfo.getData();
        if (CollectionUtil.isEmpty(lineStationList)) {
            // 产线下没有工位则不进行工位权限限制
            configs.forEach(item -> {
                if (CollectionUtil.isNotEmpty(stations.get(item.getId()))) {
                    setHasRight(item, true);
                }
            });
            return;
        }
        Set<Long> lineStationIdSet =
                lineStationList.stream().map(FactoryStationFeignVO::getId).collect(Collectors.toSet());
        Set<Long> stationIds =
                stations.values().stream().flatMap(curStationList -> curStationList.stream().filter(lineStationIdSet::contains)).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(stationIds)) {
            configs.forEach(item -> {
                if (CollectionUtil.isNotEmpty(stations.get(item.getId()))) {
                    setHasRight(item, true);
                }
            });
        }
        ResponseInfo<List<StationPermissionVO>> responseInfo = factoryFeign.checkStationPermission(stationIds,
                SysUserHolder.getUser().getUserId());
        List<StationPermissionVO> data = responseInfo.getData();
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        Map<Long, StationPermissionVO> stationPermissionVOMap =
                data.stream().collect(Collectors.toMap(StationPermissionVO::getStationId, Function.identity()));
        for (ComponentConfigDetailVO item : configs) {
            List<Long> stationIdList = stations.get(item.getId());
            boolean hasRight = false;
            if (CollectionUtil.isEmpty(stationIdList)) {
                continue;
            }
            for (Long stationIdItem : stationIdList) {
                if (!stationPermissionVOMap.containsKey(stationIdItem)) {
                    continue;
                }
                hasRight |= stationPermissionVOMap.get(stationIdItem).isPermission();
            }
            setHasRight(item, hasRight);
        }
    }

    @Override
    public List<ProcessStepVO> getListByProcess(ProcessStepQueryDTO dto) {
        return procedureStepModelMapper.selectListByProcess(dto);
    }

    @Override
    public List<FieldConfigVO> getFieldsConfig(CalculateDataQueryDTO query, List<Long> fieldIds) {
        return procedureStepModelMapper.selectFieldsConfig(query, fieldIds);
    }

    @Override
    public List<PlatformUserVO> getGroupUserList(ProcedureStepGroupUserDTO dto) {
        List<String> userIds = instructionTeamService.findInstructionPeople(dto);
        return userIds.stream().distinct().map(user -> {
            BaseUserDO userDO = UserUtils.getUser(user);
            PlatformUserVO vo = new PlatformUserVO();
            vo.setUserId(userDO.getUserId());
            vo.setUserName(userDO.getUserName());
            vo.setLoginName(userDO.getLoginName());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<IntactFormDataVO> getRecordContents(IntactMergeListQueryDTO dto) {
        dto.setNodeFunction(ProcedureStepNodeFunctionEnum.SUB_RECORD.getValue());
        List<IntactFormDataVO> vos = procedureStepModelMapper.selectRecordContents(dto);
        List<Long> recordItemId = CollectionUtils.convertList(vos, IntactFormDataVO::getId);
        Map<Long, BatchRecordParse> parseMap = CollectionUtils.convertMap(parseService.selectByItemId(recordItemId), BatchRecordParse::getId);
        if (CollUtil.isNotEmpty(vos) && CollUtil.isNotEmpty(parseMap)){
            vos.forEach(item->{
                BatchRecordParse parse = parseMap.getOrDefault(item.getId(), new BatchRecordParse());
                item.setFileContent(parse.getFileContent());
                item.setHeaderContent(parse.getDocxHeader());
                item.setFooterContent(parse.getDocxFooter());
            });
        }
        return vos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProcedureModelId(Long procedureModelId) {
        List<ProcedureStepModel> procedureStepModels =
                procedureStepModelMapper.selectByProcedureModelId(procedureModelId);
        if (CollectionUtil.isEmpty(procedureStepModels)) {
            return;
        }
        procedureStepModelMapper.deleteByProcedureModelId(procedureModelId);
        // 删除这个步骤模型关联的表达式和条件
        List<Long> modelIds = procedureStepModels.stream().map(ProcedureStepModel::getId).collect(Collectors.toList());
        expressionService.deleteByProcedureStepModelIds(modelIds);
        conditionService.deleteByProcedureStepModelIds(modelIds);
        //删除sop绑定关系
        sopService.deleteBatchByStepModelIds(modelIds);
    }

    @Override
    public ProcedureStepModel getById(Long procedureStepModelId) {
        return procedureStepModelMapper.selectById(procedureStepModelId);
    }

    @Override
    public List<ProcessRecordItemVO> queryRecordVersionIdByProcessId(Long processId, String processVersion,
                                                                     List<Long> modelId) {
        return procedureStepModelMapper.queryRecordVersionIdByProcessId(processId, processVersion, modelId);
    }

    @Override
    public List<ProcedureStepDurationVO> getProcedureAndStepDurationByNodeIds(List<String> procedureStepNodeIdList) {
        return procedureStepModelMapper.selectDurationByNodeIds(procedureStepNodeIdList);
    }

    @Override
    public List<ProcedureStepModel> getStepModelByProcessIdAndVersion(Long processId, String modifyBeforeVersion) {
        return procedureStepModelMapper.getStepModelByProcessIdAndVersion(processId, modifyBeforeVersion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatch(List<ProcedureStepModel> stepModel) {
        if (CollUtil.isEmpty(stepModel)) {
            return;
        }
        procedureStepModelMapper.updateBatch(stepModel);
    }

    @Override
    public List<NodeVO> getNodeListByProcedureModeId(Long id, Boolean type,Long stepModelId) {
        List<ProcedureStepModel> modelList = procedureStepModelMapper.getNodeListByProcedureModeId(id);
        List<String> conditionList;
        if (type) {
            modelList = CollectionUtils.filterList(modelList, item -> item.getStepType()==StepTaskTypeEnum.STEP);
            conditionList = expressionService.getStepModelCondition(Collections.singletonList(stepModelId),
                    Collections.singletonList(ConditionTypeEnum.STEP_NODE_COMPLETE.getValue()));
        } else {
            modelList = CollectionUtils.filterList(modelList, item -> item.getStepType()==StepTaskTypeEnum.TASK);
            conditionList = expressionService.getStepModelCondition(Collections.singletonList(stepModelId),
                    Collections.singletonList(ConditionTypeEnum.TASK_NODE_COMPLETE.getValue()));
        }
        List<NodeVO> voList = new ArrayList<>();
        modelList.forEach(item -> {
            NodeVO vo = new NodeVO();
            vo.setId(item.getId());
            vo.setName(item.getName());
            voList.add(vo);
        });
        //判断是否删除数据
        if (CollUtil.isEmpty(conditionList)){
            return voList;
        }
        List<Long> taskId = CollectionUtils.convertList(voList, NodeVO::getId);
        List<Long> deleteTaskId = new ArrayList<>();
        conditionList.forEach(item -> {
            ConditionDetailVO detailVO = JsonUtils.parseObject(item, ConditionDetailVO.class);
            Long taskOrStepId = type ? detailVO.getStepId() : detailVO.getTaskNodeId();
            if (!taskId.contains(taskOrStepId)){
                deleteTaskId.add(taskOrStepId);
            }
        });
        voList.addAll(this.queryByIds(deleteTaskId));
        return voList;
    }

    @Override
    public List<ProcedureStepModel> getStepModelByProcessIdAndVersionAndNodeIdList(Long processId,
                                                                                   String processVersion) {
        return procedureStepModelMapper.getStepModelByProcessIdAndVersionAndNodeIdList(processId, processVersion);
    }

    @Override
    public List<ProcedureStepModel> getByIdList(List<Long> idList) {
        return procedureStepModelMapper.selectList(new LambdaQueryWrapperX<ProcedureStepModel>()
                .in(ProcedureStepModel::getId, idList));
    }

    @Override
    public List<ProcedureStepDurationVO> getProcedureAndStepDurationByStepModeIds(List<Long> procedureStepNodeIdList) {
        return procedureStepModelMapper.selectDurationByStepModeIds(procedureStepNodeIdList);
    }

    @Override
    public List<ProcedureStepModel> getByProcedureModelId(Long procedureModelId) {
        return procedureStepModelMapper.selectByProcedureModelId(procedureModelId);
    }

    @Override
    public List<ProcedureStepModelDetailVO> queryStepModelList(ProcedureStepModelQueryDTO dto) {
        return procedureStepModelMapper.queryStepModelList(dto);
    }

    @Override
    public List<ProcedureStepModelListVO> getListByProcedureModelId(Long procedureModelId) {
        return ProcedureStepModelConverter.INSTANCE.convert2ListVO(procedureStepModelMapper.getNodeListByProcedureModeId(procedureModelId));
    }

    @Override
    public List<ProcedureStepModelVO> selectByProcedureModelIdS(List<Long> procedureModelIdS) {
        List<ProcedureStepModel> stepModelList = procedureStepModelMapper.selectByProcedureModelIds(procedureModelIdS);
        //查询班组信息
        Set<Long> ids = CollectionUtils.convertSet(stepModelList, ProcedureStepModel::getId);
        List<ProcedureStepRole> roles = procedureStepRoleRelationService.getListByProcedureStepIds(ids);
        return ProcedureStepModelConverter.INSTANCE.convertStepModelVOList(stepModelList, roles);
    }

    @Override
    public List<ProcessSortVO> selectStepModelSort(List<Long> modelIdList, Long processId, String version) {
        List<ProcedureStepModel> stepModelList = procedureStepModelMapper.getStepModelByProcessIdAndVersion(processId, version);
        List<ProcedureStepModel> stepModels = CollectionUtils.filterList(stepModelList, item -> modelIdList.contains(item.getProcedureModelId()));
        if (CollUtil.isEmpty(stepModels)){
            return Collections.emptyList();
        }
        return ProcedureStepModelConverter.INSTANCE.convertToStepModelSortList(stepModels);
    }

    @Override
    public List<ProcedureStepModel> queryListByProcessIdAndVersionAndModelId(Long processId, String processVersion, Long procedureModelId) {
        List<ProcedureStepModel> stepModelList = procedureStepModelMapper.getStepModelByProcessIdAndVersion(processId, processVersion);
        if (CollUtil.isEmpty(stepModelList)){
            return Collections.emptyList();
        }
        List<ProcedureStepModel> stepModels = CollectionUtils.filterList(stepModelList, item -> procedureModelId.equals(item.getProcedureModelId()));
        return stepModels;
    }

    @Override
    public List<IntactFormDataVO> getRecordContentsByNodeFunction(IntactMergeListQueryDTO queryDTO) {
        return procedureStepModelMapper.selectRecordContentsByNodeFunction(queryDTO);
    }

    @Override
    public List<String> selectByProcessAndRecordItemId(Long processId, Long recordItemId) {
        List<ProcedureStepModel> procedureStepModels = procedureStepModelMapper.selectByProcessAndRecordItemId(processId, recordItemId);
        if (CollUtil.isEmpty(procedureStepModels)){
            return new ArrayList<>();
        }
        return new ArrayList<>(procedureStepModels.stream().map(ProcedureStepModel::getProcessVersion).collect(Collectors.toSet()));
    }

    @Override
    public List<ProcedureStepRole> getStepTeamIdByProcessIdAndVersion(Long processId, String version) {
        List<ProcedureStepModel> stepModelList = procedureStepModelMapper.getStepModelByProcessIdAndVersion(processId, version);
        return relationService.getListByProcedureStepIds(CollectionUtils.convertSet(stepModelList, ProcedureStepModel::getId));
    }

    @Override
    public List<NodeVO> queryByIds(List<Long> deleteTaskId) {
        if (CollUtil.isEmpty(deleteTaskId)){
            return new ArrayList<>();
        }
        List<NodeVO> voList = procedureStepModelMapper.queryByIds(deleteTaskId);
        if (CollUtil.isNotEmpty(voList)){
            voList.forEach(item->item.setDisabled(true));
        }
        return voList;
    }

    @Override
    public List<ProcedureStepModel> getByProcessAndRecord(List<ProcessRecordQueryDTO> queryDTOS) {
        if (CollUtil.isEmpty(queryDTOS)){
            return new ArrayList<>();
        }
        return procedureStepModelMapper.selectByProcessAndRecord(queryDTOS);
    }
}
