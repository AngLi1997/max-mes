package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.process.ActionStateEnum;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.formula.mapper.ProductFormulaMapper;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.vo.ProductFormulaListVO;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import com.bmos.mes.service.plan.team.mapper.ProductPlanTeamMapper;
import com.bmos.mes.service.plan.team.vo.ProductPlanTeamListVO;
import com.bmos.mes.service.platform.role.role.PlatformRoleVO;
import com.bmos.mes.service.process.convert.ProcessVersionConverter;
import com.bmos.mes.service.process.dto.ProcessTodoPageDTO;
import com.bmos.mes.service.process.dto.ProcessVersionQueryDTO;
import com.bmos.mes.service.process.dto.modify.ProcessModifyDTO;
import com.bmos.mes.service.process.dto.modify.ProcessSaveVersionDTO;
import com.bmos.mes.service.process.dto.modify.ProcessVersionChangeStateDTO;
import com.bmos.mes.service.process.dto.query.ProcessDetailQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessVersionPageQueryDTO;
import com.bmos.mes.service.process.mapper.ProcessProductionLineMapper;
import com.bmos.mes.service.process.mapper.ProcessVersionMapper;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.service.*;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;
import com.bmos.mes.service.process.vo.Task.NodeVO;
import com.bmos.mes.service.record.business.model.ProcessDetailInfo;
import com.bmos.mes.service.record.enums.RecordStateEnum;
import com.bmos.mes.service.record.mapper.BatchRecordVersionMapper;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.model.BatchRecordVersion;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.service.BatchRecordItemService;
import com.bmos.mes.service.workflow.service.WorkflowService;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.orchestrator.engine.core.command.CreateDeploymentCmd;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import com.bmos.platform.facade.factory.vo.FactoryRoomFeignVO;
import com.github.pagehelper.PageHelper;
import groovyjarjarpicocli.CommandLine;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.bmos.mes.service.process.constant.ProcessConstant.PROCESS_ENABLE_LOCK;

@Service
public class ProcessVersionServiceImpl implements ProcessVersionService {

    @Autowired
    private ProcessVersionMapper processVersionMapper;

    @Autowired
    @Lazy
    private WorkflowService workflowService;

    @Autowired
    @Lazy
    private ProcedureModelService procedureModelService;

    @Autowired
    private ProcessBatchRecordRelationService processBatchRecordRelationService;

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private BatchRecordItemService batchRecordItemService;

    @Autowired
    private BatchRecordComponentService batchRecordComponentService;

    @Autowired
    private OperationHistoryService logService;

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    @Lazy
    private ProcedureExpressionService expressionService;

    @Resource
    private ProcessFormulaRelationService processFormulaRelationService;

    @Resource
    @Lazy
    private ProductFormulaMapper productFormulaMapper;

    @Resource
    @Lazy
    private ProcessProductionLineMapper lineMapper;

    @Resource
    @Lazy
    private FactoryFeign factoryFeign;

    @Resource
    private BatchRecordVersionMapper versionMapper;

    @Resource
    private ProductPlanTeamMapper productPlanTeamMapper;

    @Autowired
    private EquipmentConfigFeign equipmentConfigFeign;

    @Autowired
    private ProcedureModelMaterialService materialService;

