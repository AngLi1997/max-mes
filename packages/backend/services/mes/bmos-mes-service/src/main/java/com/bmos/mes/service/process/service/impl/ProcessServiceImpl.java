package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.audit.engine.core.model.AuditProcessInstance;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.audit.engine.core.query.service.AuditProcessInstanceQueryService;
import com.bmos.audit.engine.core.state.ProcessState;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.constant.ProcessConstant;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.common.enums.process.ActionStateEnum;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import com.bmos.mes.service.audit.dto.FlowStartDTO;
import com.bmos.mes.service.audit.service.FlowAuditService;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.aspect.OperationHistoryContext;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import com.bmos.mes.service.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mes.service.plan.production.vo.BuildPlanBatchNextNoVO;
import com.bmos.mes.service.plan.template.service.impl.PlanTemplateServiceImpl;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.convert.*;
import com.bmos.mes.service.process.convert.Task.ProcedureConditionConverter;
import com.bmos.mes.service.process.convert.Task.ProcedureExpressionConverter;
import com.bmos.mes.service.process.dto.*;
import com.bmos.mes.service.process.dto.modify.ProcessCopyDTO;
import com.bmos.mes.service.process.dto.modify.ProcessModifyDTO;
import com.bmos.mes.service.process.dto.modify.ProcessSaveVersionDTO;
import com.bmos.mes.service.process.dto.modify.ProcessVersionChangeStateDTO;
import com.bmos.mes.service.process.dto.query.*;
import com.bmos.mes.service.process.dto.save.ProcessRecordOrderSaveDTO;
import com.bmos.mes.service.process.dto.save.ProcessRelationDTO;
import com.bmos.mes.service.process.dto.save.ProcessRelationSaveDTO;
import com.bmos.mes.service.process.dto.save.ProcessSaveDTO;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.bmos.mes.service.process.mapper.*;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.service.*;
import com.bmos.mes.service.process.service.impl.copy.CopyContext;
import com.bmos.mes.service.process.service.impl.copy.CopyProcedure;
import com.bmos.mes.service.process.service.impl.copy.CopyProcedureStep;
import com.bmos.mes.service.process.service.impl.copy.CopyProcessVersion;
import com.bmos.mes.service.process.service.task.ProcedureConditionService;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;
import com.bmos.mes.service.process.vo.Task.TaskOrStepIdHandelVO;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.model.ProductMaterialCategory;
import com.bmos.mes.service.product.service.ProductMaterialCategoryService;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.product.vo.ProductCategoryTreeNodeVO;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.model.BatchRecordVersion;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.service.BatchRecordVersionService;
import com.bmos.mes.service.workflow.service.WorkflowService;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.orchestrator.engine.core.command.CreateDeploymentCmd;
import com.bmos.platform.facade.factory.dto.LineUseDTO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import com.bmos.platform.facade.factory.vo.LineModuleTreeNodeFeignVO;
import com.bmos.platform.facade.factory.vo.RoomInfoFeignVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private ProcessVersionService processVersionService;

    @Autowired
    private ProcessBatchRecordRelationService processBatchRecordRelationService;

    @Autowired
    private ProcedureModelService procedureModelService;

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private ProcedureStepService procedureStepService;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private ProcedureStepRoleRelationService procedureStepRoleRelationService;

    @Autowired
    private ResourcePermissionService resourcePermissionService;

    @Autowired
    private ProductMaterialCategoryService productMaterialCategoryService;

    @Autowired
    private ProductMaterialService productMaterialService;

    @Autowired
    private ProcedureModelGroupService procedureModelGroupService;

    @Autowired
    private ProcessRelationService processRelationService;

    @Autowired
    private ProcessRelationMaterialService processRelationMaterialService;

    @Autowired
    private ProcessRecordOrderService processRecordOrderService;


    @Autowired
    private FlowAuditService flowAuditService;

    @Autowired
    private OperationHistoryService operationHistoryService;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    private ProcedureModelMaterialService procedureModelMaterialService;

    @Autowired
    private ProcedureModelRoomMapper procedureModelRoomMapper;

    @Autowired
    private FactoryFeign factoryFeign;

    @Autowired
    private BatchRecordComponentService batchRecordComponentService;

    @Autowired
    private BatchRecordVersionService recordVersionService;


    @Autowired
    private ProcedureExpressionService expressionService;

    @Autowired
    private ProcedureConditionService conditionService;

    @Autowired
    private ProcessProductionLineMapper processProductionLineMapper;

    @Autowired
    private AuditProcessInstanceQueryService instanceQueryService;

    @Resource
    private PlanTemplateServiceImpl planTemplateService;

    @Resource
    private ProcessFormulaRelationService processFormulaRelationService;

    @Resource
    private IProcessDashboardConfigMapper processDashboardConfigMapper;

    @Resource
    private ProcessDashboardConfigDataMapper processDashboardConfigDataMapper;

    @Resource
    private ProcedureStepSopService sopService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.PROCESS, operationType = OperationType.SAVE, businessId = "#getId")
    public ProcessVersionVO save(ProcessSaveDTO dto) {

        // 校验名称
        validateName(dto.getName());
        //保存工艺
        Process process = ProcessConverter.INSTANCE.convert(dto);
        processMapper.insert(process);
        //保存数据权限
        resourcePermissionService
                .save(ResourcePermissionSaveDTO.builder().resourceId(process.getId()).deptIds(dto.getDeptIds()).build());
        //保存版本
        ProcessVersion processVersion = ProcessVersionConverter.INSTANCE.build(process.getId(), dto);
        //创建流程
        CreateDeploymentCmd cmd = ProcessConverter.INSTANCE.convertDeployment(dto, processVersion.getVersion());
        processVersion.setProcessModelId(workflowService.createDeployment(cmd));
        processVersionService.save(processVersion);
        OperationHistoryContext.putVariable(processVersion, ProcessVersion::getId);
        //保存关联的批记录
        List<ProcessBatchRecordRelation> relations =
                ProcessBatchRecordRelationConverter.INSTANCE.convert(dto, processVersion);
        processBatchRecordRelationService.saveBatch(relations);
        //保存关联的产线
        processProductionLineMapper.insertBatch(ProcessConverter.INSTANCE.convertToProcessProductionLine(processVersion, dto.getProductionLineIds()));
        //保存工序
        saveProcedures(processVersion, dto.getProcedures());
        return ProcessVersionVO.builder().processId(process.getId())
                .processVersionId(processVersion.getId())
                .version(processVersion.getVersion()).build();
    }


    private void saveProcedures(ProcessVersion processVersion, List<ProcedureDTO> procedureDTOS) {
        //先保存未在数据库工工序
        List<ProcedureDTO> notInDB = procedureDTOS.stream().filter(e -> ObjectUtil.isNull(e.getProcedureId())).collect(Collectors.toList());
        List<Procedure> procedures = procedureService.saveBatch(ProcedureConverter.INSTANCE.convertList(processVersion, notInDB));
        Map<String, Long> procedureIdMap = CollectionUtils.convertMap(procedures, Procedure::getName, Procedure::getId);
        List<ProcedureModel> procedureModels = ProcedureModelConverter.INSTANCE.convertList(processVersion, procedureDTOS, procedureIdMap,0);
        procedureModels.forEach(item-> {
            if (ObjectUtil.isNull(item.getId())){
                item.setId(IdUtils.getSnowflake());
            }
        });
        //保存表达式以及条件
        expressionService.saveProcedureExpression(procedureModels);
        //保存工序与模型的关联
        procedureModelService.saveBatch(procedureModels);

        //保存工序班组
        List<ProcedureModelGroup> groups = ProcedureConverter.INSTANCE.convertGroupList(procedureModels);
        procedureModelGroupService.saveBatch(groups);
        //保存工序与配方物料关联
        List<ProcedureModelMaterial> materials = ProcedureConverter.INSTANCE.convertMaterialList(procedureModels);
        procedureModelMaterialService.saveBatch(materials);
        // 保存工序与房间的关系
        List<ProcedureModelRoom> rooms = ProcedureConverter.INSTANCE.convertRoomList(procedureModels);
        procedureModelRoomMapper.insertBatch(rooms);
    }

    private void validateName(String name) {
        if (processMapper.existsName(name)) {
            throw new BmosException(MesResponseCode.PROCESS_NAME_EXIST);
        }
    }

    @Override
    public CommonPage<ProcessVersionPageVO> getVersionPage(ProcessVersionPageQueryDTO dto) {
        return processVersionService.getPage(dto);
    }

    @Override
    public CommonPage<ProcessPageVO> getPage(ProcessPageQueryDTO dto) {
        List<Long> categoryIds = null;
        if (ObjectUtil.isNotNull(dto.getProductCategoryId())) {
            categoryIds = productMaterialCategoryService.getAllChildCategory(dto.getProductCategoryId());
        }
        dto.setCategoryIds(categoryIds);
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
        }
        dto.setDeptIds(deptIds);
        List<ProcessPageVO> list = processMapper.selectPageList(dto);
        return CommonPage.convertPage(list);
    }

    @Override
    public List<ProcessListItemVO> getList(ProcessListQueryDTO dto) {
        return ProcessConverter.INSTANCE.convertList(processMapper.selectCustomList(dto,Collections.emptyList()));
    }

    @Override
    public List<ProcessListItemVO> getRelationProcessList(ProcessRelationQueryDTO dto) {
        List<Process> processes = processMapper.selectRelationList(dto);
        return ProcessConverter.INSTANCE.convertList(processes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeProcessVersionState(ProcessVersionChangeStateDTO dto) {
        ProcessVersion processVersion = processVersionService.updateState(dto);
        if (ObjectUtil.isNull(processVersion)) {
            return;
        }
       //停用、生效状态下通知平台记录设备、工位、产线使用次数
        if (StrUtil.equals(dto.getActionState(),ActionStateEnum.VALID.getValue()) ||
                StrUtil.equals(dto.getActionState(),ActionStateEnum.INVALID.getValue())){
            processMapper.updateVersion(processVersion.getProcessId(), StrUtil.equals(dto.getActionState(),ActionStateEnum.VALID.getValue())  ?
                    processVersion.getVersion() : null);
            updateRoomAndLine(StrUtil.equals(dto.getActionState(),ActionStateEnum.VALID.getValue()),processVersion);
            planTemplateService.updateTemplateConfirmStatus(Collections.singletonList(processMapper.selectById(processVersion.getProcessId())));
        }

    }

    private void updateRoomAndLine(Boolean state,ProcessVersion processVersion){
        List<ProcessProductionLine> lineList =
                processProductionLineMapper.selectByProcessVersionId(processVersion.getId());
        LineUseDTO lineUseDTO = new LineUseDTO();
        HashMap<Long, Boolean> lineUseMap = new HashMap<>();
        lineUseDTO.setLineUseMap(lineUseMap);
        HashMap<Long, Boolean> roomUseMap = new HashMap<>();
        lineUseDTO.setRoomUseMap(roomUseMap);
        HashMap<Long, Boolean> stationUseMap = new HashMap<>();
        lineUseDTO.setStationUseMap(stationUseMap);

        // 产线
        lineList.forEach(e -> {
            lineUseMap.put(e.getProductionLineId(), state);
        });
        List<ProcedureModel> procedureModels =
                procedureModelService.getByProcessIdAndVersion(processVersion.getProcessId(),
                        processVersion.getVersion());

        // 工序房间
        List<ProcedureModelRoom> rooms =
                procedureModelRoomMapper.selectByProcedureModelIds(CollectionUtils.convertSet(procedureModels,
                        ProcedureModel::getId));
        rooms.forEach(e -> {
            roomUseMap.put(e.getRoomId(),state);
        });
        List<ProcedureStepConfig> configs =
                procedureStepConfigService.getListByProcessVersion(processVersion.getProcessId(),
                        processVersion.getVersion());

        // 配置房间
        List<Long> roomIds = configs.stream().map(e -> {
            JSONObject jsonObject = JSONUtil.parseObj(e.getConfigInfo());
            JSONArray jsonArray = jsonObject.getJSONArray(ProcessConstant.roomField);
            if (jsonArray == null) {
                return new ArrayList<Long>();
            }
            return jsonArray.toList(Long.class);
        }).flatMap(List::stream).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        roomIds.forEach(e -> {
            roomUseMap.put(e,state);
        });

        // 工位
        List<Long> stationIds = configs.stream().map(e -> {
            JSONObject jsonObject = JSONUtil.parseObj(e.getConfigInfo());
            JSONArray jsonArray = jsonObject.getJSONArray(ProcessConstant.stationField);
            if (jsonArray == null) {
                return new ArrayList<Long>();
            }
            return jsonArray.toList(Long.class);
        }).flatMap(List::stream).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        stationIds.forEach(e -> {
            stationUseMap.put(e, state);
        });
        FeignUtils.handleRequest(data -> factoryFeign.bindUseCount(data), lineUseDTO);
    }

    @Override
    public ProcessDetailVO getDetail(ProcessDetailQueryDTO dto) {
        //工艺
        Process process = processMapper.selectById(dto.getProcessId());
        if (ObjectUtil.isNull(process)) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
        }
        //工艺版本
        ProcessVersion processVersion = processVersionService.getByProcessIdAndVersion(dto);
        if (ObjectUtil.isNull(processVersion)) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_NOT_EXIST);
        }
        //批记录
        List<ProcessBatchRecordRelation> relations =
                processBatchRecordRelationService.getListByProcessVersionId(processVersion.getId());

        ProcessDetailVO detail = ProcessConverter.INSTANCE.convert(process, processVersion, relations);
        //工序
        List<ProcedureModel> procedureModels =
                procedureModelService.getByProcessIdAndVersion(process.getId(), processVersion.getVersion());
        //工序班组
        Map<Long, List<Long>> groupMap =
                procedureModelGroupService.getByProcedureModelIds(CollectionUtils.convertSet(procedureModels, ProcedureModel::getId));
        //工序物料
        Map<Long, List<Long>> materialMap = procedureModelMaterialService.getByProcedureModelIds(CollectionUtils.convertSet(procedureModels, ProcedureModel::getId));
        // 工序房间
        List<ProcedureModelRoom> rooms = procedureModelRoomMapper.selectByProcedureModelIds(CollectionUtils.convertSet(procedureModels, ProcedureModel::getId));
        Map<Long, List<String>> roomMap = CollectionUtils.convertMultiMap(rooms, ProcedureModelRoom::getProcedureModelId, ProcedureModelRoom::getRoomIdPath);
        //工序完成条件配置
        List<ExpressionDetailVO> expressionDetail = expressionService.selectByModelId(CollectionUtils.convertList(procedureModels, ProcedureModel::getId),
                NodeTypeEnum.PROCEDURE.getValue());
        Map<Long, ExpressionDetailVO> expressionMap = CollectionUtils.convertMap(expressionDetail, ExpressionDetailVO::getProcedureModelId);
        // 历史名称查询
        List<Procedure> procedures = procedureService.selectByIds(CollectionUtils.convertList(procedureModels, ProcedureModel::getProcedureId));
        Map<Long, Procedure> procedureMap = CollectionUtils.convertMap(procedures, Procedure::getId);
        detail.setProcedures(ProcedureModelConverter.INSTANCE.convertVOList(procedureModels, groupMap, materialMap, roomMap,expressionMap, procedureMap));

        List<ProcessProductionLine> lines = processProductionLineMapper.selectByProcessVersionId(processVersion.getId());
        detail.setProcessModelId(processVersion.getProcessModelId());
        detail.setProcessVersionId(processVersion.getId());
        detail.setProductionStageCode(processVersion.getProductionStageCode());
        detail.setProductionLineIds(CollectionUtils.convertList(lines, ProcessProductionLine::getProductionLineId));
        return detail;
    }

    @Override
    public List<ProcessRelationVO> getProcessRelation(Long processId) {
        List<ProcessRelation> processRelations = processRelationService.getListByProcessId(processId);
        Set<Long> ids = CollectionUtils.convertSet(processRelations, ProcessRelation::getId);
        List<ProcessRelationMaterial> relationMaterials = processRelationMaterialService.getListByProcessRelationIds(ids);
        return ProcessConverter.INSTANCE.convertRelations(processRelations, relationMaterials);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProcessRelation(ProcessRelationSaveDTO dto) {
        //更新工艺关联
        processRelationService.deleteByProcessId(dto.getProcessId());
        processRelationMaterialService.deleteByProcessId(dto.getProcessId());
        List<ProcessRelation> relationsInDB = processRelationService.getList();

        if (CollUtil.isNotEmpty(dto.getRelations())) {
            List<ProcessRelation> relations = new ArrayList<>();
            List<ProcessRelationMaterial> relationMaterials = new ArrayList<>();
            buildRelations(dto, relations, relationMaterials);
            relationsInDB.addAll(relations);
            validateCircularDependency(dto.getProcessId(), relationsInDB);
            processRelationService.saveBatch(relations);
            processRelationMaterialService.saveBatch(relationMaterials);
        }
    }

    @Override
    public List<String> getAuditBusinessKey(List<Long> deptIdList) {
        return processMapper.getAuditBusinessKey(deptIdList);
    }

    @Override
    public List<Long> getIdListByDeptIds() {
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)){
            return Collections.emptyList();
        }
        return processMapper.getIdListByDeptIds(deptIds);
    }

    @Override
    public List<ProductLineVO> getProductLine() {
        try {
            ResponseInfo<List<FactoryLineFeignVO>> lineByCondition = factoryFeign.getLineByCondition(StrUtil.EMPTY);
            return ProcedureConverter.INSTANCE.convert2ProductLineVO(lineByCondition.getData());
        } catch (Exception e) {
            throw new BmosException(MesResponseCode.PLATFORM_GET_SYNC_ERROR);
        }

    }

    @Override
    public List<ProductLineRoomVO> getLineRoom(Long lineId) {
        try {
            ResponseInfo<List<RoomInfoFeignVO>> roomInfoByLineId = factoryFeign.getRoomInfoByLineId(lineId, false);
            return ProcedureConverter.INSTANCE.convert2ProductLineRoomVO(roomInfoByLineId.getData());
        } catch (Exception e) {
            throw new BmosException(MesResponseCode.PLATFORM_GET_SYNC_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProcessVersionActionState() {
        List<ProcessVersion> versions = processVersionService.updateProcessVersionActionState(ActionStateEnum.WAIT_VALID.getValue());
        if (CollUtil.isEmpty(versions)){
            return;
        }
        versions.forEach(e->updateRoomAndLine(true, e));
        Map<Long, ProcessVersion> versionMap = CollectionUtils.convertMap(versions, ProcessVersion::getProcessId);
        //该表工艺中生效版本号
        List<Process> processes = processMapper.selectListByIdS(CollectionUtils.convertList(versions, ProcessVersion::getProcessId));
        processes.forEach(item->item.setActiveVersion(versionMap.get(item.getId()).getVersion()));
        processMapper.saveOrUpdateBatch(processes);
        // 工艺生效版本变化则更新模板确认状态
        planTemplateService.updateTemplateConfirmStatus(processes);
    }

    @Override
    public List<ProcessListItemVO> getInstructionProcessList(ProcessListQueryDTO dto) {
        List<Long> processIdList = new ArrayList<>();
        //新建指令单查询工艺添加数据权限
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())){
            List<Long> processIds = getIdListByDeptIds();
            if (CollUtil.isEmpty(processIds)){
                return Collections.emptyList();
            }
            processIdList.addAll(processIds);
        }
        return ProcessConverter.INSTANCE.convertList( processMapper.selectCustomList(dto,processIdList));
    }

    @Override
    public List<ProcessListItemTreeVO> getListTree(ProcessTreeQueryDTO dto) {
        //获取产品信息
        List<ProductCategoryTreeNodeVO> productTree = productMaterialService.getProductTree(CategoryInfoTypeEnum.PRODUCTION.getValue());
        if (CollUtil.isEmpty(productTree)){
            return Collections.emptyList();
        }
        List<ProcessListItemTreeVO> processListItemTree = BeanUtil.copyToList(productTree, ProcessListItemTreeVO.class);
        ProcessListQueryDTO processListQueryDTO = new ProcessListQueryDTO();
        processListQueryDTO.setActive(BooleanUtil.isTrue(dto.getActiveProcess()) ? true : null);
        List<Long> idListByDeptIds = getIdListByDeptIds();
        if (BooleanUtil.isTrue(dto.getFilterPermission()) && CollUtil.isEmpty(idListByDeptIds)) {
            return processListItemTree;
        }
        List<Process> processList = processMapper.selectCustomList(processListQueryDTO,
                BooleanUtil.isTrue(dto.getFilterPermission()) ? idListByDeptIds : Collections.emptyList());
        if(CollUtil.isEmpty(processList)){
            return processListItemTree;
        }
        //产品下工艺信息
        Map<Long, List<Process>> processProductMap = CollectionUtils.convertMultiMap(processList, Process::getProductId);
        //处理数据标识工艺数据flay为true
        handelProcessListTreeNode(processListItemTree,processProductMap);
        return processListItemTree;
    }

    @Override
    public Set<Long> getByIdList(List<Long> processIdList) {
        List<Process> processes = processMapper.selectBatchIds(processIdList);
        if (CollUtil.isEmpty(processes)){
            return new HashSet<>();
        }
        return processes.stream().map(Process::getProductId).collect(Collectors.toSet());
    }

    @Override
    public List<BuildPlanBatchNextNoVO> selectProductListByProcessIdS(Set<Long> processIdList) {
        return processMapper.selectProductListByProcessIdS(processIdList);
    }

    @Override
    public ProcessDashboardVO getDashBoardConfig(Long processId) {
        ProcessDashboardConfig config = processDashboardConfigMapper.selectDashboardConfigByProcessId(processId);
        if (config == null){
            // 无配置 查询工艺启用所有的工步
            Process process = processMapper.selectById(processId);
            if (process == null){
                throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
            }
            String activeVersion = process.getActiveVersion();
            if (StringUtils.isBlank(activeVersion)){
                throw new BmosException(MesResponseCode.NO_ACTIVE_PROCESS);
            }
            List<ProcedureModel> procedureModelList = procedureModelService.getByProcessIdAndVersion(processId, activeVersion);
            return new ProcessDashboardVO(process, procedureModelList);
        }else {
            return ProcessDashboardConfigConverter.INSTANCE.convertToVO(config);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDashboardConfig(SaveDashboardConfigDTO dto) {
        Process process = processMapper.selectById(dto.getProcessId());
        if (process == null){
            throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
        }
        if (!dto.getProcessVersion().equals(process.getActiveVersion())){
            throw new BmosException(MesResponseCode.PLAN_TEMPLATE_CONFIRMED_ERROR);
        }

        List<Long> procedureIds = CollectionUtils.convertList(dto.getProcedureList(), SaveDashboardConfigProcedureDTO::getProcedureId);
        List<Procedure> procedures = procedureService.selectByIds(procedureIds);
        Map<Long, String> procedureMap = CollectionUtils.convertMap(procedures, Procedure::getId, Procedure::getName, (k1, k2) -> k1);
        List<ProcessDashboardProcedureVO> procedureList = dto.getProcedureList().stream().map(item -> {
            ProcessDashboardProcedureVO step = new ProcessDashboardProcedureVO();
            step.setProcedureId(item.getProcedureId());
            step.setProcedureName(procedureMap.get(item.getProcedureId()));
            step.setCustomName(item.getCustomName());
            step.setModelCode(item.getModelCode());
            step.setEffect(item.getEffect());
            step.setSort(item.getSort());
            return step;
        }).collect(Collectors.toList());

        ProcessDashboardConfig config = processDashboardConfigMapper.selectDashboardConfigByProcessId(process.getId());
        if (config == null){
            // 新增
            ProcessDashboardConfig processDashboardConfig = new ProcessDashboardConfig(process, procedureList);
            processDashboardConfigMapper.insert(processDashboardConfig);
            List<ProcessDashboardConfigData> processDashboardConfigDataList = ProcessDashboardConfigConverter.INSTANCE.covert2ConfigDataList(procedureList, processDashboardConfig.getId());
            if (CollUtil.isNotEmpty(processDashboardConfigDataList)){
                processDashboardConfigDataMapper.insertBatch((processDashboardConfigDataList));
            }
        }else {
            config.setProcessVersion(dto.getProcessVersion());
            config.setProcedureList(procedureList);
            processDashboardConfigMapper.updateById(config);
            processDashboardConfigDataMapper.deleteByConfigId(config.getId());
            List<ProcessDashboardConfigData> processDashboardConfigDataList = ProcessDashboardConfigConverter.INSTANCE.covert2ConfigDataList(procedureList, config.getId());
            if (CollUtil.isNotEmpty(processDashboardConfigDataList)){
                processDashboardConfigDataMapper.insertBatch((processDashboardConfigDataList));
            }
        }
    }

    @Override
    public Process getOneByVersionId(Long businessId) {
        ProcessVersion version = processVersionService.getById(businessId);
        Process process = processMapper.selectById(version.getProcessId());
        process.setProcessVersion(version.getVersion());
        return process;
    }

    @Override
    public List<Process> selectByIdList(List<Long> processIdList) {
        if (CollUtil.isEmpty(processIdList)){
            return new ArrayList<>();
        }
        return processMapper.selectBatchIds(processIdList);
    }

    /**
     * @author: Ren Jin Guang
     * @Description: 返回关联工艺数据
     * @Param : list 产品树信息
     * @param : processCategoryMap 产品分类工艺信息
     * @param : processProductMap 产品工艺信息
     * @return: null
     * @Date: 2024-08-06 14:33:27
     */
    public void handelProcessListTreeNode(List<ProcessListItemTreeVO> list,Map<Long, List<Process>> processProductMap) {
        list.forEach(item -> {
            List<ProcessListItemTreeVO> children = item.getChildren();
            final List<Process> processList = processProductMap.get(item.getId());
            if (CollUtil.isNotEmpty(processList)){
                processList.forEach(processItem->{
                    ProcessListItemTreeVO vo = BeanUtil.toBean(processItem, ProcessListItemTreeVO.class);
                    vo.setIsFlag(true);
                    vo.setShowName(processItem.getName());
                    children.add(vo);
                });
            }
            if (CollUtil.isNotEmpty(item.getChildren())) {
                handelProcessListTreeNode(item.getChildren(),processProductMap);
            }
        });
    }

    @Override
    public List<ProductLineModuleTreeNodeVO> getProductLineTree() {
        ResponseInfo<List<LineModuleTreeNodeFeignVO>> listResponseInfo = FeignUtils.handleRequest(data -> factoryFeign.getLineModuleTreeVO(), null);
        List<LineModuleTreeNodeFeignVO> data = listResponseInfo.getData();
        if (CollUtil.isEmpty(data)){
            return new ArrayList<>();
        }
        return ProcedureConverter.INSTANCE.convert2ProductLineTreeVOList(data);
    }

    private void validateCircularDependency(Long originProcessId, List<ProcessRelation> relations) {
        Map<Long, List<ProcessRelation>> dataMap = CollectionUtils.convertMultiMap(relations, ProcessRelation::getProcessId);
        recursiveValid(originProcessId, originProcessId, dataMap);
    }

    private void recursiveValid(Long originProcessId, Long cur, Map<Long, List<ProcessRelation>> dataMap) {
        if (CollUtil.isEmpty(dataMap.get(cur))) {
            return;
        }
        dataMap.get(cur).forEach(relation -> {
            if (relation.getRelationProcessId().equals(originProcessId)) {
                throw new BmosException(MesResponseCode.PROCESS_RELATION_CIRCULAR);
            }
            recursiveValid(originProcessId, relation.getRelationProcessId(), dataMap);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.PROCESS, operationType = OperationType.PROCESS_UPDATE, businessId = "#dto.id")
    public ProcessVersionVO modifyProcess(ProcessModifyDTO dto) {
        //校验版本号
        processVersionService.validateVersion(dto.getId(), dto.getProcessId(), dto.getVersion());
        //校验是否是 编辑状态
        ProcessVersion processVersion = processVersionService.validateEditState(dto.getId());
        Long oldFormulaVersionId = processVersion.getProductFormulaVersionId();
        String modifyBeforeVersion = processVersion.getVersion();
        List<ProcessBatchRecordRelation> list = processBatchRecordRelationService.getListByProcessVersionId(processVersion.getId());
        //更新版本号
        processVersionService.modifyVersion(dto, processVersion);
        //更新关联批记录
        processBatchRecordRelationService.modifyBatch(dto);
        //更新工序
        procedureModelService.refreshBatch(processVersion, dto.getProcedures());
        // 工序配置未修改版本号
        procedureStepConfigService.refreshBatch(processVersion.getProcessId(), modifyBeforeVersion, dto.getVersion());
        List<ProcedureStepModel> stepModel = procedureStepModelService.getStepModelByProcessIdAndVersion(processVersion.getProcessId(), modifyBeforeVersion);
        // 如果工序删除，将工步的数据一并删除
        List<ProcedureModel> nowProcedureModel =
                procedureModelService.getByProcessIdAndVersion(processVersion.getProcessId(),
                        processVersion.getVersion());
        Set<Long> exitsProcedureModel = nowProcedureModel.stream().map(ProcedureModel::getId).collect(Collectors.toSet());
        Set<Long> deleteProcedureStepModel =
                stepModel.stream().filter(item -> !exitsProcedureModel.contains(item.getProcedureModelId())).map(ProcedureStepModel::getId).collect(Collectors.toSet());
        procedureStepModelService.removeBatchByIds(deleteProcedureStepModel);
        if (ObjectUtil.isNotEmpty(stepModel)) {
            List<BatchRecordVersion> recordVersionList = recordVersionService.queryVersionByRecordIdList(
                    CollectionUtils.convertList(dto.getBatchRecordItems(), RelationBatchRecordItemDTO::getBatchRecordId));
            Map<Long, BatchRecordVersion> versionMap = CollectionUtils.convertMap(recordVersionList, BatchRecordVersion::getId);
            Map<Long, RelationBatchRecordItemDTO> itemMap = CollectionUtils.convertMap(dto.getBatchRecordItems(), RelationBatchRecordItemDTO::getBatchRecordId);
            stepModel.forEach(item -> {
                if (ProcedureStepNodeFunctionEnum.notRecordNode(item.getNodeFunction())){
                    return;
                }
                BatchRecordVersion version = versionMap.get(item.getRecordVersionId());
                if (ObjectUtil.isNotEmpty(version)) {
                    item.setRecordVersionId(itemMap.get(version.getRecordId()).getBatchRecordVersionId());
                }
                item.setProcessVersion(dto.getVersion());
            });

        }
        procedureStepModelService.updateBatch(stepModel);
        List<ProcedureStepConfig> configList = procedureStepConfigService.getListByProcessVersion(processVersion.getProcessId(), modifyBeforeVersion);
        handleAndSaveConfig(configList, dto.getBatchRecordItems(), true, list);
        // 更新产线
        processProductionLineMapper.deleteByProcessVersionId(processVersion.getId());//保存关联的产线
        processProductionLineMapper.insertBatch(ProcessConverter.INSTANCE.convertToProcessProductionLine(processVersion, dto.getProductionLineIds()));
        // 更新配方物料绑定关系
        if (!Objects.equals(oldFormulaVersionId, processVersion.getProductFormulaVersionId())) {
            processFormulaRelationService.replaceFormulaMaterial(processVersion.getId(), oldFormulaVersionId);
        }
        return ProcessVersionVO.builder().processId(processVersion.getProcessId()).version(processVersion.getVersion()).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessVersionVO saveProcessVersion(ProcessSaveVersionDTO dto) {
        //校验版本号
        processVersionService.validateVersion(null, dto.getProcessId(), dto.getVersion());
        //更新版本号
        ProcessVersion processVersion = processVersionService.getById(dto.getId());
        Long oldFormulaVersionId = processVersion.getProductFormulaVersionId();
        if (ObjectUtil.isNull(processVersion)) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_NOT_EXIST);
        }
        clearBasicFields(processVersion);
        processVersionService.saveNewVersion(dto, processVersion);
        //更新关联批
        processBatchRecordRelationService.saveBatch(ProcessBatchRecordRelationConverter.INSTANCE.convertList(processVersion, dto.getBatchRecordItems()));
        //保存产线
        processProductionLineMapper.insertBatch(ProcessConverter.INSTANCE.convertToProcessProductionLine(processVersion, dto.getProductionLineIds()));
        //复制数据
        copyDataSave(dto, processVersion, false);
        // 更新配方物料绑定关系
        if (!Objects.equals(oldFormulaVersionId, processVersion.getProductFormulaVersionId())) {
            processFormulaRelationService.replaceFormulaMaterial(processVersion.getId(), oldFormulaVersionId);
        }
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), processVersion.getId(), OperationType.SAVE, null);
        return ProcessVersionVO.builder()
                .processId(processVersion.getProcessId())
                .version(processVersion.getVersion())
                .processVersionId(processVersion.getId())
                .build();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessVersionVO copyProcessVersion(ProcessCopyDTO dto) {
        //校验名称
        validateName(dto.getName());
        Process process = ProcessConverter.INSTANCE.convert(dto);
        //保存新工艺
        processMapper.insert(process);
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder().resourceId(process.getId()).deptIds(dto.getDeptIds()).build());
        //查询 被复制的版本
        if (!processVersionService.existByIdAndVersion(dto.getId(), dto.getOriginVersion())) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_NOT_EXIST);
        }
        //创建新版本
        ProcessVersion copyVersion = ProcessConverter.INSTANCE.convertVersion(dto, process);
        //保存流程图
        CreateDeploymentCmd cmd =
                ProcessVersionConverter.INSTANCE.convertCmd(process.getName(), dto.getProcessModel(), dto.getVersion());

        String deploymentId = workflowService.createDeployment(cmd);
        copyVersion.setProcessInstanceId(null);
        copyVersion.setProcessModelId(deploymentId);
        copyVersion.setProcessId(process.getId());
        //保存新版本
        processVersionService.save(copyVersion);
        //更新关联批
        processBatchRecordRelationService.saveBatch(ProcessBatchRecordRelationConverter.INSTANCE.convertList(copyVersion, dto.getBatchRecordItems()));
        //复制数据
        copyDataSave(ProcessConverter.INSTANCE.convert2SaveVersionDTO(dto), copyVersion, true);
        //保存产线关联
        processProductionLineMapper.insertBatch(ProcessConverter.INSTANCE.convertToProcessProductionLine(copyVersion, dto.getProductionLineIds()));
        // 更新配方物料绑定关系
        ProcessVersion oriVersion = processVersionService.getById(dto.getId());
        if (!Objects.equals(oriVersion.getProductFormulaVersionId(), dto.getProductFormulaVersionId())) {
            processFormulaRelationService.replaceFormulaMaterial(copyVersion.getId(), oriVersion.getProductFormulaVersionId());
        }
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), copyVersion.getId(), OperationType.SAVE, null);
        return ProcessVersionVO.builder()
                .processId(copyVersion.getProcessId())
                .version(copyVersion.getVersion())
                .processVersionId(copyVersion.getId()).build();
    }

    @Override
    public List<ProcessVO> getVersionList(ProcessQueryDTO dto) {
        return processVersionService.getProcessList(dto);
    }

    @Override
    public List<ProcessRecordOrderVO> getRecordOrder(ProcessRecordOrderQueryDTO dto) {
        List<ProcessRecordVO> records = procedureStepModelService.getRecords(dto);
        if (CollUtil.isEmpty(records)) {
            return Collections.emptyList();
        }
        List<ProcessRecordOrder> relations = processRecordOrderService.getRecordItems(dto.getProcessId(), dto.getProcessVersion());
        return ProcessRecordOrderConverter.INSTANCE.convertList(records, relations);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecordOrder(ProcessRecordOrderSaveDTO dto) {
        ProcessVersion version = processVersionService.getById(dto.getProcessVersionId());
        if (!ActionStateEnum.EDIT.getValue().equals(version.getActionState())) {
            throw new BmosException(MesResponseCode.PROCESS_ACTION_STATE_ERROR);
        }
        processRecordOrderService.saveRecordOrders(dto);
    }

    @Override
    public List<ProductProcessTreeNodeVO> getProcessProductTree() {
        List<Process> processes = processMapper.selectList();
        if (CollUtil.isEmpty(processes)) {
            return Collections.emptyList();
        }
        List<ProductMaterialCategory> categories = productMaterialCategoryService.selectListByType(CategoryInfoTypeEnum.PRODUCTION.getValue());
        Set<Long> productIds = CollectionUtils.convertSet(processes, Process::getProductId);
        List<ProductMaterial> products = productMaterialService.getListByTypeAndIds(CategoryInfoTypeEnum.PRODUCTION, productIds);
        return ProcessConverter.INSTANCE.convertTree(processes, categories, products);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditVersion(ProcessVersionAuditDTO dto) {
        ProcessVersion processVersion = processVersionService.getById(dto.getId());
        if (ObjectUtil.isNull(processVersion)) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_NOT_EXIST);
        }

        Process process = processMapper.selectById(processVersion.getProcessId());
        if (ObjectUtil.isNull(process)) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
        }
        if (StrUtil.isNotBlank(processVersion.getProcessInstanceId())){
            AuditProcessInstance instance = instanceQueryService.findByProcessInstanceIdAndState(processVersion.getProcessInstanceId(), ProcessState.ACTIVE);
            if (ObjectUtil.isNotEmpty(instance)){
                throw new BmosException(MesResponseCode.OPERATE_VERSION_FLOW_START_ERROR);
            }
        }
        if (!StrUtil.equals(processVersion.getActionState(),ActionStateEnum.CONFIRM.getValue()) &&
                !StrUtil.equals(processVersion.getActionState(),ActionStateEnum.INVALID.getValue())){
            throw new BmosException(MesResponseCode.PROCESS_NOT_CONFIRM);
        }

        //校验是否存在待生效以及发起审核的版本
        processVersionService.validateVersionAudit(process.getId());
        FlowStartDTO startDto = new FlowStartDTO();
        startDto.setBusinessKey(String.valueOf(dto.getId()));
        startDto.setCode(AuditCategoryCodeEnum.PROCESS.getCode());
        startDto.setCategoryCode(AuditCategoryCodeEnum.PROCESS.getCode());
        startDto.setName(process.getName());
        startDto.setExtField(processVersion.getVersion());
        String processInstanceId = flowAuditService.flowAuditStart(startDto);
        processVersion.setProcessInstanceId(processInstanceId);
        processVersion.setEffectDate(dto.getEffectDate());
        processVersion.setHistoryState(processVersion.getActionState());
        processVersion.setActionState(ActionStateEnum.APPROVAL.getValue());
        processVersionService.updateById(processVersion);
        saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), processVersion.getId(), OperationType.START_AUDIT, null);
    }

    @Override
    public CommonPage<ProcessTodoPageVO> getAuditTodoPage(ProcessTodoPageDTO dto) {
        FlowAuditTaskDTO flowAuditTaskDTO = dto.convertAuditTaskDTO();
        BasePage basePage = dto.convertBasePage();
        List<ProcessTodoPageVO> records = processVersionService.getAuditTodoProcessVersionIds(dto, ActionStateEnum.APPROVAL.getValue());
        if (CollUtil.isEmpty(records)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, basePage);
        }
        Map<Long, ProcessTodoPageVO> dataMap = CollectionUtils.convertMap(records, ProcessTodoPageVO::getId);
        flowAuditTaskDTO.setBusinessKeyList(dataMap.keySet().stream().map(String::valueOf).collect(Collectors.toList()));
        PageQueryResp<List<TaskListResp>> pageResult = flowAuditService.queryToDoListByCategory(flowAuditTaskDTO);
        if (pageResult.getTotal() == 0) {
            return CommonPage.CommonPage(Collections.emptyList(), pageResult.getTotal(), basePage);
        }
        List<String> businessKey = CollectionUtils.convertList(pageResult.getData(), TaskListResp::getBusinessKey);
        dto.setProcessVersionIds(businessKey.stream().map(Long::valueOf).collect(Collectors.toList()));
        List<ProcessTodoPageVO> vos = ProcessConverter.INSTANCE.convertList(pageResult.getData(), dataMap);
        return CommonPage.CommonPage(vos, pageResult.getTotal(), basePage);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessSuccessCallBack(String processInstanceId, String comment, String userId) {
        //处理工艺版本数据
        ProcessVersion processVersion = processVersionService.updateVersionActionState(processInstanceId);
        Process process = processMapper.selectById(processVersion.getProcessId());
        if (StrUtil.equals(processVersion.getActionState(),ActionStateEnum.VALID.getValue())){
            process.setActiveVersion(processVersion.getVersion());
            processMapper.updateById(process);
            updateRoomAndLine(true, processVersion);
        }
        // 处理生产计划模板确认状态
        planTemplateService.updateTemplateConfirmStatus(Collections.singletonList(process));
        List<String> processModelIds = procedureModelService.getProcessModelList(processVersion.getId());
        processModelIds.add(processVersion.getProcessModelId());
        workflowService.deployBatch(processModelIds);
        //saveHistoryLog(comment, userId, processVersion.getId(), OperationType.APPROVE_AUDIT);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessRejectCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName) {
        ProcessVersion processVersion = processVersionService.getByProcessInstanceId(processInstanceId);
        processVersion.setActionState(processVersion.getHistoryState());
        processVersionService.updateById(processVersion);
        saveHistoryLog(comment, remark, userId, processVersion.getId(), OperationType.REJECT_AUDIT, nodeName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditExecutionSuccessCallBack(String businessKey, String comment, String remark, String userId, String nodeName) {
        saveHistoryLog(comment, remark, userId, Long.valueOf(businessKey), OperationType.APPROVE_AUDIT, nodeName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditExecutionRejectCallBack(String businessKey, String comment, String userId) {
        saveHistoryLog(comment, null, userId, Long.valueOf(businessKey), OperationType.REJECT_AUDIT, null);
    }

    @Override
    public List<ProcessListItemVO> getRecursionRelationProcessList(ProcessRelationQueryDTO dto) {
        List<ProcessRelation> relationData = processRelationService.getList();
        if (CollUtil.isEmpty(relationData)) {
            return Collections.emptyList();
        }
        List<Process> processes = processMapper.selectList();
        Map<Long, Process> processMap = CollectionUtils.convertMap(processes, Process::getId);
        Set<Long> resultIds = new HashSet<>();
        resultIds.add(dto.getProcessId());
        Map<Long, List<Long>> relations = CollectionUtils
                .convertMultiMap(relationData, ProcessRelation::getProcessId, ProcessRelation::getRelationProcessId);
        recursionSelectRelations(relations, dto.getProcessId(), resultIds);
        return resultIds.stream().map(id -> {
            Process process = Optional.ofNullable(processMap.get(id)).orElse(new Process());
            return ProcessListItemVO.builder().id(id).name(process.getName()).activeVersion(process.getActiveVersion()).build();
        }).collect(Collectors.toList());
    }

    private void recursionSelectRelations(Map<Long, List<Long>> relations, Long processId, Collection<Long> resultIds) {
        List<Long> relationIds = relations.get(processId);
        if (CollUtil.isNotEmpty(relationIds)) {
            resultIds.addAll(relationIds);
            for (Long relationId : relationIds) {
                recursionSelectRelations(relations, relationId, resultIds);
            }
        }
    }


    /**
     * 复制数据并保存为新的工序工艺版本
     * 此方法用于处理工艺版本工序工步数据的复制和保存操作
     * 并根据提供的信息进行相应的构建和保存
     * @param dto
     * @param processVersion 新工艺版本对象
     */
    private void copyDataSave(ProcessSaveVersionDTO dto, ProcessVersion processVersion, boolean copy) {

        CopyProcessVersion copyProcessVersion = new CopyProcessVersion();

        copyProcessVersion.convertFromVersion(processVersion);

        // 待处理的工序列表
        List<ProcedureCopyDTO> procedures = dto.getProcedures();
        // 创建一个列表用于存储复制后的工序对象
        List<CopyProcedure> copyProcedures = new ArrayList<>();
        // 创建一个复制上下文对象，用于在复制过程中传递共享信息
        CopyContext copyContext = buildContext(dto);
        copyContext.setCopy(copy);
        for (ProcedureCopyDTO procedure : procedures) {
            // 初始化一个新的复制工序对象
            CopyProcedure copyProcedure = copyProcessVersion.initCopyProcedure();
            // 构建复制工序对象，包括设置工序信息和下层的工步信息及相关配置信息
            copyProcedure.buildProcedure(copyContext, procedure);
            copyProcedures.add(copyProcedure);
        }
        // 复制完成后处理工步条件详情中的工步id以及任务id
        handelStepCondition(copyProcedures);
        // 展平并保存复制后的数据
        flattenProcedureAndSave(copyContext, copyProcedures);
    }


    /**
     * 构建复制上下文对象
     * @param dto
     * @return
     */
    private CopyContext buildContext(ProcessSaveVersionDTO dto) {
        // 记录项数据
        List<BatchRecordVersion> recordVersionList = recordVersionService.queryVersionByRecordIdList(
                CollectionUtils.convertList(dto.getBatchRecordItems(), RelationBatchRecordItemDTO::getBatchRecordId));
        Map<Long, BatchRecordVersion> versionMap = CollectionUtils.convertMap(recordVersionList, BatchRecordVersion::getId);
        Map<Long, RelationBatchRecordItemDTO> itemMap = CollectionUtils.convertMap(dto.getBatchRecordItems(), RelationBatchRecordItemDTO::getBatchRecordId);

        List<Long> ids = dto.getProcedures().stream().map(ProcedureCopyDTO::getId).filter(ObjectUtil::isNotNull).collect(Collectors.toList());
        //步骤
        List<ProcedureStepModel> oldProcedureStepModel = procedureStepModelService.getByProcedureModelIds(ids);
        Set<Long> stepIds = CollectionUtils.convertSet(oldProcedureStepModel, ProcedureStepModel::getId);

        Map<Long, List<ProcedureStepModel>> oldProcedureStepModelMap =
                CollectionUtils.convertMultiMap(oldProcedureStepModel, ProcedureStepModel::getProcedureModelId);

        //工步绑定的操作规程
        List<ProcedureStepSop> sops = sopService.queryListByStepModelId(stepIds);
        Map<Long, List<ProcedureStepSop>> sopMap = CollectionUtils.convertMultiMap(sops, ProcedureStepSop::getStepModelId);

        // 查询 步骤组件配置
        List<ProcedureStepConfig> configs = procedureStepConfigService.getListByProcedureStepModelIds(dto.getProcessId(), dto.getOriginVersion(),
                oldProcedureStepModel.stream()
                        .map(e -> BooleanUtil.isTrue(e.getReusable()) ? 0L : e.getId())
                        .collect(Collectors.toSet()));

        // 查询步骤节点权限
        List<ProcedureStepRole> roles = procedureStepRoleRelationService.getListByProcedureStepIds(stepIds);
        Map<Long, List<ProcedureStepRole>> roleMap = CollectionUtils.convertMultiMap(roles, ProcedureStepRole::getProcedureStepId);

        //找到配置的条件以及表达式
        Map<Long, List<ProcedureExpression>> expressionMap = expressionService.getMapByProcedureStepModeIds(stepIds);

        Integer sort = dto.getProcedures().stream()
                .map(ProcedureCopyDTO::getSort)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(1);

        // 处理归档顺序
        Map<String, ProcessRecordOrder> orderMap = processRecordOrderService.getRecordItems(dto.getProcessId(),
                dto.getOriginVersion())
                .stream()
                .collect(Collectors.toMap(e -> e.getRecordItemId() + "-" + e.getProcedureStepModelId(), e -> e));
        return CopyContext.builder()
                .versionMap(versionMap)
                .itemMap(itemMap)
                .stepModelMap(oldProcedureStepModelMap)
                .sopMap(sopMap)
                .stepConfigList(configs)
                .stepGroupMap(roleMap)
                .expressionMap(expressionMap)
                .sort(new AtomicInteger(sort))
                .orderMap(orderMap)
                .build();
    }

    /**
     * 摊开处理好的工序对象
     * 将工序信息和内部的工步信息以及相关配置保存
     *
     * @param copyContext
     * @param copyProcedures
     */
    private void flattenProcedureAndSave(CopyContext copyContext, List<CopyProcedure> copyProcedures) {
        // 处理工序层信息保存
        handleProcedureLevel(copyProcedures);
        // 处理工步层信息保存
        handleProcedureStepLevel(copyProcedures.stream()
                .map(CopyProcedure::getCopyProcedureStepList)
                .flatMap(List::stream)
                .collect(Collectors.toList()), copyContext.isCopy());
        // 处理保存归档顺序
        processRecordOrderService.saveBatch(copyContext.getOrderMap().values());
    }

    private void handleProcedureStepLevel(List<CopyProcedureStep> copySteps, boolean copy) {
        // 工步
        List<ProcedureStep> steps = new ArrayList<>();
        // 工步模型
        List<ProcedureStepModel> stepModels = new ArrayList<>();
        // 工步班组
        List<ProcedureStepRole> stepGroups = new ArrayList<>();
        // 工步操规
        List<ProcedureStepSop> stepSops = new ArrayList<>();
        // 工步条件
        List<ProcedureCondition> conditions = new ArrayList<>();
        // 工步表达式
        List<ProcedureExpression> expressions = new ArrayList<>();
        // 组件配置
        Set<ProcedureStepConfig> stepConfigs = new HashSet<>();
        copySteps.forEach(copyStep -> {
            steps.add(copyStep.convert2ProcedureStep());
            stepModels.add(copyStep.convert2ProcedureStepModel());
            stepGroups.addAll(copyStep.getStepGroupList());
            stepSops.addAll(copyStep.getStepSopList());
            conditions.addAll(copyStep.getProcedureConditionList());
            expressions.addAll(copyStep.getExpressionList());
            stepConfigs.addAll(copyStep.getProcedureStepConfigs());
        });
        // 保存工步 升版不需保存工步
        if (copy) {
            procedureStepService.saveBatch(steps);
        }
        // 保存工步模型
        procedureStepModelService.saveBatch(stepModels);
        // 保存工步班组
        procedureStepRoleRelationService.saveBatch(stepGroups);
        // 保存工步条件
        conditionService.saveBatch(conditions);
        // 保存工步表达式
        expressionService.saveBatch(expressions);
        // 工步操规
        sopService.saveBatch(stepSops);
        // 处理组件配置
        handleConfigsComponentId(stepConfigs);
        procedureStepConfigService.saveBatch(stepConfigs);
    }

    /**
     * 处理工步条件中的id
     * @param copyProcedures 复制完成的数据
     */
    private void handelStepCondition(List<CopyProcedure> copyProcedures){
        List<CopyProcedureStep> stepList = copyProcedures.stream()
                .map(CopyProcedure::getCopyProcedureStepList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(stepList)){
            return;
        }
        List<ProcedureCondition> stepConditionList = stepList.stream()
                .map(CopyProcedureStep::getProcedureConditionList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(stepConditionList)){
            return;
        }
        Map<Long, CopyProcedure> procedureMap = CollectionUtils.convertMap(copyProcedures, CopyProcedure::getOldProcedureModelId);
        Map<Long, CopyProcedureStep> stepMap = CollectionUtils.convertMap(stepList, CopyProcedureStep::getOldStepModelId);
        stepConditionList.forEach(item->{
            ConditionDetailVO detailVO = JsonUtils.parseObject(item.getConditionDetails(), ConditionDetailVO.class);
            Optional.ofNullable(procedureMap.get(detailVO.getProcedureId())).ifPresent(copyProcedure -> {
                detailVO.setProcedureId(copyProcedure.getProcedureModelId());
                detailVO.setProcedureName(copyProcedure.getProcedureName());
            });
            if (ObjectUtil.isNotNull(detailVO.getTaskNodeId())) {
                Optional.ofNullable(stepMap.get(detailVO.getTaskNodeId()))
                        .ifPresent(copyProcedureStep -> {
                            detailVO.setTaskNodeId(copyProcedureStep.getStepModelId());
                            detailVO.setTaskNodeName(copyProcedureStep.getStepName());
                        });
            }
            if (ObjectUtil.isNotNull(detailVO.getStepId())) {
                Optional.ofNullable(stepMap.get(detailVO.getStepId()))
                        .ifPresent(copyProcedureStep -> {
                            detailVO.setStepId(copyProcedureStep.getStepModelId());
                            detailVO.setStepName(copyProcedureStep.getStepName());
                        });
            }
            item.setConditionDetails(JsonUtils.toJsonString(detailVO));
        });
    }
    /**
     * 处理组件id
     * @param stepConfigs
     */
    private void handleConfigsComponentId(Collection<ProcedureStepConfig> stepConfigs) {
        List<BatchRecordComponent> componentList =
                batchRecordComponentService.selectByRecordVersionIdsAndFields(CollectionUtils.convertSet(stepConfigs,
                        ProcedureStepConfig::getRecordVersionId), CollectionUtils.convertSet(stepConfigs,
                        ProcedureStepConfig::getFieldId));
        Map<String, BatchRecordComponent> map = componentList.stream()
                .collect(Collectors.toMap(
                        item -> item.getRecordVersionId() + "-" + item.getFieldId(),
                        item -> item,
                        (existing, replacement) -> existing
                ));
        stepConfigs.forEach(e->{
            String key = e.getRecordVersionId() + "-" + e.getFieldId();
            e.setComponentId(map.get(key) != null ? map.get(key).getId() : e.getComponentId());
        });
    }

    private void handleProcedureLevel(List<CopyProcedure> copyProcedures) {
        if (CollUtil.isEmpty(copyProcedures)) {
            return;
        }
        // 工序
        List<Procedure> procedures = new ArrayList<>();
        // 工序模型
        List<ProcedureModel> procedureModels = new ArrayList<>();
        // 工序房间
        List<ProcedureModelRoom> rooms = new ArrayList<>();
        // 工序物料
        List<ProcedureModelMaterial> materials = new ArrayList<>();
        // 工序班组
        List<ProcedureModelGroup> groups = new ArrayList<>();
        //工序表达式
        List<ProcedureExpression> expressionList = new ArrayList<>();
        //工序条件
        List<ProcedureCondition> conditionList = new ArrayList<>();
        Map<String, String> subFlowMap = new HashMap<>();
        copyProcedures.forEach(copyProcedure -> {
            procedures.add(copyProcedure.convert2Procedure());
            procedureModels.add(copyProcedure.convert2ProcedureModel());
            rooms.addAll(copyProcedure.getRooms());
            materials.addAll(copyProcedure.getMaterials());
            groups.addAll(copyProcedure.getGroups());
            Optional.ofNullable(copyProcedure.getExpression()).ifPresent(expressionList::add);
            conditionList.addAll(copyProcedure.getCondition());
            subFlowMap.put(copyProcedure.getNodeId(), copyProcedure.getProcedureProcessModelId());
        });
        // 保存工序
        procedureService.saveOrUpdateBatch(procedures);
        // 保存工序模型
        procedureModelService.saveBatch(procedureModels);
        // 保存工序房间
        procedureModelRoomMapper.insertBatch(rooms);
        // 保存工序物料
        procedureModelMaterialService.saveBatch(materials);
        // 保存工序班组
        procedureModelGroupService.saveBatch(groups);
        //保存工序表达式
        expressionService.saveBatch(expressionList);
        //保存工序条件
        conditionService.saveBatch(conditionList);
        // 处理流程节点
        workflowService.bindBatchDeployment(CollUtil.getFirst(copyProcedures).getProcessModelId(), subFlowMap);
    }

    private void handleAndSaveConfig(List<ProcedureStepConfig> stepConfigsToSave,
                                     List<RelationBatchRecordItemDTO> batchRecordItems,
                                     boolean modify,
                                     List<ProcessBatchRecordRelation> list) {
        if (CollUtil.isNotEmpty(stepConfigsToSave)) {
            // 校验记录升版
            // 需要更新保存的配置
            List<ProcedureStepConfig> needUpdateConfig = new ArrayList<>();
            List<ProcedureStepConfig> noUpdateConfig = new ArrayList<>();
            Map<Long, List<ProcedureStepConfig>> oldConfigMap = CollectionUtils.convertMultiMap(stepConfigsToSave, ProcedureStepConfig::getRecordVersionId);
            // 新记录map key 记录id value 记录版本id
            Map<Long, Long> recordMap = CollectionUtils.convertMap(batchRecordItems, RelationBatchRecordItemDTO::getBatchRecordId,
                    RelationBatchRecordItemDTO::getBatchRecordVersionId);
            Map<Long, ProcessBatchRecordRelation> originRecordMap = CollectionUtils.convertMap(list, ProcessBatchRecordRelation::getBatchRecordId);
            List<Long> longs = new ArrayList<>();
            List<Long> fieldIdList = new ArrayList<>();
            for (Map.Entry<Long, Long> entry : recordMap.entrySet()) {
                Long key = entry.getKey();
                ProcessBatchRecordRelation recordRelation = originRecordMap.get(key);
                if (recordRelation == null) {
                    continue;
                }
                List<ProcedureStepConfig> configList = oldConfigMap.get(recordRelation.getBatchRecordVersionId());
                if (CollUtil.isNotEmpty(configList) && !Objects.equals(recordRelation.getBatchRecordVersionId(), entry.getValue())) {
                    longs.add(entry.getValue());
                    configList.forEach(config -> {
                        config.setRecordVersionId(entry.getValue());
                        fieldIdList.add(config.getFieldId());
                    });
                    needUpdateConfig.addAll(configList);
                } else if (CollUtil.isNotEmpty(configList)) {
                    noUpdateConfig.addAll(configList);
                }
            }
            List<BatchRecordComponent> componentList = batchRecordComponentService.selectByRecordVersionIdsAndFields(longs, fieldIdList);
            Map<Long, Map<Long, Long>> fieldMap = componentList.stream()
                    .collect(Collectors.groupingBy(BatchRecordComponent::getRecordVersionId,
                            Collectors.toMap(BatchRecordComponent::getFieldId, BatchRecordComponent::getId)));
            if (CollUtil.isNotEmpty(fieldMap)) {
                for (ProcedureStepConfig procedureStepConfig : needUpdateConfig) {
                    Map<Long, Long> idMap = fieldMap.get(procedureStepConfig.getRecordVersionId());
                    if (idMap == null) {
                        continue;
                    }
                    Long newComponentId = idMap.get(procedureStepConfig.getFieldId());
                    if (newComponentId == null) {
                        continue;
                    }
                    procedureStepConfig.setComponentId(newComponentId);
                }
            }
            ;
            if (modify) {
                procedureStepConfigService.updateBatch(needUpdateConfig);
            } else {
                needUpdateConfig.addAll(noUpdateConfig);
                procedureStepConfigService.saveBatch(new ArrayList<>(needUpdateConfig));
            }
        }
    }


    private void buildRelations(ProcessRelationSaveDTO dto,
                                List<ProcessRelation> relations,
                                List<ProcessRelationMaterial> relationMaterials) {
        for (ProcessRelationDTO processRelationDTO : dto.getRelations()) {
            if (ObjectUtil.isNull(processRelationDTO.getRelationProcessId())) {
                continue;
            }
            Long nextId = CustomIdGenerator.nextId();
            ProcessRelation processRelation = ProcessRelation
                    .builder()
                    .id(nextId)
                    .processId(dto.getProcessId())
                    .relationProcessId(processRelationDTO.getRelationProcessId())
                    .build();
            relations.add(processRelation);
            for (Long materialId : processRelationDTO.getMaterialIds()) {
                ProcessRelationMaterial relationMaterial =
                        ProcessRelationMaterial.builder()
                                .processRelationId(nextId)
                                .materialId(materialId)
                                .processId(dto.getProcessId())
                                .build();
                relationMaterials.add(relationMaterial);
            }
        }
    }

    private void clearBasicFields(ProcessVersion processVersion) {
        processVersion.setId(null);
        processVersion.setCreateBy(null);
        processVersion.setCreateTime(null);
        processVersion.setUpdateBy(null);
        processVersion.setUpdateTime(null);
        processVersion.setState(false);
        processVersion.setProcessInstanceId(null);
        processVersion.setActionState(ActionStateEnum.EDIT.getValue());
        processVersion.setHistoryState(ActionStateEnum.EDIT.getValue());
        processVersion.setEffectDate(StrUtil.DASHED);
    }

    private void saveHistoryLog(String comment, String remark, String userId, Long id, OperationType operationType, String nodeName) {
        operationHistoryService.save(OperationLogModel.builder()
                .module(BusinessModule.PROCESS.name())
                .businessId(id)
                .operationType(operationType.getValue())
                .remark(remark)
                .nodeName(nodeName)
                .comment(comment)
                .createBy(userId)
                .build());
    }
}

