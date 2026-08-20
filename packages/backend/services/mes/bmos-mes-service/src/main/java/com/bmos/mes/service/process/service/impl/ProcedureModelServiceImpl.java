package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.role.dto.PlatformRoleListQueryDTO;
import com.bmos.mes.service.platform.role.role.PlatformRoleVO;
import com.bmos.mes.service.platform.role.service.PlatformRoleService;
import com.bmos.mes.service.platform.user.feign.PlatformUserOpenFeign;
import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.process.convert.ProcedureConverter;
import com.bmos.mes.service.process.convert.ProcedureModelConverter;
import com.bmos.mes.service.process.convert.ProcedureStepModelConverter;
import com.bmos.mes.service.process.convert.Task.ProcessTaskConverter;
import com.bmos.mes.service.process.dto.*;
import com.bmos.mes.service.process.dto.query.ProcedureHistoricQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcedurePrincipalQueryDTO;
import com.bmos.mes.service.process.dto.save.ProcedureSaveDTO;
import com.bmos.mes.service.process.dto.save.SaveProcessSortVO;
import com.bmos.mes.service.process.mapper.ProcedureModelMapper;
import com.bmos.mes.service.process.mapper.ProcedureModelRoomMapper;
import com.bmos.mes.service.process.mapper.ProcessProductionLineMapper;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.service.*;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import com.bmos.mes.service.process.vo.Task.EquipmentModuleTreeNodeVO;
import com.bmos.mes.service.process.vo.Task.NodeVO;
import com.bmos.mes.service.process.vo.Task.ProcedureStepAndTaskVO;
import com.bmos.mes.service.workflow.dto.BindDeploymentDTO;
import com.bmos.mes.service.workflow.service.WorkflowService;
import com.bmos.orchestrator.engine.core.command.CreateDeploymentCmd;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentModuleTreeNodeFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentVO;
import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryLineDetailFeignVO;
import com.bmos.platform.facade.factory.vo.FactoryRoomFeignVO;
import com.bmos.platform.facade.factory.vo.RoomInfoFeignVO;
import com.bmos.platform.facade.system.role.feign.RoleFeign;
import com.bmos.platform.facade.system.role.vo.FeignRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcedureModelServiceImpl implements ProcedureModelService {

    @Autowired
    private ProcedureModelMapper procedureModelMapper;

    @Autowired
    @Lazy
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    @Lazy
    private WorkflowService workflowService;

    @Autowired
    @Lazy
    private ProcessVersionService processVersionService;

    @Autowired
    @Lazy
    private ProcedureService procedureService;

    @Autowired
    @Lazy
    private ProcedureStepService procedureStepService;

    @Autowired
    private ProcedureModelGroupService procedureModelGroupService;

    @Autowired
    private PlatformUserOpenFeign platformUserOpenFeign;

    @Autowired
    private ProcedureModelMaterialService procedureModelMaterialService;

    @Autowired
    private ProcedureModelRoomMapper procedureModelRoomMapper;

    @Autowired
    private FactoryFeign factoryFeign;

    @Autowired
    private EquipmentConfigFeign equipmentConfigFeign;

    @Autowired
    private ProcessProductionLineMapper processProductionLineMapper;

    @Resource
    @Lazy
    private ProcedureExpressionService expressionService;

    @Resource
    private PlatformRoleService platformRoleService;

    @Resource
    private RoleFeign roleFeign;

    @Resource
    private ProcedureStepSopService stepSopService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ProcedureModel> saveBatch(List<ProcedureModel> procedureModels) {
        procedureModelMapper.insertBatch(procedureModels);
        return procedureModels;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshBatch(ProcessVersion processVersion, List<ProcedureDTO> procedures) {
        //获取当前工序最大排序号默认返回 1
        Integer sort = procedureModelMapper.selectMaxSort(processVersion.getId());
        //如果有需要新增的基础的工序信息，先保存
        List<ProcedureModel> procedureModels =
                ProcedureModelConverter.INSTANCE.convertList(processVersion, procedures,
                        getProceduresNewMap(processVersion, procedures),sort);

        Map<Boolean, List<ProcedureModel>> partition =
                procedureModels.stream().collect(Collectors.partitioningBy(e -> ObjectUtil.isNotNull(e.getId())));

        // 更新 已存在数据库的 工序
        List<ProcedureModel> proceduresInDB = partition.get(true);
        if (CollUtil.isNotEmpty(proceduresInDB)) {
            //删除 工序
            List<Long> ids = CollectionUtils.convertList(proceduresInDB, ProcedureModel::getId);
            //删除是删除不包含当前工序id的
            procedureModelMapper.deleteByIdNotIn(processVersion.getId(), ids);
            procedureModelMapper.updateBatch(proceduresInDB);
            procedureModelGroupService.deleteByProcedureModelIds(ids);
            procedureModelMaterialService.deleteByProcedureModelIds(ids);
            procedureModelRoomMapper.deleteByProcedureModelIds(ids);
            //更新序配置完成条件以及表达式
            expressionService.updateExpressionAndCondition(proceduresInDB);
        } else {
            List<ProcedureModel> modelList = procedureModelMapper.selectByProcessId(processVersion.getId());
            procedureModelMapper.deleteByProcessVersion(processVersion.getId());
            //同时删除工序完成条件配置
            List<Long> modelIdList = CollectionUtils.convertList(modelList, ProcedureModel::getId);
            expressionService.deleteByProcedureModelIds(modelIdList);
        }
        // 新增 未存在数据库的 工序
        if (CollUtil.isNotEmpty(partition.get(false))) {
            List<ProcedureModel> modelList = partition.get(false);
            procedureModelMapper.insertBatch(modelList);
            //新增工序完成条件配置
            expressionService.saveProcedureExpression(modelList);
        }

        //保存工序班组
        List<ProcedureModelGroup> groups = ProcedureConverter.INSTANCE.convertGroupList(procedureModels);
        //保存工序配方物料
        List<ProcedureModelMaterial> materials = ProcedureConverter.INSTANCE.convertMaterialList(procedureModels);
        procedureModelGroupService.saveBatch(groups);
        procedureModelMaterialService.saveBatch(materials);
        // 保存工序房间
        List<ProcedureModelRoom> rooms = ProcedureConverter.INSTANCE.convertRoomList(procedureModels);
        procedureModelRoomMapper.insertBatch(rooms);
    }

    private Map<String, Long> getProceduresNewMap(ProcessVersion processVersion, List<ProcedureDTO> procedures) {
        List<ProcedureDTO> toSave =
                procedures.stream().filter(e -> ObjectUtil.isNull(e.getProcedureId())).collect(Collectors.toList());
        List<Procedure> proceduresNew =
                procedureService.saveBatch(ProcedureConverter.INSTANCE.convertList(processVersion, toSave));
        if (CollUtil.isEmpty(proceduresNew)) {
            return Collections.emptyMap();
        }
        return CollectionUtils.convertMap(proceduresNew, Procedure::getName, Procedure::getId);
    }

    @Override
    public List<ProcedureModel> getByProcessIdAndVersion(Long processId, String version) {
        return procedureModelMapper.selectByProcessIdAndVersion(processId, version);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveDetail(ProcedureSaveDTO dto) {
        ProcedureModel procedureModel = updateProcedure(dto);
        //保存工序步骤
        this.resolveStepNew(dto, procedureModel);
        this.resolveTaskNew(dto, procedureModel);
        procedureStepModelService.saveBatch(procedureModel, dto.getProcedureSteps());
        procedureStepModelService.saveBatch(procedureModel, dto.getProcedureTasks());
        return procedureModel.getProcessModelId();
    }

    private ProcedureModel updateProcedure(ProcedureSaveDTO dto) {
        ProcedureModel procedureModel = procedureModelMapper.selectById(dto.getProcedureId());
        if (ObjectUtil.isNull(procedureModel)) {
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_EXIST);
        }
        ProcessVersion version = processVersionService.getByProcessIdAndVersion(procedureModel.getProcessId(),
                procedureModel.getProcessVersion());
        CreateDeploymentCmd cmd = ProcedureModelConverter.INSTANCE.convertCmd(procedureModel.getName(), dto);
        procedureModel.setProcessModelId(createDeployment(procedureModel, version, cmd));
        procedureModel.setProcessVersionId(version.getId());
        procedureModelMapper.updateById(procedureModel);
        return procedureModel;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyProcedureDetail(ProcedureSaveDTO dto) {
        ProcedureModel procedureModel = updateProcedure(dto);
        //处理步骤
        this.resolveStepNew(dto, procedureModel);
        //处理任务
        this.resolveTaskNew(dto, procedureModel);
        this.modifyStepOrTaskModel(procedureModel, dto);

    }

    private void resolveTaskNew(ProcedureSaveDTO dto, ProcedureModel procedureModel) {
        if (CollUtil.isEmpty(dto.getProcedureTasks())) {
            return;
        }
        List<ProcedureStepDTO> taskNew =
                dto.getProcedureTasks().stream().filter(e -> ObjectUtil.isNull(e.getProcedureStepId())).collect(Collectors.toList());
        if (CollUtil.isEmpty(taskNew)) {
            dto.getProcedureTasks().forEach(item-> item.setStepType(StepTaskTypeEnum.TASK));
            return;
        }
        List<ProcedureStep> procedureTasks = procedureStepService.saveBatch(procedureModel, taskNew, StepTaskTypeEnum.TASK);
        Map<String, Long> procedureStepMap = CollectionUtils.convertMap(procedureTasks, ProcedureStep::getName,
                ProcedureStep::getId);
        dto.getProcedureTasks().forEach(e -> {
            if (ObjectUtil.isNull(e.getProcedureStepId())) {
                e.setProcedureStepId(procedureStepMap.get(e.getHistoricalName()));
                e.setStepType(StepTaskTypeEnum.TASK);
            }
        });
    }

    private void modifyStepOrTaskModel(ProcedureModel procedureModel, ProcedureSaveDTO saveDTO) {
        List<ProcedureStepDTO> stepDTOS=new ArrayList<>();
        if (CollectionUtil.isNotEmpty(saveDTO.getProcedureTasks())) {
            saveDTO.getProcedureTasks().forEach(e->e.setStepType(StepTaskTypeEnum.TASK));
            stepDTOS.addAll(saveDTO.getProcedureTasks());
        }
        if (CollectionUtil.isNotEmpty(saveDTO.getProcedureSteps())) {
            saveDTO.getProcedureSteps().forEach(e->e.setStepType(StepTaskTypeEnum.STEP));
            stepDTOS.addAll(saveDTO.getProcedureSteps());
        }
        Map<Boolean, List<ProcedureStepDTO>> partition = stepDTOS
                .stream()
                .collect(Collectors.partitioningBy(e -> ObjectUtil.isNotNull(e.getId())));
        List<ProcedureStepDTO> stepsInDB = partition.get(true);
        //true 代表 已存在的 工序步骤
        if (CollUtil.isNotEmpty(stepsInDB)) {
            // 删除 工序步骤
            procedureStepModelService.deleteByIdNotIn(procedureModel.getId(), CollectionUtils.convertList(stepsInDB,
                    ProcedureStepDTO::getId));
            // 更新 工序步骤
            procedureStepModelService.updateBatchById(procedureModel, stepsInDB);
            // 更新 执行岗
            procedureStepModelService.updateBatchRoles(stepsInDB);
            //更新sop配置
            stepSopService.updateBatchSops(stepsInDB);
        } else {
            procedureStepModelService.deleteByProcedureModelId(procedureModel.getId());
        }
        //false 代表 新创建的 工序步骤
        List<ProcedureStepDTO> stepsForSave = partition.get(false);
        if (CollUtil.isNotEmpty(stepsForSave)) {
            log.info("新增工步模型表信息: {}", procedureModel);
            procedureStepModelService.saveBatch(procedureModel, stepsForSave);
        }
    }

    private void resolveStepNew(ProcedureSaveDTO dto, ProcedureModel procedureModel) {
        if (CollUtil.isEmpty(dto.getProcedureSteps())) {
            return;
        }
        List<ProcedureStepDTO> stepsNew =
                dto.getProcedureSteps().stream().filter(e -> ObjectUtil.isNull(e.getProcedureStepId())).collect(Collectors.toList());
        if (CollUtil.isEmpty(stepsNew)) {
            dto.getProcedureSteps().forEach(item-> item.setStepType(StepTaskTypeEnum.STEP));
            return;
        }
        List<ProcedureStep> procedureSteps = procedureStepService.saveBatch(procedureModel, stepsNew,StepTaskTypeEnum.STEP);
        Map<String, Long> procedureStepMap = CollectionUtils.convertMap(procedureSteps, ProcedureStep::getName,
                ProcedureStep::getId);
        dto.getProcedureSteps().forEach(e -> {
            if (ObjectUtil.isNull(e.getProcedureStepId())) {
                e.setProcedureStepId(procedureStepMap.get(e.getHistoricalName()));
                e.setStepType(StepTaskTypeEnum.STEP);
            }
        });
    }

    @Override
    public List<HistoricVO> getHistoricProcedureList(ProcedureHistoricQueryDTO dto) {
        return ProcedureConverter.INSTANCE.convertHistoric(procedureService.getHistoricList(dto));
    }

    @Override
    public List<ProcedureModel> getByIds(List<Long> ids) {
        return procedureModelMapper.selectBatchIds(ids);
    }

    @Override
    public List<ProcedureVO> getList(ProcedureQueryDTO dto) {
        List<ProcedureModel> models = procedureModelMapper.selectListByCondition(dto);
        Set<Long> ids = CollectionUtils.convertSet(models, ProcedureModel::getId);
        Map<Long, List<Long>> groupMap = procedureModelGroupService.getByProcedureModelIds(ids);
        Map<Long, Procedure> procedureMap = CollectionUtils.convertMap(procedureService.selectByIds(CollectionUtils.convertList(models, ProcedureModel::getProcedureId)), Procedure::getId);
        return ProcedureModelConverter.INSTANCE.convertVOList(models, groupMap, Collections.emptyMap(),
                Collections.emptyMap(),Collections.emptyMap(), procedureMap);
    }

    @Override
    public ProcedureModelDetailVO getDetail(Long id) {
        ProcedureModel procedureModel = procedureModelMapper.selectById(id);
        ProcedureStepAndTaskVO steps = procedureStepModelService.getByProcedureModelId(null, procedureModel.getId());
        List<Long> groupIds = procedureModelGroupService.getByProcedureModelId(id);
        return ProcedureModelConverter.INSTANCE.convertDetail(procedureModel, steps, groupIds);
    }

    @Override
    public List<PlatformUserVO> getPrincipalList(ProcedurePrincipalQueryDTO dto) {
        ProcedureModel procedureModel = procedureModelMapper.selectListByCondition2(dto);
        if (ObjectUtil.isNull(procedureModel)) {
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_EXIST);
        }
        return FeignUtils.handleRequest((data) ->
                platformUserOpenFeign.getListByRole(data), procedureModel.getPrincipal()).getData();
    }

    @Override
    public List<String> getProcessModelList(Long processVersionId) {
        return procedureModelMapper.selectProcessModelList(processVersionId);
    }

    @Override
    public void validateProcessModel(Long processVersionId) {
        if (procedureModelMapper.existsEmptyProcessModel(processVersionId)) {
            throw new BmosException(MesResponseCode.PROCEDURE_EXIST_EMPTY_GRAPH);
        }
    }

    @Override
    public List<ProcedureModelRoomOrStationVO> getProcedureModelRoomList(ProcedureModelRoomQueryDTO dto) {
        // 查询当前工序绑定的工艺id
        ProcedureModel procedureModel = procedureModelMapper.selectById(dto.getProcedureModelId());
        ProcessVersion processVersion = processVersionService.getById(procedureModel.getProcessVersionId());
        List<ProcessProductionLine> lineList =
                processProductionLineMapper.selectByProcessVersionId(processVersion.getId());
        // 修改为根据多个产线获取
        List<FactoryLineDetailFeignVO> list =
                FeignUtils.handleRequest(data -> factoryFeign.getLineDetailByLineIds(data, true),
                        CollectionUtils.convertList(lineList, ProcessProductionLine::getProductionLineId)).getData();
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        // 查询是否绑定了房间
        List<ProcedureModelRoom> procedureModelRoomList =
                procedureModelRoomMapper.selectByProcedureModelId(dto.getProcedureModelId());
        if (CollectionUtil.isEmpty(procedureModelRoomList)) {
            // 过滤掉所有房间
            list.stream().forEach(e -> e.setRoomInfoFeignVOList(Collections.emptyList()));
        } else {
            Set<Long> roomIdSet = CollectionUtils.convertSet(procedureModelRoomList, ProcedureModelRoom::getRoomId);
            // 只保留当前房间
            for (FactoryLineDetailFeignVO factoryLineDetailFeignVO : list) {
                List<RoomInfoFeignVO> roomInfoFeignVOList = factoryLineDetailFeignVO.getRoomInfoFeignVOList();
                if (CollectionUtil.isEmpty(roomInfoFeignVOList)) {
                    continue;
                }
                List<RoomInfoFeignVO> weedoutRoomInfoList = new ArrayList<>();
                for (RoomInfoFeignVO roomInfoFeignVO : roomInfoFeignVOList) {
                    if (!roomIdSet.contains(roomInfoFeignVO.getId())) {
                        continue;
                    }
                    weedoutRoomInfoList.add(roomInfoFeignVO);
                }
                factoryLineDetailFeignVO.setRoomInfoFeignVOList(weedoutRoomInfoList);
            }
        }
        return ProcedureConverter.INSTANCE.convertToProcedureModelRoomVO2(list);
    }

    @Override
    public ProcedureModel getById(Long procedureModelId) {
        return procedureModelMapper.selectById(procedureModelId);
    }

    @Override
    public List<ProcedureModelRoomVO> getProcedureModelRoomInfoTree(ProcedureModelRoomQueryDTO dto) {
        List<ProcedureModelRoom> list = procedureModelRoomMapper.selectByProcedureModelId(dto.getProcedureModelId());
        Set<String> roomIdPaths = CollectionUtils.convertSet(list, ProcedureModelRoom::getRoomIdPath);
        ProcedureModel procedureModel = procedureModelMapper.selectById(dto.getProcedureModelId());
        ProcessVersion processVersion = processVersionService.getById(procedureModel.getProcessVersionId());
        List<ProcessProductionLine> lineList =
                processProductionLineMapper.selectByProcessVersionId(processVersion.getId());
        List<FactoryLineDetailFeignVO> res =
                FeignUtils.handleRequest(data -> factoryFeign.getLineDetailByLineIds(data, false),
                        CollectionUtils.convertList(lineList, ProcessProductionLine::getProductionLineId)).getData();
        List<ProcedureModelRoomVO> result = new ArrayList<>();
        for (FactoryLineDetailFeignVO vo : res) {
            ProcedureModelRoomVO roomVO = new ProcedureModelRoomVO();
            roomVO.setId(vo.getId());
            roomVO.setName(vo.getName());
            roomVO.setCode(vo.getCode());
            roomVO.setShowName(vo.getCode() + StrUtil.DASHED + vo.getName());
            roomVO.setRoomIdPath(String.valueOf(vo.getId()));
            roomVO.setChildren(new ArrayList<>());
            if (CollUtil.isNotEmpty(vo.getRoomInfoFeignVOList())) {
                roomVO.getChildren().addAll(vo.getRoomInfoFeignVOList().stream().map(e -> {
                    ProcedureModelRoomVO child = new ProcedureModelRoomVO();
                    child.setId(e.getId());
                    child.setCode(e.getCode());
                    child.setName(e.getName());
                    child.setRoomFlag(true);
                    child.setRoomFlag(true);
                    child.setShowName(e.getCode() + StrUtil.DASHED + e.getName());
                    child.setStatus(CommonEnum.getEnumByValue(RoomStatusEnum.class, e.getStatus()));
                    child.setRoomIdPath(roomVO.getRoomIdPath() + StrUtil.DASHED + e.getId());
                    return child;
                }).collect(Collectors.toList()));
            }
            if (CollUtil.isNotEmpty(roomIdPaths)) {
                Set<String> strings = CollectionUtils.convertSet(roomVO.getChildren(),
                        ProcedureModelRoomVO::getRoomIdPath);
                strings.retainAll(roomIdPaths);
                if (CollUtil.isEmpty(strings)) {
                    continue;
                }
                roomVO.getChildren().removeIf(procedureModelRoomVO -> !roomIdPaths.contains(procedureModelRoomVO.getRoomIdPath()));
            }
            result.add(roomVO);
        }
        result.removeIf(procedureModelRoomVO -> CollUtil.isEmpty(procedureModelRoomVO.getChildren()));
        return result;
    }

    @Override
    public List<NodeVO> getNodeList(Long id, Boolean type,Long stepModelId) {
        return procedureStepModelService.getNodeListByProcedureModeId(id, type,stepModelId);
    }

    @Override
    public List<EquipmentModuleTreeNodeVO> getEquipmentTree() {
        ResponseInfo<List<EquipmentModuleTreeNodeFeignVO>> equipmentFeignTree =
                equipmentConfigFeign.getEquipmentFeignTree();
        if (CollUtil.isEmpty(equipmentFeignTree.getData())) {
            return Collections.emptyList();
        }
        List<EquipmentModuleTreeNodeVO> vo =
                ProcessTaskConverter.INSTANCE.convertToModelVo(equipmentFeignTree.getData());
        handelModuleTreeNode(vo);
        return vo;
    }

    public void handelModuleTreeNode(List<EquipmentModuleTreeNodeVO> vo) {
        vo.forEach(item -> {
            if (CollUtil.isNotEmpty(item.getInfoList())) {
                List<EquipmentModuleTreeNodeVO> children = item.getChildren();
                item.getInfoList().forEach(info -> {
                    EquipmentModuleTreeNodeVO nodeVo = ProcessTaskConverter.INSTANCE.convertToInfoVo(info);
                    nodeVo.setFlag(true);
                    children.add(nodeVo);
                });
            }
            if (CollUtil.isNotEmpty(item.getChildren())) {
                handelModuleTreeNode(item.getChildren());
            }
        });
    }

    @Override
    public List<ProcedureModelRoomVO> getProcedureModelRoomInfo(ProcedureModelRoomQueryDTO dto) {
        ProcedureModel procedureModel = procedureModelMapper.selectById(dto.getProcedureModelId());
        ProcessVersion processVersion = processVersionService.getById(procedureModel.getProcessVersionId());
        List<ProcessProductionLine> lineList =
                processProductionLineMapper.selectByProcessVersionId(processVersion.getId());
        List<FactoryLineDetailFeignVO> res =
                FeignUtils.handleRequest(data -> factoryFeign.getLineDetailByLineIds(data, false),
                        CollectionUtils.convertList(lineList, ProcessProductionLine::getProductionLineId)).getData();
        List<ProcedureModelRoomVO> result = new ArrayList<>();
        for (FactoryLineDetailFeignVO vo : res) {
            ProcedureModelRoomVO roomVO = new ProcedureModelRoomVO();
            roomVO.setId(vo.getId());
            roomVO.setName(vo.getName());
            roomVO.setCode(vo.getCode());
            roomVO.setShowName(vo.getCode() + StrUtil.DASHED + vo.getName());
            roomVO.setRoomIdPath(String.valueOf(vo.getId()));
            roomVO.setChildren(new ArrayList<>());
            if (CollUtil.isNotEmpty(vo.getRoomInfoFeignVOList())) {
                roomVO.getChildren().addAll(vo.getRoomInfoFeignVOList().stream().map(e -> {
                    ProcedureModelRoomVO child = new ProcedureModelRoomVO();
                    child.setId(e.getId());
                    child.setCode(e.getCode());
                    child.setName(e.getName());
                    child.setShowName(e.getCode() + StrUtil.DASHED + e.getName());
                    child.setRoomFlag(true);
                    child.setStatus(CommonEnum.getEnumByValue(RoomStatusEnum.class, e.getStatus()));
                    child.setRoomIdPath(roomVO.getRoomIdPath() + StrUtil.DASHED + e.getId());
                    return child;
                }).collect(Collectors.toList()));
            }
            result.add(roomVO);
        }
        result.removeIf(procedureModelRoomVO -> CollUtil.isEmpty(procedureModelRoomVO.getChildren()));
        return result;
    }

    @Override
    public List<ProcedureModelDetailVO> selectByIds(List<Long> procedureModelIdS) {
        if (CollUtil.isEmpty(procedureModelIdS)){
            return Collections.emptyList();
        }
        List<ProcedureModel> modelList = procedureModelMapper.selectByIds(procedureModelIdS);
        List<ProcedureStepModelVO> list = procedureStepModelService.selectByProcedureModelIdS(procedureModelIdS);
        Set<Long> ids = CollectionUtils.convertSet(modelList, ProcedureModel::getId);
        Map<Long, List<Long>> groupMap = procedureModelGroupService.getByProcedureModelIds(ids);
        return ProcedureModelConverter.INSTANCE.convertDetailList(modelList,list,groupMap);
    }

    @Override
    public List<ProcessSortVO> listProcessSort(Long processVersionId) {
        ProcessVersion version = processVersionService.getById(processVersionId);
        List<ProcedureModel> modelList = procedureModelMapper.selectByProcessIdAndVersion(version.getProcessId(), version.getVersion());
        if (CollUtil.isEmpty(modelList)){
            return Collections.emptyList();
        }
        List<ProcedureModel> nullSort = CollectionUtils.filterList(modelList, item -> ObjectUtil.isNull(item.getSort()));

        List<ProcessSortVO> procedureModelSort = ProcedureModelConverter.INSTANCE.convertToSortVO(modelList)
                .stream()
                .sorted(CollUtil.isEmpty(nullSort) ? Comparator.comparing(ProcessSortVO::getSort) : Comparator.comparing(ProcessSortVO::getId))
                .collect(Collectors.toList());
        //查询工序步骤信息
        List<Long> modelIdList = CollectionUtils.convertList(modelList, ProcedureModel::getId);
        List<ProcessSortVO> stepModelSort = procedureStepModelService.selectStepModelSort(modelIdList, version.getProcessId(),
                version.getVersion());
        if (CollUtil.isNotEmpty(stepModelSort)){
            Map<Long, List<ProcessSortVO>> stepMap = CollectionUtils.convertMultiMap(stepModelSort, ProcessSortVO::getProcedureModelId);
            procedureModelSort.forEach(item->{
                List<ProcessSortVO> procedureStepList = stepMap.get(item.getId());
                if (CollUtil.isEmpty(procedureStepList)){
                    return;
                }
                List<ProcessSortVO> procedureStepSort = CollectionUtils.filterList(procedureStepList, items -> ObjectUtil.isNull(items.getSort()));
                item.setProcedureStepSortList(procedureStepList.stream()
                        .sorted(CollUtil.isEmpty(procedureStepSort) ? Comparator.comparing(ProcessSortVO::getSort) :
                                Comparator.comparing(ProcessSortVO::getId))
                        .collect(Collectors.toList()));
            });
        }
        return procedureModelSort;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProcessSort(List<SaveProcessSortVO> sortList) {
        List<ProcedureModel> modelList = ProcedureModelConverter.INSTANCE.convertToSaveModel(sortList);
        procedureModelMapper.updateBatch(modelList);
        List<ProcedureStepModel> stepModelList = new ArrayList<>();
        sortList.forEach(item->{
            List<ProcedureStepModel> stepModel = ProcedureStepModelConverter.INSTANCE.convertToStepModel(item.getProcedureStepSortList());
            if (CollUtil.isNotEmpty(stepModel)){
                stepModelList.addAll(stepModel);
            }
        });
        procedureStepModelService.updateBatch(stepModelList);
    }

    @Override
    public List<ProcedureModel> getListByProcessVersionId(Long processVersionId) {
        return procedureModelMapper.selectByProcessVersion(processVersionId);
    }

    @Override
    public List<ProcessConfigVO> getTeamByProcessVersionId(Long processVersionId) {
        if (ObjectUtil.isNull(processVersionId)){
            return new ArrayList<>();
        }
        List<ProcedureModel> procedureModels = procedureModelMapper.selectByProcessVersion(processVersionId);
        if (CollUtil.isEmpty(procedureModels)){
            return new ArrayList<>();
        }
        return procedureModelGroupService.getDeleteByProcedureModelId(CollectionUtils.convertSet(procedureModels, ProcedureModel::getId));
    }

    @Override
    public List<ProcessConfigVO> getRoomListByProcessVersionId(Long processVersionId) {
        List<ProcedureModel> procedureModels = procedureModelMapper.selectByProcessVersion(processVersionId);
        if (CollUtil.isEmpty(procedureModels)){
            return new ArrayList<>();
        }
        return procedureModelRoomMapper.selectRoomIdByModelIds(CollectionUtils.convertSet(procedureModels,ProcedureModel::getId));
    }

    @Override
    public List<PlatformRoleVO> getProcedureRoleRoles(PlatformRoleListQueryDTO dto) {
        List<PlatformRoleVO> roles = platformRoleService.getRoles(dto);
        if (CollUtil.isEmpty(roles) || ObjectUtil.isNull(dto.getProcedureModelId())){
            return roles;
        }
        ProcedureModel procedureModel = procedureModelMapper.selectById(dto.getProcedureModelId());
        List<Long> roleIds = CollectionUtils.convertList(roles, PlatformRoleVO::getId);
        if (!roleIds.contains(procedureModel.getPrincipal())){
            ResponseInfo<List<FeignRoleVO>> list = roleFeign.getListByIds(Collections.singletonList(procedureModel.getPrincipal()));
            List<PlatformRoleVO> roleVo = BeanUtil.copyToList(list.getData(), PlatformRoleVO.class);
            roleVo.forEach(item->item.setDisabled(true));
            roles.addAll(roleVo);
        }
        return roles;
    }

    @Override
    public List<PlatformRoleVO> getRoleListByProcessVersionId(Long processVersionId) {
        List<ProcedureModel> models = procedureModelMapper.selectByProcessVersion(processVersionId);
        ResponseInfo<List<FeignRoleVO>> roleList = roleFeign.getListByIds(CollectionUtils.convertList(models, ProcedureModel::getPrincipal));
        List<FeignRoleVO> feignRoleVo = CollectionUtils.filterList(roleList.getData(), item -> BooleanUtil.isTrue(item.getIsDeleted()));
        return BeanUtil.copyToList(feignRoleVo, PlatformRoleVO.class);
    }

    @Override
    public List<NodeVO> getProcedureNodeList(Long procedureModelId) {
        List<NodeVO> voList = this.procedureStepModelService.getNodeListByProcedureModeId(procedureModelId, false,null);
        List<String> modelCompleteCondition = expressionService.getConfigByModelId(Collections.singletonList(procedureModelId));
        if (CollUtil.isEmpty(modelCompleteCondition)){
            return voList;
        }
        List<Long> taskId = CollectionUtils.convertList(voList, NodeVO::getId);
        List<Long> deleteTaskId = new ArrayList<>();
        modelCompleteCondition.forEach(item -> {
            ConditionDetailVO detailVO = JsonUtils.parseObject(item, ConditionDetailVO.class);
            if (!taskId.contains(detailVO.getTaskNodeId())){
                deleteTaskId.add(detailVO.getTaskNodeId());
            }
        });
        voList.addAll(procedureStepModelService.queryByIds(deleteTaskId));
        return voList;
    }


    @Override
    public List<EquipmentModuleTreeNodeVO> getStepEquipmentTree(Long stepModelId) {
        List<EquipmentModuleTreeNodeVO> equipmentTree = this.getEquipmentTree();
        List<String> conditionDetail = expressionService.getStepModelCondition(Collections.singletonList(stepModelId),
                Collections.singletonList(ConditionTypeEnum.EQUIPMENT_USE_STATE.getValue()));
        if (CollUtil.isEmpty(conditionDetail)){
            return equipmentTree;
        }
        List<Long> equipmentId = new ArrayList<>();
        conditionDetail.forEach(item->{
            ConditionDetailVO detailVO = JsonUtils.parseObject(item, ConditionDetailVO.class);
            if (BooleanUtil.isFalse(getDeleteEquipmentId(detailVO.getEquipmentId(),equipmentTree))){
                equipmentId.add(detailVO.getEquipmentId());
            }
        });
        ResponseInfo<List<EquipmentVO>> deleteEquipment = equipmentConfigFeign.getDeleteEquipment(equipmentId);
        if (CollUtil.isNotEmpty(deleteEquipment.getData())){
            List<EquipmentModuleTreeNodeVO> vos = deleteEquipment.getData().stream().map(item -> {
                EquipmentModuleTreeNodeVO vo = new EquipmentModuleTreeNodeVO();
                vo.setFlag(true);
                vo.setDisabled(true);
                vo.setId(item.getId());
                vo.setCode(item.getCode());
                vo.setName(item.getName());
                return vo;
            }).collect(Collectors.toList());
            equipmentTree.addAll(vos);
        }
        return equipmentTree;
    }

    @Override
    public List<ProcedureModelRoomVO> getStepModelRoomInfo(ProcedureModelRoomQueryDTO dto) {
        List<ProcedureModelRoomVO> procedureModelRoomInfo = this.getProcedureModelRoomInfo(dto);
        List<String> conditionDetail = expressionService.getStepModelCondition(Collections.singletonList(dto.getStepModelId()),
                Collections.singletonList(ConditionTypeEnum.ROOM_STATE.getValue()));
        if (CollUtil.isEmpty(conditionDetail)){
            return procedureModelRoomInfo;
        }
        List<ProcedureModelRoomVO> roomVoList = CollectionUtils.convertList(procedureModelRoomInfo, ProcedureModelRoomVO::getChildren,
                item -> CollUtil.isNotEmpty(item.getChildren()))
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<Long> roomIds = new ArrayList<>();
        List<ConditionDetailVO> conditionDetailVo = new ArrayList<>();
        conditionDetail.forEach(item->{
            ConditionDetailVO detailVO = JsonUtils.parseObject(item, ConditionDetailVO.class);
            if (BooleanUtil.isFalse(getDeleteRoomId(detailVO.getRoomId(),roomVoList,detailVO.getProcedureName()))){
                roomIds.add(detailVO.getRoomId());
                conditionDetailVo.add(detailVO);
            }
        });
        ResponseInfo<List<FactoryRoomFeignVO>> listResponseInfo = factoryFeign.queryRoomListByRoomIds(roomIds);
        if (CollUtil.isNotEmpty(listResponseInfo.getData())){
            Map<Long, List<ConditionDetailVO>> map = CollectionUtils.convertMultiMap(conditionDetailVo, ConditionDetailVO::getRoomId);
            listResponseInfo.getData().forEach(item -> {
                List<ConditionDetailVO> detailList = map.get(item.getId());
                detailList.forEach(list->{
                    ProcedureModelRoomVO vo = new ProcedureModelRoomVO();
                    vo.setCode(item.getCode());
                    vo.setName(item.getName());
                    vo.setId(item.getId());
                    vo.setRoomFlag(true);
                    vo.setRoomIdPath(list.getProcedureName());
                    vo.setDisabled(true);
                    vo.setShowName(item.getCode() + StrUtil.DASHED + item.getName());
                    procedureModelRoomInfo.add(vo);
                });
            });
        }
        return procedureModelRoomInfo;
    }

    @Override
    public List<NodeVO> getProcedureModelList(Long versionId, Long stepModelId) {
        List<NodeVO> voList = BeanUtil.copyToList(procedureModelMapper.selectByProcessVersion(versionId), NodeVO.class);
        List<String> conditions = expressionService.getStepModelCondition(Collections.singletonList(stepModelId),
                Arrays.asList(ConditionTypeEnum.STEP_NODE_COMPLETE.getValue(), ConditionTypeEnum.TASK_NODE_COMPLETE.getValue()));
        if (CollUtil.isNotEmpty(conditions)){
            List<Long> modelId = new ArrayList<>();
            List<Long> procedureModelIdList = CollectionUtils.convertList(voList, NodeVO::getId);
            conditions.forEach(item->{
                ConditionDetailVO detailVO = JsonUtils.parseObject(item, ConditionDetailVO.class);
                if (!procedureModelIdList.contains(detailVO.getProcedureId())){
                    modelId.add(detailVO.getProcedureId());
                }
            });
            if (CollUtil.isNotEmpty(modelId)){
                List<NodeVO> models = this.getListByDeleteIds(modelId);
                models.forEach(item->item.setDisabled(true));
                voList.addAll(models);
            }
        }
        return voList;
    }

    @Override
    public List<NodeVO> getListByDeleteIds(List<Long> procedureId) {
        return procedureModelMapper.getListByDeleteIds(procedureId);
    }

    @Override
    public List<ProcessConfigVO> getRoomListByProcedureModelId(Long procedureModelId) {
        if (ObjectUtil.isNull(procedureModelId)){
            return new ArrayList<>();
        }
        return procedureModelRoomMapper.selectRoomIdByModelIds(new HashSet<>(Collections.singletonList(procedureModelId)));
    }

    private Boolean getDeleteRoomId(Long roomId,List<ProcedureModelRoomVO> roomVOS,String roomPatch){
        if (CollUtil.isEmpty(roomVOS)){
            return false;
        }
        for (ProcedureModelRoomVO item :roomVOS){
            if (item.getId().equals(roomId) && item.getRoomIdPath().equals(roomPatch)){
                return true;
            }
            if (CollUtil.isNotEmpty(item.getChildren())){
                getDeleteRoomId(roomId,item.getChildren(),roomPatch);
            }
        }
        return false;
    }

    private Boolean getDeleteEquipmentId(Long equipmentId,List<EquipmentModuleTreeNodeVO> equipmentTree){
        if (CollUtil.isEmpty(equipmentTree)){
            return false;
        }
        for (EquipmentModuleTreeNodeVO item :equipmentTree){
            if (BooleanUtil.isTrue(item.getFlag()) && item.getId().equals(equipmentId)){
                return true;
            }
            if (CollUtil.isNotEmpty(item.getChildren())){
                getDeleteEquipmentId(equipmentId,item.getChildren());
            }
        }
       return false;
    }


    private String createDeployment(ProcedureModel procedureModel, ProcessVersion version, CreateDeploymentCmd cmd) {
        String deployment = workflowService.createDeployment(cmd);
        BindDeploymentDTO param = BindDeploymentDTO.builder()
                .superDeploymentId(version.getProcessModelId())
                .currentDeploymentId(deployment)
                .nodeId(procedureModel.getNodeId())
                .build();
        //todo 绑定
        workflowService.bindDeployment(param);
        return deployment;
    }
}