    @Autowired
    private ProductFormulaMaterialMapper materialMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ProcessVersion processVersion) {
        processVersionMapper.insert(processVersion);
    }

    @Override
    public CommonPage<ProcessVersionPageVO> getPage(ProcessVersionPageQueryDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<ProcessVersionPageVO> list = processVersionMapper.selectPageList(dto);
        return CommonPage.convertPage(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessVersion updateState(ProcessVersionChangeStateDTO dto) {
        ProcessVersion processVersion = processVersionMapper.selectById(dto.getId());
        if (ObjectUtil.isNull(processVersion)) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_NOT_EXIST);
        }
        RLock lock = redissonClient.getLock(String.format(PROCESS_ENABLE_LOCK, processVersion.getProcessId()));
        boolean locked = lock.tryLock();
        if (!locked) {
            throw new BmosException(BaseResponseCode.TRY_AGAIN_LATER);
        }
        try {
            //校验工艺必填项目等内容
            validateStates(dto, processVersion);
            //当状态为重新编辑时判断现有状态是否是确认状态
            if (StrUtil.equals(dto.getActionState(),ActionStateEnum.FRESH_EDIT.getValue()) &&
                    !StrUtil.equals(processVersion.getActionState(),ActionStateEnum.CONFIRM.getValue())){
                throw new BmosException(MesResponseCode.PROCESS_NOT_CONFIRM);
            }
            //立即生效将原有工艺版本改为失效
            if (StrUtil.equals(dto.getActionState(), ActionStateEnum.VALID.getValue())) {
                List<ProcessVersion> processVersions = processVersionMapper.selectListByProcessId(processVersion.getProcessId());
                //查询生效版本
                ProcessVersion version = CollectionUtils.findFirst(processVersions, item ->
                        StrUtil.equals(item.getActionState(), ActionStateEnum.VALID.getValue()));
                if (ObjectUtil.isNotEmpty(version)) {
                    version.setHistoryState(version.getActionState());
                    version.setActionState(ActionStateEnum.INVALID.getValue());
                    processVersionMapper.updateById(version);
                }
                processVersion.setEffectDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            //记录历史状态,审批不通过时回退到历史状态
            processVersion.setHistoryState(processVersion.getActionState());
            processVersion.setActionState(StrUtil.equals(dto.getActionState(),ActionStateEnum.FRESH_EDIT.getValue()) ?
                    ActionStateEnum.EDIT.getValue() : dto.getActionState());
            processVersionMapper.updateById(processVersion);
            //记录历史日志
            saveProcessVersionHistoryLog(dto.getActionState(), dto.getId());
            return processVersion;
        } finally {
            lock.unlock();
        }
    }

    private void saveProcessVersionHistoryLog(String operationType, Long businessId) {
        logService.save(OperationLogModel.builder()
                .module(BusinessModule.PROCESS.name())
                .businessId(businessId)
                .operationType(operationType)
                .createBy(SysUserHolder.getUser().getUserId())
                .build());
    }

    @Override
    public ProcessVersion getByProcessIdAndVersion(ProcessDetailQueryDTO dto) {
        return processVersionMapper.selectByProcessIdAndVersion(dto);
    }

    @Override
    public ProcessVersion getByProcessIdAndVersion(Long processId, String version) {
        return processVersionMapper.selectByProcessIdAndVersion(processId, version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyVersion(ProcessModifyDTO dto, ProcessVersion processVersion) {
        String name = processVersionMapper.selectProcessNameByProcessId(processVersion.getProcessId());
        CreateDeploymentCmd cmd = ProcessVersionConverter.INSTANCE.convertCmd(name, dto.getProcessModel(), processVersion.getVersion());
        String deploymentId = workflowService.createDeployment(cmd);
        processVersion.setProcessModelId(deploymentId);
        processVersion.setVersion(dto.getVersion());
        processVersion.setProductFormulaVersionId(dto.getProductFormulaVersionId());
        processVersion.setDescription(dto.getDescription());
        processVersion.setProductionStageCode(dto.getProductionStageCode());
        processVersionMapper.saveOrUpdate(processVersion);
    }

    @Override
    public ProcessVersion saveNewVersion(ProcessSaveVersionDTO dto, ProcessVersion processVersion) {
        String name = processVersionMapper.selectProcessNameByProcessId(processVersion.getProcessId());
        CreateDeploymentCmd cmd = ProcessVersionConverter.INSTANCE.convertCmd(name, dto.getProcessModel(), processVersion.getVersion());
        String deploymentId = workflowService.createDeployment(cmd);
        fillVersion(dto, processVersion, deploymentId);
        processVersionMapper.insert(processVersion);
        return processVersion;
    }

    @Override
    public List<ProcessVO> getProcessList(ProcessQueryDTO dto) {
        return processVersionMapper.selectProcessList(dto);
    }

    @Override
    public ProcessVersion getByProcessModel(Long processId, String version) {
        return processVersionMapper.selectByProcessIdAndVersion(processId, version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(ProcessVersion processVersion) {
        processVersionMapper.updateById(processVersion);
    }

    @Override
    public List<ProcessTodoPageVO> getAuditTodoProcessVersionIds(ProcessTodoPageDTO dto, String value) {
        return processVersionMapper.selectAuditTodoProcessVersionIds(dto, value);
    }

    @Override
    public List<ProcessTodoPageVO> getByProcessInstanceIds(List<String> processInstanceIds, String actionState) {
        return processVersionMapper.selectByProcessInstanceIds(processInstanceIds, actionState);
    }

    @Override
    public ProcessVersion getByProcessInstanceId(String processInstanceId) {
        return processVersionMapper.selectByProcessInstanceId(processInstanceId);
    }

    @Override
    public ProcessDetailInfo getProcessDetailInfo(Long processId, String processVersion) {
        return processVersionMapper.selectProcessDetailInfo(processId, processVersion);
    }


    @Override
    public List<ProcessVersion> selectListByProcessId(Long id) {
        return processVersionMapper.selectListByProcessId(id);
    }

    @Override
    public void validateVersionAudit(Long id) {
        List<ProcessVersion> processVersions = processVersionMapper.selectListByProcessId(id);
        List<ProcessVersion> processVersionsList = CollectionUtils.filterList(processVersions, item ->
                StrUtil.equals(item.getActionState(), ActionStateEnum.APPROVAL.getValue()) ||
                        StrUtil.equals(item.getActionState(), ActionStateEnum.WAIT_VALID.getValue()));
        if (CollUtil.isNotEmpty(processVersionsList)) {
            throw new BmosException(MesResponseCode.OPERATE_VERSION_FLOW_START_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessVersion updateVersionActionState(String processInstanceId) {
        ProcessVersion processVersion = getByProcessInstanceId(processInstanceId);
        if (ObjectUtil.isEmpty(processVersion)) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_NOT_EXIST);
        }
        processVersion.setHistoryState(processVersion.getActionState());
        //生效时间满足或者生效时间为立即生效
        if (StrUtil.equals(processVersion.getEffectDate(), StrUtil.DASHED) ||
                DateUtil.parse(processVersion.getEffectDate(), "yyyy-MM-dd").compareTo(DateUtil.parse(DateUtil.format(new Date(), "yyyy-MM-dd"))) <= 0) {
            List<ProcessVersion> processVersionList = processVersionMapper.selectListByProcessId(processVersion.getProcessId());
            ProcessVersion version = CollectionUtils.findFirst(processVersionList, item -> StrUtil.equals(item.getActionState(), ActionStateEnum.VALID.getValue()));
            if (ObjectUtil.isNotEmpty(version)) {
                version.setActionState(ActionStateEnum.INVALID.getValue());
                processVersionMapper.updateById(version);
            }
            processVersion.setActionState(ActionStateEnum.VALID.getValue());
            processVersion.setEffectDate(StrUtil.equals(processVersion.getEffectDate(), StrUtil.DASHED) ? DateUtil.formatDate(new Date()) : processVersion.getEffectDate());
        } else {
            processVersion.setActionState(ActionStateEnum.WAIT_VALID.getValue());
        }
        processVersionMapper.updateById(processVersion);
        return processVersion;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ProcessVersion> updateProcessVersionActionState(String state) {
        //待生效并且达到生效时间的
        List<ProcessVersion> versions = processVersionMapper.getListByActionStateAndDate(state, DateUtil.format(new Date(), "yyyy-MM-dd"));
        if (CollUtil.isEmpty(versions)) {
            return Collections.emptyList();
        }
        List<Long> processIds = CollectionUtils.convertList(versions, ProcessVersion::getProcessId);
        List<ProcessVersion> validVersionList = processVersionMapper.selectListByProcessIdListAndState(processIds, ActionStateEnum.VALID.getValue());
        if (CollUtil.isNotEmpty(validVersionList)) {
            validVersionList.forEach(item -> item.setActionState(ActionStateEnum.INVALID.getValue()));
            processVersionMapper.saveOrUpdateBatch(validVersionList);
        }
        versions.forEach(item -> {
            item.setActionState(ActionStateEnum.VALID.getValue());
        });
        processVersionMapper.saveOrUpdateBatch(versions);
        return versions;
    }

    @Override
    public List<ProcessVersion> selectByQueryDTOLIst(List<ProcessVersionQueryDTO> processVersionQueryDTOS) {
        return processVersionMapper.selectByQueryDTOLIst(processVersionQueryDTOS);
    }

    @Override
    public List<ProcessVersion> getByIds(Collection<Long> processVersionIdList) {
        return processVersionMapper.selectBatchIds(processVersionIdList);
    }


    private void fillVersion(ProcessSaveVersionDTO dto,
                             ProcessVersion processVersion,
                             String deploymentId) {
        processVersion.setProcessModelId(deploymentId);
        processVersion.setVersion(dto.getVersion());
        processVersion.setProductFormulaVersionId(dto.getProductFormulaVersionId());
        processVersion.setDescription(dto.getDescription());
    }

    @Override
    public void validateVersion(Long id, Long processId, String version) {
        if (processVersionMapper.versionExists(id, processId, version)) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_EXIST);
        }
    }

    @Override
    public ProcessVersion validateEditState(Long id) {
        ProcessVersion processVersion = processVersionMapper.selectById(id);
        if (ObjectUtil.isNull(processVersion)) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_NOT_EXIST);
        }
        if (!ActionStateEnum.EDIT.getValue().equals(processVersion.getActionState())) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_EDITING);
        }
        return processVersion;
    }

    @Override
    public ProcessVersion getById(Long id) {
        return processVersionMapper.selectById(id);
    }

    @Override
    public Boolean existByIdAndVersion(Long id, String version) {
        return processVersionMapper.existByIdAndVersion(id, version);
    }


    private void validateStates(ProcessVersionChangeStateDTO dto, ProcessVersion processVersion) {
        if (StrUtil.equals(dto.getActionState(), ActionStateEnum.CONFIRM.getValue())) {
            //校验记录是否确定
            processBatchRecordRelationService.validateBatchRecord(processVersion.getId());
            //校验流程步骤
            procedureModelService.validateProcessModel(processVersion.getId());
            workflowService.validateDeployment(processVersion.getProcessModelId());
            //校验记录项配置必填
            validateBatchRecordItem(processVersion.getId(), processVersion.getProcessId(), processVersion.getVersion());
            // 校验配置必填
            validateRequiredConfig(processVersion.getProcessId(), processVersion.getVersion());
            //校验工序以及步骤配置的条件
            this.validateCondition(processVersion.getId());
            // 校验配方物料配置版本是否一致
            processFormulaRelationService.validateFormulaMaterialMatch(processVersion.getId());
            //校验配方、记录版本、产线、班组、BOM物料、条件配置中物料预定量
            //this.validateProcessConfigId(processVersion);
        }
    }

    /**
     * 校验各项配置id是否失效或删除
     * @param processVersion 工艺版本
     */
    private void validateProcessConfigId(ProcessVersion processVersion){
        List<ProcedureModel> models = procedureModelService.getListByProcessVersionId(processVersion.getId());
        List<Long> procedureModelIdList = CollectionUtils.convertList(models, ProcedureModel::getId);
        List<ProcedureStepModel> stepModelList = procedureStepModelService.getByProcedureModelIds(procedureModelIdList);
        //配方配置
        ProductFormulaListVO vo = productFormulaMapper.getFormulaByVersionId(processVersion.getProductFormulaVersionId());
        if (BooleanUtil.isFalse(vo.getEnable()) || BooleanUtil.isTrue(vo.getIsDeleted())){
            throw new BmosException(MesResponseCode.PROCESS_CONFIG_ERROR,vo.getProductFormulaName());
        }
        //产线配置
        List<ProcessProductionLine> lines = lineMapper.selectByProcessVersionId(processVersion.getId());
        ResponseInfo<List<FactoryLineFeignVO>> lineList = factoryFeign.queryLineListByLineIds(CollectionUtils.convertList(lines, ProcessProductionLine::getProductionLineId));
        List<FactoryLineFeignVO> notEnableLineList = CollectionUtils.filterList(lineList.getData(), item ->
                BooleanUtil.isFalse(item.getEnable()) || BooleanUtil.isTrue(item.getIsDeleted()));
        if (CollUtil.isNotEmpty(notEnableLineList)){
            List<String> lineName = CollectionUtils.convertList(notEnableLineList, FactoryLineFeignVO::getName);
            throw new BmosException(MesResponseCode.PROCESS_CONFIG_ERROR,StrUtil.join(StrUtil.DASHED,lineName));
        }
        //批记录
        List<ProcessBatchRecordRelation> relations =
                processBatchRecordRelationService.getListByProcessVersionId(processVersion.getId());
        List<BatchRecordVersion> versions = versionMapper.selectDeleteListByIds(CollectionUtils.convertSet(relations, ProcessBatchRecordRelation::getBatchRecordVersionId));
        List<BatchRecordVersion> cancelVersion = CollectionUtils.filterList(versions, item ->
                item.getState().equals(RecordStateEnum.CANCEL.getValue()) || BooleanUtil.isTrue(item.getDeleted()));
        if (CollUtil.isNotEmpty(cancelVersion)){
            List<String> version = CollectionUtils.convertList(cancelVersion, BatchRecordVersion::getRecordName);
            throw new BmosException(MesResponseCode.PROCESS_CONFIG_ERROR,StrUtil.join(StrUtil.DASHED,version));
        }
        //工序--负责人
        List<PlatformRoleVO> roleVOList = procedureModelService.getRoleListByProcessVersionId(processVersion.getId());
        if (CollUtil.isNotEmpty(roleVOList)){
            List<Long> roleIds = CollectionUtils.convertList(roleVOList, PlatformRoleVO::getId);
            List<String> procedureModelName = CollectionUtils.convertList(models, ProcedureModel::getName, item -> roleIds.contains(item.getPrincipal()));
            List<String> roleNameList = CollectionUtils.convertList(roleVOList, PlatformRoleVO::getRoleName);
            throw new BmosException(MesResponseCode.PROCEDURE_CONFIG_ERROR,StrUtil.join(StrUtil.DASHED,procedureModelName),StrUtil.join(StrUtil.DASHED,roleNameList));
        }
        //房间
        List<ProcessConfigVO> roomIdList = procedureModelService.getRoomListByProcessVersionId(processVersion.getId());
        this.validateRoomId(CollectionUtils.convertList(roomIdList,ProcessConfigVO::getConfigId));
        //班组--工序配置
        List<ProcessConfigVO> team = procedureModelService.getTeamByProcessVersionId(processVersion.getId());
        List<Long> teamId = CollectionUtils.convertList(team, ProcessConfigVO::getConfigId);
        List<ProductPlanTeamListVO> notStatusVos = this.validateTeamId(teamId);
        if (CollUtil.isNotEmpty(notStatusVos)){
            List<String> teamName = CollectionUtils.convertList(notStatusVos, ProductPlanTeamListVO::getName);
            List<Long> procedureModelId = CollectionUtils.convertList(team, ProcessConfigVO::getDataId, item ->
                    CollectionUtils.convertList(notStatusVos, ProductPlanTeamListVO::getId).contains(item.getConfigId()));
            List<String> modelName = CollectionUtils.convertList(models, ProcedureModel::getName, item -> procedureModelId.contains(item.getId()));
            throw new BmosException(MesResponseCode.PROCEDURE_CONFIG_ERROR,StrUtil.join(StrUtil.DASHED,modelName),StrUtil.join(StrUtil.DASHED,teamName));
        }
        //班组--工步配置
        List<ProcedureStepRole> stepTeamIds = procedureStepModelService.getStepTeamIdByProcessIdAndVersion(processVersion.getProcessId(),
                processVersion.getVersion());
        List<ProductPlanTeamListVO> stepNotStatusVos = this.validateTeamId(CollectionUtils.convertList(stepTeamIds, ProcedureStepRole::getRoleId));
        if (CollUtil.isNotEmpty(stepNotStatusVos)){
            List<String> teamName = CollectionUtils.convertList(stepNotStatusVos, ProductPlanTeamListVO::getName);
            List<Long> teamIds = CollectionUtils.convertList(stepNotStatusVos, ProductPlanTeamListVO::getId);
            List<Long> stepModelId = CollectionUtils.convertList(stepTeamIds, ProcedureStepRole::getProcedureStepId, item ->
                    teamIds.contains(item.getRoleId()));
            List<String> stepModelName = CollectionUtils.convertList(stepModelList, ProcedureStepModel::getName, model ->
                    stepModelId.contains(model.getId()));
            throw new BmosException(MesResponseCode.PROCEDURE_CONFIG_ERROR,StrUtil.join(StrUtil.DASHED,stepModelName),
                    StrUtil.join(StrUtil.DASHED,teamName));
        }
        List<String> conditionList = expressionService.getStepModelCondition(CollectionUtils.convertList(stepModelList, ProcedureStepModel::getId),
                Arrays.asList(ConditionTypeEnum.ROOM_STATE.getValue(), ConditionTypeEnum.EQUIPMENT_USE_STATE.getValue()));
        if (CollUtil.isNotEmpty(conditionList)){
            List<ConditionDetailVO> conditionDetailList = conditionList.stream().map(item -> {
                return JsonUtils.parseObject(item, ConditionDetailVO.class);
            }).collect(Collectors.toList());
            List<Long> equipmentIds = CollectionUtils.convertList(conditionDetailList, ConditionDetailVO::getEquipmentId,
                    item -> ObjectUtil.isNotNull(item.getEquipmentId()));
            //工步条件中的设备信息
            ResponseInfo<List<EquipmentVO>> deleteEquipment = equipmentConfigFeign.getDeleteEquipment(equipmentIds);
            List<EquipmentVO> equipmentVOList = CollectionUtils.filterList(deleteEquipment.getData(), item ->
                    BooleanUtil.isFalse(item.getEnable()) || BooleanUtil.isTrue(item.getDeleted()));
            if (CollUtil.isNotEmpty(equipmentVOList)){
                throw new BmosException(MesResponseCode.PROCESS_CONFIG_ERROR,
                        StrUtil.join(StrUtil.DASHED,CollectionUtils.convertList(equipmentVOList,EquipmentVO::getName)));
            }
            //工步条件中的房间信息
            List<Long> roomIds = CollectionUtils.convertList(conditionDetailList, ConditionDetailVO::getRoomId,
                    item -> ObjectUtil.isNotNull(item.getRoomId()));
            this.validateRoomId(roomIds);
        }
        //工序配置物料信息
        List<Long> materialIds = materialService.getByProcedureModelId(procedureModelIdList);
        if (CollUtil.isNotEmpty(materialIds)){
            List<ProductFormulaMaterial> materialList = materialMapper.selectListByDisabledIds(materialIds);
            List<String> materialName = CollectionUtils.convertList(materialList, ProductFormulaMaterial::getMaterialName, item ->
                    !item.getVersionId().equals(processVersion.getProductFormulaVersionId()));
            if (CollUtil.isNotEmpty(materialName)) {
                throw new BmosException(MesResponseCode.PROCESS_CONFIG_ERROR,
                        StrUtil.join(StrUtil.DASHED, materialName));
            }
        }
        //条件中配置的物料信息
        List<ProcedureCondition> materialConditionList = expressionService.selectMaterialConditionListByStepModelId(CollectionUtils.convertList(stepModelList,
                ProcedureStepModel::getId), ConditionTypeEnum.MATERIAL_RESERVE_NUMBER.getValue());
        if (CollUtil.isNotEmpty(materialConditionList)){
            List<Long> detailList = CollectionUtils.convertList(materialConditionList,ProcedureCondition::getConditionDetails)
                    .stream().map(item -> {
                return JsonUtils.parseObject(item, ConditionDetailVO.class).getMaterialId();
            }).collect(Collectors.toList());

            //获取配方物料信息
            List<ProductFormulaMaterial> materialList = materialMapper.selectListByDisabledIds(detailList);
            Map<Long, ProcedureModel> modelMap = CollectionUtils.convertMap(models, ProcedureModel::getId);
            //获取工序配置物料信息
            List<ProcedureModelMaterialVO> materialListByProcedureModelIds = materialService.getMaterialListByProcedureModelIds(procedureModelIdList);
            Map<Long, List<ProcedureModelMaterialVO>> modelMaterialMap = CollectionUtils.convertMultiMap(materialListByProcedureModelIds, ProcedureModelMaterialVO::getProcedureModelId);
            Map<Long, ProcedureStepModel> stepModelMap = CollectionUtils.convertMap(stepModelList, ProcedureStepModel::getId);
            Map<Long, List<String>> materialMap = CollectionUtils.convertMultiMap(materialConditionList,
                    ProcedureCondition::getProcedureStepModelId,ProcedureCondition::getConditionDetails);
            materialMap.forEach((stepModelId,conditions)->{
                ProcedureStepModel model = stepModelMap.get(stepModelId);
                ProcedureModel procedureModel = modelMap.get(model.getProcedureModelId());
                List<ProcedureModelMaterialVO> vos = modelMaterialMap.get(procedureModel.getId());
                List<Long> materialIdList = conditions.stream().map(item -> {
                    return JsonUtils.parseObject(item, ConditionDetailVO.class).getMaterialId();
                }).collect(Collectors.toList());
                if (CollUtil.isEmpty(vos)){
                    throw new BmosException(MesResponseCode.PROCEDURE_CONFIG_ERROR,procedureModel.getName() + StrUtil.DASHED + model.getName(),
                            StrUtil.join(StrUtil.DASHED,CollectionUtils.convertList(materialList,ProductFormulaMaterial::getMaterialName,
                                    item->materialIdList.contains(item.getId()))));
                }
                List<Long> materialId = CollectionUtils.convertList(vos, ProcedureModelMaterialVO::getProductFormulaMaterialId);
                List<Long> disableId = CollectionUtils.filterList(materialIdList, item -> !materialId.contains(item));
                if (CollUtil.isNotEmpty(disableId)){
                    throw new BmosException(MesResponseCode.PROCEDURE_CONFIG_ERROR,procedureModel.getName() + StrUtil.DASHED + model.getName(),
                            StrUtil.join(StrUtil.DASHED,CollectionUtils.convertList(materialList,ProductFormulaMaterial::getMaterialName,
                                    item->disableId.contains(item.getId()))));
                }
            });
        }
    }

    /**
     * 校验班组
     * @param teamId 班组id
     * @return
     */
    private List<ProductPlanTeamListVO> validateTeamId(List<Long> teamId){
        if (CollUtil.isEmpty(teamId)){
            return new ArrayList<>();
        }
        List<ProductPlanTeamListVO> productPlanTeamList = productPlanTeamMapper.selectListByIds(teamId);
        return CollectionUtils.filterList(productPlanTeamList, item ->
                BooleanUtil.isFalse(item.getStatus()) || BooleanUtil.isTrue(item.getIsDeleted()));
    }

    /**
     * 校验房间
     * @param roomIdList 房间id集合
     */
    private void validateRoomId(List<Long> roomIdList){
        if (CollUtil.isEmpty(roomIdList)){
            return;
        }
        ResponseInfo<List<FactoryRoomFeignVO>> listResponseInfo = factoryFeign.queryRoomListByRoomIds(roomIdList);
        //删除或者未启用的
        List<FactoryRoomFeignVO> roomVos = CollectionUtils.filterList(listResponseInfo.getData(), item -> BooleanUtil.isFalse(item.getEnable()) ||
                BooleanUtil.isTrue(item.getIsDeleted()));
        if (CollUtil.isNotEmpty(roomVos)){
            List<String> roomName = CollectionUtils.convertList(roomVos, FactoryRoomFeignVO::getName);
            throw new BmosException(MesResponseCode.PROCESS_CONFIG_ERROR,StrUtil.join(StrUtil.DASHED,roomName));
        }
    }

    private void validateCondition(Long processVersionId) {
        List<ProcedureModel> procedureModels = procedureModelService.getListByProcessVersionId(processVersionId);
        if (CollUtil.isEmpty(procedureModels)) {
            return;
        }
        List<ExpressionDetailVO> expressionDetail = expressionService.selectByModelId(CollectionUtils.convertList(procedureModels, ProcedureModel::getId), null);
        if (CollUtil.isEmpty(expressionDetail)) {
            return;
        }
        Map<Long, ProcedureModel> modelMap = CollectionUtils.convertMap(procedureModels, ProcedureModel::getId);
        ProcedureModel first = CollectionUtils.getFirst(procedureModels);
        List<ProcedureStepModel> stepModelList = procedureStepModelService.getStepModelByProcessIdAndVersionAndNodeIdList(first.getProcessId(),first.getProcessVersion());
        Map<Long, ProcedureStepModel> stepModelMap = CollectionUtils.convertMap(stepModelList, ProcedureStepModel::getId);
        List<Long> stepModelIdList = CollectionUtils.convertList(stepModelList, ProcedureStepModel::getId);
        expressionDetail.forEach(item -> {
            ProcedureModel procedureModel = modelMap.get(item.getProcedureModelId());
            if (ObjectUtil.isEmpty(procedureModel)) {
                return;
            }
            item.getConditionList().forEach(conditionItem -> {
                if (!conditionItem.getConditionType().equals(ConditionTypeEnum.STEP_NODE_COMPLETE.getValue()) &&
                        !conditionItem.getConditionType().equals(ConditionTypeEnum.TASK_NODE_COMPLETE.getValue())){
                    return;
                }
                if (!stepModelIdList.contains(conditionItem.getTaskNodeId()) && !stepModelIdList.contains(conditionItem.getStepId())){
                    if (item.getExpressionNodeType().equals(NodeTypeEnum.PROCEDURE.getValue())){
                        throw new BmosException(MesResponseCode.PROCESS_PROCEDURE_CONDITION_ERROR,procedureModel.getName(),conditionItem.getName());
                    }
                    ProcedureStepModel model = stepModelMap.get(conditionItem.getProcedureStepModelId());
                    throw new BmosException(MesResponseCode.PROCESS_STEP_CONDITION_ERROR,procedureModel.getName(),model.getName(),conditionItem.getName());
                }
            });
        });


    }

    private void validateBatchRecordItem(Long processVersionId, Long processId, String processVersion) {
        List<ProcessBatchRecordRelation> processBatchRecordList = processBatchRecordRelationService.getListByProcessVersionId(processVersionId);
        List<ProcedureModel> modelList = procedureModelService.getByProcessIdAndVersion(processId, processVersion);
        List<Long> modelIds = CollectionUtils.convertList(modelList, ProcedureModel::getId);
        List<ProcessRecordItemVO> recordItemVOList = procedureStepModelService.queryRecordVersionIdByProcessId(processId, processVersion, modelIds);
        if (CollUtil.isEmpty(recordItemVOList)) {
            throw new BmosException(MesResponseCode.PROCEDURE_EXIST_EMPTY_GRAPH);
        }
        //筛选出工艺换班的数据
        List<Long> modelIdS = CollectionUtils.convertList(recordItemVOList, ProcessRecordItemVO::getProcedureModelId,
                item -> ProcedureStepNodeFunctionEnum.changeTeamFlag(item.getNodeFunction()));
        //筛选出配置了记录页的节点
        recordItemVOList = CollectionUtils.filterList(recordItemVOList, item -> !ProcedureStepNodeFunctionEnum.notRecordNode(item.getNodeFunction()));
        //校验工序更换记录版本后是否更改工步
        Map<Long, List<ProcessRecordItemVO>> modelMap = CollectionUtils.convertMultiMap(recordItemVOList, ProcessRecordItemVO::getProcedureModelId);
        modelIds.forEach(item -> {
            //当前工序下不包含工步并且节点不是换班节点
            if (CollUtil.isEmpty(modelMap.get(item)) && !modelIdS.contains(item)) {
                throw new BmosException(MesResponseCode.PROCEDURE_EXIST_EMPTY_GRAPH);
            }
        });
        List<Long> recordVersionIdList = CollectionUtils.convertList(processBatchRecordList, ProcessBatchRecordRelation::getBatchRecordVersionId);
        //校验记录版本
        List<BatchRecordItem> itemList = batchRecordItemService.queryItemListByVersionIdList(recordVersionIdList);
        Map<Long, List<BatchRecordItem>> itemMap = CollectionUtils.convertMultiMap(itemList, BatchRecordItem::getRecordVersionId);
        Map<Long, List<ProcessRecordItemVO>> recordVersionMap = CollectionUtils.convertMultiMap(recordItemVOList, ProcessRecordItemVO::getRecordVersionId);
        recordVersionMap.forEach((key, value) -> {
            List<BatchRecordItem> list = itemMap.get(key);
            if (CollUtil.isEmpty(list)) {
                ProcessRecordItemVO first = CollectionUtils.getFirst(value);
                throw new BmosException(MesResponseCode.PROCEDURE_ITEM_ERROR, first.getProcedureName(), first.getName());
            }
            List<Long> itemIdList = CollectionUtils.convertList(list, BatchRecordItem::getItemId);
            //判断记录项
            value.forEach(item -> {
                if (!itemIdList.contains(item.getRecordItemId())) {
                    throw new BmosException(MesResponseCode.PROCEDURE_ITEM_ERROR, item.getProcedureName(), item.getName());
                }
            });
        });
    }

    private void validateRequiredConfig(Long processId, String version) {
        List<ProcedureStepModel> procedureStepModelList = procedureStepModelService.getStepModelByProcessIdAndVersion(processId, version);
        List<ProcedureStepConfig> configList = procedureStepConfigService.getListByProcessVersion(processId, version);
        List<BatchRecordComponent> componentList = batchRecordComponentService.getProcedureStepListRequiredConfigList(procedureStepModelList);
        Map<Long, List<BatchRecordComponent>> componentMap = CollectionUtils.convertMultiMap(componentList, BatchRecordComponent::getRecordItemId);
        Map<Long, List<ProcedureStepConfig>> configItemMap = CollectionUtils.convertMultiMap(configList, ProcedureStepConfig::getRecordItemId);
        Map<Long, List<ProcedureStepConfig>> configStepMap = CollectionUtils.convertMultiMap(configList, ProcedureStepConfig::getProcedureStepModelId);
        List<BusinessComponentTypeEnum> requiredConfigEnums = BusinessComponentTypeEnum.getRequiredConfigEnums();
        Map<String, BusinessComponentTypeEnum> enumMap = CollectionUtils.convertMap(requiredConfigEnums, BusinessComponentTypeEnum::getValue);
        for (ProcedureStepModel procedureStepModel : procedureStepModelList) {
            // 表单中必须配置的组件列表
            List<BatchRecordComponent> batchRecordComponents = componentMap.get(procedureStepModel.getRecordItemId());
            if (CollUtil.isEmpty(batchRecordComponents)) {
                continue;
            }
            List<ProcedureStepConfig> stepConfigList;

            if (procedureStepModel.getReusable()) {
                // 复用配置列表
                stepConfigList = Optional.ofNullable(configItemMap.get(procedureStepModel.getRecordItemId()))
                        .orElse(new ArrayList<>()).stream().filter(e -> e.getProcedureStepModelId() == 0).collect(Collectors.toList());
            } else {
                stepConfigList = Optional.ofNullable(configStepMap.get(procedureStepModel.getId())).orElse(new ArrayList<>());
            }
            Map<Long, ProcedureStepConfig> configMap = CollectionUtils.convertMap(stepConfigList, ProcedureStepConfig::getComponentId);

            for (BatchRecordComponent batchRecordComponent : batchRecordComponents) {
                ProcedureStepConfig procedureStepConfig = configMap.get(batchRecordComponent.getId());
                if (procedureStepConfig == null || StrUtil.isBlank(procedureStepConfig.getConfigInfo())) {
                    ProcedureModel procedureModel = procedureModelService.getById(procedureStepModel.getProcedureModelId());
                    throw new BmosException(MesResponseCode.PROCEDURE_STEP_CONFIG_REQUIRED, procedureModel.getName(),
                            procedureStepModel.getName(), batchRecordComponent.getComponentName());
                }
                JSONObject json = JSONUtil.parseObj(procedureStepConfig.getConfigInfo());
                BusinessComponentTypeEnum typeEnum = enumMap.get(batchRecordComponent.getComponentType());
                String[] requiredProperties = typeEnum.getRequiredProperties();
                // 校验所属属性
                for (String requiredProperty : requiredProperties) {
                    Object propertyObj = json.get(requiredProperty);
                    if (propertyObj == null) {
                        ProcedureModel procedureModel = procedureModelService.getById(procedureStepModel.getProcedureModelId());
                        throw new BmosException(MesResponseCode.PROCEDURE_STEP_CONFIG_REQUIRED, procedureModel.getName(),
                                procedureStepModel.getName(), batchRecordComponent.getComponentName());
                    }
                    if (propertyObj instanceof JSONArray) {
                        JSONArray array = (JSONArray) propertyObj;
                        if (CollUtil.isEmpty(array)) {
                            ProcedureModel procedureModel = procedureModelService.getById(procedureStepModel.getProcedureModelId());
                            throw new BmosException(MesResponseCode.PROCEDURE_STEP_CONFIG_REQUIRED, procedureModel.getName(),
                                    procedureStepModel.getName(), batchRecordComponent.getComponentName());
                        }
                    }
                }
            }
        }
    }
}
