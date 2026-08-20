package com.bmos.mes.service.weigh.centre.execute.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.common.enums.ingredient.WeighMode;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighProcess;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.TaskStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.components.BusinessComponentManager;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.ingredient.DiffUtil;
import com.bmos.mes.service.ingredient.weigh.dto.WeighLogSaveDTO;
import com.bmos.mes.service.ingredient.weigh.service.WeighLogService;
import com.bmos.mes.service.ingredient.weigh.vo.WeighBalanceEquipment;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialReserveDTO;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialReserveMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.tag.convert.ScanDeviceConvert;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mes.service.weigh.centre.config.service.IWeighCentreService;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreDetailVO;
import com.bmos.mes.service.weigh.centre.execute.dto.*;
import com.bmos.mes.service.weigh.centre.execute.mapper.IWeighExecuteConsumeRecordMapper;
import com.bmos.mes.service.weigh.centre.execute.mapper.IWeighExecuteWeighRecordMapper;
import com.bmos.mes.service.weigh.centre.execute.model.WeighExecuteConsumeRecord;
import com.bmos.mes.service.weigh.centre.execute.model.WeighExecuteWeighRecord;
import com.bmos.mes.service.weigh.centre.execute.service.IWeighExecuteService;
import com.bmos.mes.service.weigh.centre.execute.vo.*;
import com.bmos.mes.service.weigh.centre.input.mapper.IWeighInputRecordMapper;
import com.bmos.mes.service.weigh.centre.input.model.WeighInputRecord;
import com.bmos.mes.service.weigh.centre.requirement.convert.WeighRequirementConvert;
import com.bmos.mes.service.weigh.centre.requirement.mapper.IWeighRequirementMapper;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighRequirement;
import com.bmos.mes.service.weigh.centre.task.mapper.IWeighTaskMapper;
import com.bmos.mes.service.weigh.centre.task.model.WeighTask;
import com.bmos.mes.service.workflow.behavior.CustomTaskBehaviorFactory;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bmos.mes.common.enums.storage.StorageOperateTypeEnum.MATERIAL_ODD_WEIGHT;
import static com.bmos.mes.common.enums.storage.StorageOperateTypeEnum.MATERIAL_WEIGHT;

/**
 * 称量执行 service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 16:15
 */
@Service
@Slf4j
public class WeighExecuteServiceImpl implements IWeighExecuteService {

    private static final String LOG_PREFIX = "[称量执行]";

    @Resource
    private IWeighRequirementMapper weighRequirementMapper;

    @Resource
    private IWeighTaskMapper weighTaskMapper;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;

    @Resource
    private IStorageMaterialBatchService storageMaterialBatchService;

    @Resource
    private IWeighExecuteConsumeRecordMapper weighExecuteConsumeRecordMapper;

    @Resource
    private IWeighExecuteWeighRecordMapper weighExecuteWeighRecordMapper;

    @Resource
    private IStorageMaterialReserveMapper storageMaterialReserveMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ProductFormulaMaterialMapper formulaMaterialMapper;

    @Resource
    private ICargoPositionService cargoPositionService;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private WeighLogService weighLogService;

    @Resource
    private BusinessComponentManager businessComponentManager;

    @Resource
    private IWeighInputRecordMapper weighInputRecordMapper;

    @Resource
    private IWeighCentreService weighCentreService;
    @Autowired
    private CustomTaskBehaviorFactory taskBehaviorFactory;

    @Override
    public WeighExecuteTaskDetailVO queryTaskById(Long taskId) {
        WeighExecuteTaskDetailVO task = weighTaskMapper.selectWeighExecuteTaskDetailById(taskId);
        if (task == null) {
            return null;
        }
        List<WeighExecuteRequirementVO> requirements = weighRequirementMapper.selectExecuteRequirementListByTaskId(taskId);
        List<WeighExecuteWeighRecord> records = weighExecuteWeighRecordMapper.selectListByRequirementIds(requirements.stream().map(WeighExecuteRequirementVO::getId).collect(Collectors.toList()))
                .stream().filter(item -> Objects.equals(item.getWeighType(), WeighType.MAIN)).collect(Collectors.toList());
        Map<Long, List<WeighExecuteWeighRecord>> groups = records.stream().collect(Collectors.groupingBy(WeighExecuteWeighRecord::getRequirementId, Collectors.toList()));
        requirements.forEach(item -> {
            item.setWeighed(PrecisionHelper.precision(groups.get(item.getId()) == null ? BigDecimal.ZERO : groups.get(item.getId())
                    .stream()
                    .map(WeighExecuteWeighRecord::getNetWeight)
                    .reduce(BigDecimal.ZERO, BigDecimal::add), item.getUnitId()));
            item.setUnWeighed(checkZero(item.getRequirementQuantity().subtract(item.getWeighed())));
        });
        task.setRequirements(requirements);
        requirements.stream()
                .filter(item -> Objects.equals(item.getWeighStatus(), RequirementWeighStatusEnum.PROCESSING)
                        || (Objects.equals(item.getWeighStatus(), RequirementWeighStatusEnum.FINISHED_WEIGH) && Objects.equals(item.getWeighProcess(), RequirementWeighProcess.ODD)))
                .findAny()
                .ifPresent(task::setWeighExecuteRequirement);
        BigDecimal sum = getSum(requirements, WeighExecuteRequirementVO::getWeighed);
        task.setWeighed(sum);
        task.setUnWeighed(checkZero(task.getRequirementQuantity().subtract(task.getWeighed())));
        WeighCentreDetailVO weighCentreDetailVO = weighCentreService.queryCentreInfo(task.getWeighCentreId());
        if (weighCentreDetailVO == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NOT_EXIST);
        }
        task.setStation(weighCentreDetailVO.getStationIds());
        // 修改当前任务称量中的物料批次id为当前称量需求中的物料批次id
        task.setStorageMaterialBatchId(Objects.nonNull(task.getWeighExecuteRequirement()) ?
                task.getWeighExecuteRequirement().getStorageMaterialBatchId() : task.getStorageMaterialBatchId());
        return task;
    }

    @Override
    public WeighExecuteRequirementDetailVO queryRequirementById(Long requirementId) {
        WeighExecuteRequirementDetailVO requirement = weighRequirementMapper.selectWeighExecuteRequirementDetailById(requirementId);
        if (requirement == null) {
            return null;
        }
        requirement.setTargetTotalQuantity(PrecisionHelper.precision(requirement.getTargetTotalQuantity(), requirement.getUnitId()));
        ProductFormulaMaterial productFormulaMaterial = formulaMaterialMapper.selectById(requirement.getFormulaMaterialId());
        if (productFormulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXIST);
        }

        WeighTask weighTask = weighTaskMapper.selectById(requirement.getTaskId());
        if (weighTask == null){
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        Optional.of(weighTask)
                .map(WeighTask::getStorageMaterialBatchId)
                .map(item -> storageMaterialBatchService.getById(weighTask.getStorageMaterialBatchId()))
                .ifPresent(item -> {
                    requirement.setStorageMaterialBatchId(item.getId());
                    requirement.setStorageMaterialBatchNo(item.getMaterialBatchNo());
                });

        // 需求总消耗量
        List<WeighExecuteConsumeRecord> consumeRecordList = weighExecuteConsumeRecordMapper.selectListByTaskId(requirement.getTaskId());
        BigDecimal consumeSum = PrecisionHelper.precision(getSum(consumeRecordList, WeighExecuteConsumeRecord::getConsumeQuantity), requirement.getUnitId());
        requirement.setConsumeTotalQuantity(consumeSum);

        // 批次总消耗量
        List<WeighExecuteConsumeRecord> batchConsumeRecordList = consumeRecordList.stream()
                .filter(item -> Objects.equals(item.getStorageMaterialBatchId(), weighTask.getStorageMaterialBatchId()))
                .collect(Collectors.toList());
        BigDecimal batchConsumeSum = PrecisionHelper.precision(getSum(batchConsumeRecordList, WeighExecuteConsumeRecord::getConsumeQuantity), requirement.getUnitId());
        requirement.setBatchConsumeTotalQuantity(batchConsumeSum);

        // 物料总称量量
        List<WeighExecuteWeighRecord> mainWeighRecordList = weighExecuteWeighRecordMapper.selectListByRequirementId(requirementId);
        BigDecimal mainWeighSum = getSum(mainWeighRecordList, WeighExecuteWeighRecord::getNetWeight);

        List<WeighExecuteWeighRecord> taskRecordList = weighExecuteWeighRecordMapper.queryListByTaskId(weighTask.getId());
        List<WeighExecuteWeighRecord> batchWeighList = taskRecordList.stream().filter(item -> Objects.equals(item.getStorageMaterialBatchId(), weighTask.getStorageMaterialBatchId())).collect(Collectors.toList());
        BigDecimal batchWeighSum = getSum(batchWeighList, WeighExecuteWeighRecord::getNetWeight);

        if (Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.MAIN)
                || Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.CHANGE_REQUIREMENT)) {
            // 物料称量查询
            // 已称量
            requirement.setWeighedQuantity(PrecisionHelper.precision(mainWeighSum, requirement.getUnitId()));

            // 未称量 = 目标量 - 已称量
            requirement.setUnWeighedQuantity(checkZero(requirement.getTargetTotalQuantity().subtract(requirement.getWeighedQuantity())));

            // 物料总量 - 已称量
            requirement.setRemainingQuantity(checkZero(batchConsumeSum.subtract(batchWeighSum)));

            // 使用总量计算允差
            WeighExecuteDiff weighExecuteDiff = buildDiff(requirement.getTargetTotalQuantity(), productFormulaMaterial, requirement.getWeighProcess());
            // 允差减去已称量的
            weighExecuteDiff.diffSubtract(requirement.getWeighedQuantity());
            log.info("{}允差信息:{}", LOG_PREFIX, weighExecuteDiff);
            // 允差信息
            requirement.setDiff(weighExecuteDiff);
        } else if (Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.ODD) || Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.ODD_CHANGE_REQUIREMENT)) {
            // 余料称量查询

            // 当前物料总称量量
            List<WeighExecuteWeighRecord> mainBatchWeighRecordList = weighExecuteWeighRecordMapper.queryListByTaskIdAndType(requirement.getTaskId() , WeighType.MAIN)
                    .stream()
                    .filter(item -> Objects.equals(item.getStorageMaterialBatchId(), weighTask.getStorageMaterialBatchId()))
                    .collect(Collectors.toList());
            BigDecimal mainBatchWeighSum = getSum(mainBatchWeighRecordList, WeighExecuteWeighRecord::getNetWeight);

            // 余料称量目标量 = 当前批次物料总量 - 当前批次已物料称量称量的量
            BigDecimal targetTotalQuantity = checkZero(batchConsumeSum.subtract(mainBatchWeighSum));
            requirement.setTargetTotalQuantity(PrecisionHelper.precision(targetTotalQuantity, requirement.getUnitId()));

            // 该物料批次的余料称量已称量
            List<WeighExecuteWeighRecord> oddBatchWeighRecordList = weighExecuteWeighRecordMapper.queryListByTaskIdAndType(requirement.getTaskId(), WeighType.ODD);
            BigDecimal oddBatchWeighSum = getSum(oddBatchWeighRecordList, WeighExecuteWeighRecord::getNetWeight);
            requirement.setWeighedQuantity((PrecisionHelper.precision(oddBatchWeighSum, requirement.getUnitId())));

            // 未称量 = 目标量 - 已称量
            requirement.setUnWeighedQuantity(checkZero(requirement.getTargetTotalQuantity().subtract(requirement.getWeighedQuantity())));

            // 物料总量 - 物料已称量 - 余料已称量
            requirement.setRemainingQuantity(batchConsumeSum.subtract(batchWeighSum));

            // 使用总量计算允差
            WeighExecuteDiff weighExecuteDiff = buildDiff(requirement.getTargetTotalQuantity(), productFormulaMaterial, requirement.getWeighProcess());

            // 允差减去已称量的
            weighExecuteDiff.diffSubtract(requirement.getWeighedQuantity());
            log.info("{}允差信息:{}", LOG_PREFIX, weighExecuteDiff);
            // 允差信息
            requirement.setDiff(weighExecuteDiff);
        } else {
            requirement.setDiff(new WeighExecuteDiff());
        }
        return requirement;
    }

    @Override
    public List<WeighExecutePendingRequirementSimpleVO> queryPendingRequirementListByTaskIds(Long taskId) {
        if (taskId == null) {
            return new ArrayList<>();
        }

        List<WeighRequirement> list = weighRequirementMapper.selectListByTaskId(taskId)
                .stream()
                .filter(item -> Objects.equals(item.getWeighStatus(), RequirementWeighStatusEnum.PENDING) || Objects.equals(item.getWeighStatus(), RequirementWeighStatusEnum.FINISHED_WEIGH) && Objects.equals(item.getWeighProcess(), RequirementWeighProcess.ODD))
                .filter(item -> !Objects.equals(item.getRequirementStatus(), RequirementStatusEnum.EXPIRED))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return WeighRequirementConvert.INSTANCE.convertToRequirementSimpleVO(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeSureWeigh(WeighExecuteMakeSureWeighDTO makeSureWeighDTO) {
        WeighRequirement requirement = weighRequirementMapper.selectById(makeSureWeighDTO.getRequirementId());
        if (requirement == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NOT_EXIST);
        }
        WeighTask task = weighTaskMapper.selectById(requirement.getWeighRequirementTaskId());
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        if (makeSureWeighDTO.getWeigherId() != null
                && makeSureWeighDTO.getReCheckerId() != null
                && task.getPreReCheckerId() == null
                && task.getPreWeigherId() == null
        ) {
            // 首次确认切换称量人
            WeighExecuteChangeWeigherDTO changeWeigherDTO = new WeighExecuteChangeWeigherDTO();
            changeWeigherDTO.setTaskId(task.getId());
            changeWeigherDTO.setWeigherId(makeSureWeighDTO.getWeigherId());
            changeWeigherDTO.setReCheckerId(makeSureWeighDTO.getReCheckerId());
            changeWeigherDTO.setRemark(makeSureWeighDTO.getRemark());
            changeWeigher(changeWeigherDTO, false);
        }
        if (!requirement.isProcessing()) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NOT_ALLOWED_MAKE_SURE);
        }
        WeighExecuteAddConsumeMaterialWeighDTO dto = new WeighExecuteAddConsumeMaterialWeighDTO();
        dto.setRequirementId(makeSureWeighDTO.getRequirementId());
        dto.setConsumeStorateMaterialIdList(makeSureWeighDTO.getConsumeStorateMaterialIdList());

        addConsumeStorageMaterial(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addConsumeStorageMaterial(WeighExecuteAddConsumeMaterialWeighDTO dto) {
        WeighRequirement requirement = weighRequirementMapper.selectById(dto.getRequirementId());
        if (requirement == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NOT_EXIST);
        }
        WeighTask weighTask = weighTaskMapper.selectById(requirement.getWeighRequirementTaskId());
        if (weighTask == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }

        WeighType weighType;
        if (Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.MAIN) || Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.CHANGE_REQUIREMENT)) {
            weighType = WeighType.MAIN;
        } else {
            weighType = WeighType.ODD;
        }

        Plan plan = planMapper.selectById(requirement.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        List<Long> consumeStorateMaterialIdList = dto.getConsumeStorateMaterialIdList();
        if (CollectionUtil.isNotEmpty(consumeStorateMaterialIdList)) {
            // 保存称量记录并消耗
            Long batchId = weighConsume(consumeStorateMaterialIdList,
                    weighType,
                    requirement.getRemark(),
                    requirement.getWeighRequirementTaskId(),
                    requirement.getId(),
                    requirement.getProductPlanId(),
                    requirement.getUnitId(),
                    plan
            );
            if (batchId != null) {
                // 根据消耗的批次号更新需求批次号
                requirement.setStorageMaterialBatchId(batchId);
                weighTask.setStorageMaterialBatchId(batchId);
            }
        }
        weighTaskMapper.updateById(weighTask);
        if (Objects.equals(requirement.getWeighStatus(), RequirementWeighStatusEnum.FINISHED_WEIGH) || Objects.equals(requirement.getWeighStatus(), RequirementWeighStatusEnum.FINISHED_SIGN)){
            return;
        }
        // 称量中
        requirement.setWeighStatus(RequirementWeighStatusEnum.PROCESSING);
        requirement.setRequirementStatus(RequirementStatusEnum.WEIGHING);
        if (Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.CHANGE_REQUIREMENT)){
            requirement.setWeighProcess(RequirementWeighProcess.MAIN);
        }else if (Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.ODD_CHANGE_REQUIREMENT)){
            requirement.setWeighProcess(RequirementWeighProcess.ODD);
        }
        weighRequirementMapper.updateById(requirement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WeighExecuteWeighResult weighAndPrint(WeighExecuteWeighAndPrintDTO weighAndPrintDTO) {

        WeighRequirement requirement = weighRequirementMapper.selectById(weighAndPrintDTO.getRequirementId());
        if (requirement == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NOT_EXIST);
        }

        WeighTask task = weighTaskMapper.selectById(requirement.getWeighRequirementTaskId());
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }

        // 待称量列表
        List<WeighExecutePendingRequirementSimpleVO> pendingList = this.queryPendingRequirementListByTaskIds(requirement.getWeighRequirementTaskId());

        // 称量人校验是否匹配
        if (!Objects.equals(requirement.getWeigherId(), SysUserHolder.getUser().getUserId())) {
            throw new BmosException(MesResponseCode.WEIGHER_NOT_MATCH);
        }

        // 生产计划
        Plan plan = planMapper.selectById(requirement.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }

        // 配方物料
        ProductFormulaMaterial productFormulaMaterial = formulaMaterialMapper.selectById(requirement.getFormulaMaterialId());
        if (productFormulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXIST);
        }

        // 当前称量类型 物料/余料
        WeighType weighType;
        if (Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.MAIN) || Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.CHANGE_REQUIREMENT)) {
            weighType = WeighType.MAIN;
        } else if (Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.ODD) || Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.ODD_CHANGE_REQUIREMENT)) {
            weighType = WeighType.ODD;
        } else {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_ENOUGH);
        }

        // 查询最新的称量状态信息
        WeighExecuteRequirementDetailVO detail = queryRequirementById(requirement.getId());
        if (detail == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NOT_EXIST);
        }

        BigDecimal[] diff = detail.getDiff().getToleranceDiff();
        // 是否配置了允差
        boolean withDiff = withDiff(diff);
        // 处理单边允差
        if (withDiff) {
            // 配了允差 但是没配置下限
            if (diff[0] == null) {
                diff[0] = diff[1];
            }
            // 配了允差 但是没配置上限
            if (diff[2] == null) {
                diff[2] = diff[1];
            }
        }else {
            // 没配允差 按0处理
            diff[0] = diff[1];
            diff[2] = diff[1];
        }

        // 本次称量值
        BigDecimal netWeight = weighAndPrintDTO.getNetWeight();

        List<WeighExecuteConsumeRecord> comsumeList = weighExecuteConsumeRecordMapper.selectListByTaskIdAndBatchId(task.getId(), task.getStorageMaterialBatchId());
        BigDecimal consumeSum = getSum(comsumeList, WeighExecuteConsumeRecord::getConsumeQuantity);
        List<WeighExecuteWeighRecord> weighedList = weighExecuteWeighRecordMapper.queryListByTaskIdAndBatchId(task.getId(), task.getStorageMaterialBatchId());
        BigDecimal weighedSum = getSum(weighedList, WeighExecuteWeighRecord::getNetWeight);

        // 根据重量值判断是否满足称量要求
        boolean updateProcess = validateWeighQuantity(weighType, withDiff, netWeight, consumeSum, weighedSum, diff, requirement, pendingList);
        // 更新流程
        if (updateProcess) {
            weighRequirementMapper.updateById(requirement);
        }
        // 查询物料相关扩展信息， 包括货位id、容器id、容器名称
        StorageMaterialExtInfo extInfo = getStorageMaterialExtInfos(weighAndPrintDTO);

        // 保存预定物料件
        StorageMaterial storageMaterial = createStorageMaterial(weighAndPrintDTO.getNetWeight(), productFormulaMaterial, requirement, task, extInfo);
        storageMaterialService.save(storageMaterial);

        // 保存称量记录
        WeighExecuteWeighRecord weighRecord = createWeighRecord(weighAndPrintDTO, requirement, task, storageMaterial, weighType, detail.getStorageMaterialBatchNo(), extInfo);
        weighExecuteWeighRecordMapper.insert(weighRecord);

        StorageOperateTypeEnum operateTypeEnum;
        if (Objects.equals(weighType, WeighType.MAIN)){
            operateTypeEnum = MATERIAL_WEIGHT;
            // 保存待投入记录 余料称量不需要
            WeighInputRecord pendingInputRecord = createPendingInputRecord(productFormulaMaterial, weighRecord, requirement, plan);
            weighInputRecordMapper.insert(pendingInputRecord);
        }else {
            operateTypeEnum = MATERIAL_ODD_WEIGHT;
        }

        // 记录物料/货位日志
        storageMaterialPositionLogService.saveLog(StorageMaterialPositionLogDTO.builder()
                .storageMaterialId(storageMaterial.getId())
                .operateType(operateTypeEnum)
                .quantity(weighAndPrintDTO.getNetWeight())
                .unitId(weighRecord.getUnitId())
                .senderId(requirement.getWeigherId())
                .receiverId(requirement.getReCheckerId())
                .productName(plan.getProductName())
                .productCode(plan.getProductMergeCode())
                .productBatchNo(plan.getBatchNo())
                .tareWeight(weighRecord.getTareWeight())
                .grossWeight(weighRecord.getGrossWeight())
                .materialPositionId(weighAndPrintDTO.getMaterialPositionId())
                .build());

        if (Objects.equals(weighType, WeighType.MAIN)) {
            // 物料称量 需要预定
            // 预定物料件
            storageMaterialService.reserve(StorageMaterialReserveDTO.builder()
                    .storageMaterialId(storageMaterial.getId())
                    .processId(plan.getProcessId())
                    .batchId(plan.getId())
                    .productId(plan.getProductId())
                    .reCheckerId(requirement.getReCheckerId())
                    .operatorId(requirement.getWeigherId())
                    .remark(requirement.getRemark())
                    .build());
        }

        // 保存称量日志
        WeighLogSaveDTO saveLogDTO = createWeighLogDTO(weighAndPrintDTO, weighRecord, weighType, plan, detail, extInfo.containerId);
        weighLogService.saveLog(saveLogDTO);

        // 需求的称量历史
        List<WeighExecuteWeighRecord> weighExecuteWeighRecords = weighExecuteWeighRecordMapper.selectListByRequirementId(requirement.getId());

        // 查询单次称量结果
        WeighExecuteWeighResult result = buildReturnResult(storageMaterial, plan, requirement.getWeighProcess(), weighType, weighExecuteWeighRecords, detail, weighRecord.getNetWeight());

        // 确认编号
        storageMaterialService.confirmSerial(storageMaterial.getNo());
        return result;
    }

    /**
     * 创建待投料记录
     *
     * @param productFormulaMaterial 配方物料
     * @param weighRecord            称量记录
     * @param requirement            需求信息
     * @param plan                   生产计划信息
     * @return 待投料记录
     */
    private WeighInputRecord createPendingInputRecord(ProductFormulaMaterial productFormulaMaterial, WeighExecuteWeighRecord weighRecord, WeighRequirement requirement, Plan plan) {
        WeighInputRecord weighInputRecord = new WeighInputRecord();
        weighInputRecord.setFormulaMaterialId(productFormulaMaterial.getId());
        weighInputRecord.setMaterialId(productFormulaMaterial.getMaterialId());
        weighInputRecord.setStorageMaterialBatchId(weighRecord.getStorageMaterialBatchId());
        weighInputRecord.setStorageMaterialId(weighRecord.getStorageMaterialId());
        weighInputRecord.setQuantity(weighRecord.getNetWeight());
        weighInputRecord.setUnitId(weighRecord.getUnitId());
        weighInputRecord.setRequirementId(requirement.getId());
        weighInputRecord.setProductPlanId(plan.getId());
        return weighInputRecord;
    }


    /**
     * 创建称量记录
     *
     * @param weighAndPrintDTO       参数
     * @param requirement            需求
     * @param storageMaterial        物料件信息
     * @param weighType              称量类型
     * @param storageMaterialBatchNo 物料批号
     * @param extInfo 物料扩展信息 包括容器id、容器名称、货位id、货位名称
     * @return 创建称量记录
     */
    private @NotNull WeighExecuteWeighRecord createWeighRecord(WeighExecuteWeighAndPrintDTO weighAndPrintDTO,
                                                               WeighRequirement requirement,
                                                               WeighTask task,
                                                               StorageMaterial storageMaterial,
                                                               WeighType weighType,
                                                               String storageMaterialBatchNo,
                                                               StorageMaterialExtInfo extInfo) {
        WeighExecuteWeighRecord weighRecord = new WeighExecuteWeighRecord();
        weighRecord.setTaskId(requirement.getWeighRequirementTaskId());
        weighRecord.setRequirementId(requirement.getId());
        weighRecord.setProductPlanId(requirement.getProductPlanId());
        weighRecord.setGrossWeight(weighAndPrintDTO.getGrossWeight());
        weighRecord.setNetWeight(weighAndPrintDTO.getNetWeight());
        weighRecord.setTareWeight(weighRecord.getGrossWeight().subtract(weighRecord.getNetWeight()));
        weighRecord.setUnitId(requirement.getUnitId());
        weighRecord.setStorageMaterialBatchId(task.getStorageMaterialBatchId());
        weighRecord.setStorageMaterialBatchNo(storageMaterialBatchNo);
        weighRecord.setStorageMaterialId(storageMaterial.getId());
        weighRecord.setStorageMaterialNo(storageMaterial.getNo());
        weighRecord.setWeighType(weighType);
        weighRecord.setWeighMode(WeighMode.getByValue(weighAndPrintDTO.getWeighMode()));
        weighRecord.setSignStatus(WeighSignStatus.UN_SIGNED);
        weighRecord.setWeigherId(requirement.getWeigherId());
        weighRecord.setReCheckerId(requirement.getReCheckerId());
        weighRecord.setRemark(requirement.getRemark());
        weighRecord.setWeighTime(LocalDateTime.now());
        if (extInfo.containerId != null){
            weighRecord.setContainerName(extInfo.containerName + "-" + extInfo.containerCode);
        }
        if (extInfo.positionId != null){
            weighRecord.setMaterialPositionName(extInfo.positionName + "-" + extInfo.positionCode);
        }
        return weighRecord;
    }

    private @NotNull StorageMaterial createStorageMaterial(BigDecimal netWeight, ProductFormulaMaterial productFormulaMaterial, WeighRequirement requirement, WeighTask task, StorageMaterialExtInfo extInfo) {
        StorageMaterial storageMaterial = new StorageMaterial();
        storageMaterial.setMaterialId(productFormulaMaterial.getMaterialId());
        storageMaterial.setStorageMaterialBatchId(task.getStorageMaterialBatchId());
        storageMaterial.setMaterialPositionId(extInfo.positionId);
        if (extInfo.containerId != null){
            storageMaterial.setContainerId(extInfo.containerId);
            storageMaterial.setContainer(extInfo.containerCode + "-" + extInfo.containerName);
        }
        storageMaterial.setNo(storageMaterialService.getSerial());
        storageMaterial.setInitQuantity(unitCache.toBasic(netWeight, productFormulaMaterial.getUnitId()));
        storageMaterial.setAvailableQuantity(storageMaterial.getInitQuantity());
        storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
        storageMaterial.setReserveQuantity(BigDecimal.ZERO);
        CacheUnit unit = unitCache.getGlobalUnit(productFormulaMaterial.getUnitId());
        if (unit != null) {
            if (unit.getExtend()) {
                storageMaterial.setUnitId(unit.getParentUnitId());
                storageMaterial.setUnitExtendId(unit.getUnitId());
            } else {
                storageMaterial.setUnitId(unit.getUnitId());
            }
        }
        storageMaterial.setSignStatus(WeighSignStatus.UN_SIGNED);
        storageMaterial.setProductPlanId(requirement.getProductPlanId());
        return storageMaterial;
    }

    /**
     * 查询物料相关扩展信息， 包括货位id、容器id、容器名称
     *
     * @param weighAndPrintDTO
     * @return 物料相关扩展信息， 包括货位id、容器id、容器名称
     */
    private @NotNull StorageMaterialExtInfo getStorageMaterialExtInfos(WeighExecuteWeighAndPrintDTO weighAndPrintDTO) {
        CargoPosition cargoPosition = null;
        if (weighAndPrintDTO.getMaterialPositionId() != null) {
            cargoPosition = cargoPositionService.getByIdWithPermission(weighAndPrintDTO.getMaterialPositionId());
            if (cargoPosition == null) {
                throw new BmosException(MesResponseCode.CARGO_POSITION_NOT_EXIST);
            }
        }
        EquipmentInfoFeignVO container = null;
        if (weighAndPrintDTO.getContainerId() != null) {
            container = FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data), weighAndPrintDTO.getContainerId()).getData();
            if (container == null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_NOT_EXIST);
            }
            StorageMaterial existContainer = storageMaterialService.selectStorageMaterialByContainerId(container.getId());
            if (existContainer != null) {
                throw new BmosException(MesResponseCode.STORAGE_MATERIAL_CONTAINER_OCCUPY);
            }
        }
        return new StorageMaterialExtInfo(cargoPosition, container);
    }

    private static boolean validateWeighQuantity(WeighType weighType,
                                                 boolean withDiff,
                                                 BigDecimal netWeight,
                                                 BigDecimal consumeTotalQuantity,
                                                 BigDecimal weighedTotalQuantity,
                                                 BigDecimal[] diff,
                                                 WeighRequirement requirement,
                                                 List<WeighExecutePendingRequirementSimpleVO> pendingList) {
        boolean updateProcess = false;

        if (Objects.equals(weighType, WeighType.MAIN)) {
            // 物料称量

            // 判断称量结果是否超出了（物料总量+允差上限）
            if ((netWeight.add(weighedTotalQuantity)).compareTo(consumeTotalQuantity.add(diff[2]).subtract(diff[1])) > 0) {
                throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_INPUT_NOT_ENOUGH);
            }

            if ((withDiff && netWeight.compareTo(diff[0]) >= 0 && netWeight.compareTo(diff[2]) <= 0)
                    || (!withDiff && netWeight.compareTo(diff[1]) == 0)) {
                log.info("{}物料称量：称量结果{}在允差范围{}内，结束称量，进入余料称量", LOG_PREFIX, netWeight, Arrays.toString(diff));
                // 完成配料称量(允差范围内)
                if (CollectionUtil.isEmpty(pendingList)){
                    // 是最后一个需求 进入余料称量
                    requirement.setWeighProcess(RequirementWeighProcess.ODD);
                    requirement.setWeighStatus(RequirementWeighStatusEnum.FINISHED_WEIGH);
                    requirement.setRequirementStatus(RequirementStatusEnum.WEIGHED);
                } else {
                    // 切换批次
                    requirement.setWeighProcess(RequirementWeighProcess.CHANGE_REQUIREMENT);
                    requirement.setWeighStatus(RequirementWeighStatusEnum.FINISHED_WEIGH);
                    requirement.setRequirementStatus(RequirementStatusEnum.WEIGHED);
                }
                updateProcess = true;
            } else if ((withDiff && netWeight.compareTo(diff[2]) > 0)
                    || (!withDiff && netWeight.compareTo(diff[1]) > 0)) {
                log.info("{}物料称量：称量结果{}超出允差范围{}", LOG_PREFIX, netWeight, Arrays.toString(diff));
                // 超出批次目标量范围
                throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_OVER_TARGET);
            } else {
                log.info("{}物料称量：称量结果{}不满足允差{}, 继续配料称量", LOG_PREFIX, netWeight, Arrays.toString(diff));
            }
        } else {
            // 余料称量
            if ((withDiff && netWeight.compareTo(diff[0]) >= 0)
                    || (!withDiff && netWeight.compareTo(diff[1]) >= 0)) {
                // 完成余料称量(允差范围内 或者大于下限即可（因为有超出的情况）)
                log.info("{}余料称量：称量结果{}在允差范围{}内，结束称量，完成余料称量", LOG_PREFIX, netWeight, Arrays.toString(diff));
                requirement.setWeighProcess(RequirementWeighProcess.FINISHED_WEIGH);
                requirement.setWeighStatus(RequirementWeighStatusEnum.FINISHED_WEIGH);
                requirement.setRequirementStatus(RequirementStatusEnum.WEIGHED);
                updateProcess = true;
            } else {
                log.info("{}余料称量：称量结果{}不满足允差{}, 继续余料称量", LOG_PREFIX, netWeight, Arrays.toString(diff));
            }
        }
        return updateProcess;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeBatch(WeighExecuteChangeBatchDTO dto) {
        WeighRequirement requirement = weighRequirementMapper.selectById(dto.getRequirementId());
        if (requirement == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NOT_EXIST);
        }
        if (!Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.MAIN)) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NOT_ALLOW_CHANGE_BATCH);
        }
        RequirementWeighProcess process = RequirementWeighProcess.CHANGE_REQUIREMENT;
        if (Objects.equals(requirement.getWeighProcess(), RequirementWeighProcess.ODD)){
            process = RequirementWeighProcess.ODD_CHANGE_REQUIREMENT;
        }
        // 清空需求当前称量批次信息
        weighRequirementMapper.clearBatch(dto.getRequirementId(), process);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finish(WeighExecuteWeighFinishDTO weighFinishDTO) {
        WeighRequirement requirement = weighRequirementMapper.selectById(weighFinishDTO.getRequirementId());
        if (requirement == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NOT_EXIST);
        }

        WeighTask task = weighTaskMapper.selectById(requirement.getWeighRequirementTaskId());
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        List<WeighExecutePendingRequirementSimpleVO> pendingList = this.queryPendingRequirementListByTaskIds(task.getId());

        RequirementWeighProcess weighProcess = requirement.getWeighProcess();
        if (Objects.equals(weighProcess, RequirementWeighProcess.MAIN)) {
            // 当前处于物料称量阶段
            if (CollectionUtil.isEmpty(pendingList)){
                // 是最后一个需求 进入余料称量
                requirement.setWeighProcess(RequirementWeighProcess.ODD);
                requirement.setWeighStatus(RequirementWeighStatusEnum.FINISHED_WEIGH);
                requirement.setRequirementStatus(RequirementStatusEnum.WEIGHED);
            } else {
                // 切换需求
                requirement.setWeighProcess(RequirementWeighProcess.CHANGE_REQUIREMENT);
                requirement.setWeighStatus(RequirementWeighStatusEnum.FINISHED_WEIGH);
                requirement.setRequirementStatus(RequirementStatusEnum.WEIGHED);
            }
        } else if (Objects.equals(weighProcess, RequirementWeighProcess.ODD)) {
            // 当前处于余料称量阶段
            // 未签名列表
            List<WeighExecuteWeighRecord> unSignList = weighExecuteWeighRecordMapper.selectListByRequirementId(requirement.getId())
                    .stream()
                    .filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(unSignList)){
                // 存在未签名的
                requirement.setWeighProcess(RequirementWeighProcess.FINISHED_WEIGH);
                requirement.setWeighStatus(RequirementWeighStatusEnum.FINISHED_WEIGH);
                requirement.setRequirementStatus(RequirementStatusEnum.WEIGHED);
            }else {
                // 全签完了
                requirement.setWeighProcess(RequirementWeighProcess.FINISHED_SIGN);
                requirement.setWeighStatus(RequirementWeighStatusEnum.FINISHED_SIGN);
                requirement.setRequirementStatus(RequirementStatusEnum.WEIGHED);
            }
        } else {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NOT_ALLOW_FINISH);
        }
        weighRequirementMapper.updateById(requirement);
        // 判断是否任务已完成， 刷新任务状态
        this.refreshTaskStatus(Collections.singleton(requirement.getWeighRequirementTaskId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeWeigher(WeighExecuteChangeWeigherDTO dto, boolean validSignStatus) {
        WeighTask task = weighTaskMapper.selectById(dto.getTaskId());
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        if (validSignStatus) {
            // 未签名的记录
            List<WeighExecuteWeighRecord> recordList = weighExecuteWeighRecordMapper.queryListByTaskId(dto.getTaskId())
                    .stream()
                    .filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(recordList)) {
                // 已称量物料件需签名后才能更换
                throw new BmosException(MesResponseCode.WEIGH_RECORD_EXIST_UNSINGED_RECORD);
            }
        }
        task.setPreWeigherId(dto.getWeigherId());
        task.setPreReCheckerId(dto.getReCheckerId());
        task.setRemark(dto.getRemark());
        weighTaskMapper.updateById(task);
        List<WeighRequirement> requirements = weighRequirementMapper.selectListByTaskId(task.getId())
                .stream()
                .filter(WeighRequirement::isProcessing)
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(requirements)) {
            requirements.forEach(item -> {
                item.setWeigherId(dto.getWeigherId());
                item.setReCheckerId(dto.getReCheckerId());
            });
            weighRequirementMapper.updateBatch(requirements);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sign(WeighExecuteWeighSignDTO signDTO) {
        List<WeighExecuteWeighRecord> weighRecords = weighExecuteWeighRecordMapper.queryListByTaskId(signDTO.getTaskId())
                .stream()
                .filter(item -> Objects.equals(item.getSignStatus(), WeighSignStatus.UN_SIGNED))
                .collect(Collectors.toList());
        weighRecords.forEach(weighRecord -> {
            weighRecord.setRemark(signDTO.getRemark());
            weighRecord.setSignStatus(WeighSignStatus.SIGNED);
        });
        if (CollectionUtil.isNotEmpty(weighRecords)) {
            weighExecuteWeighRecordMapper.updateBatch(weighRecords);
        }
        List<Long> storageMaterialIds = weighRecords.stream().map(WeighExecuteWeighRecord::getStorageMaterialId).collect(Collectors.toList());
        storageMaterialService.signBatchByIdList(storageMaterialIds);

        List<Long> requirementIds = weighRecords.stream().map(WeighExecuteWeighRecord::getRequirementId).collect(Collectors.toList());
        // 更新需求签名状态
        List<WeighRequirement> requirements = weighRequirementMapper.selectBatchIds(requirementIds);
        // 更新需求状态
        requirements.forEach(item -> {
            // 只处理称量完成的
            if (item.getWeighProcess() == RequirementWeighProcess.FINISHED_WEIGH || item.getWeighProcess() == RequirementWeighProcess.CHANGE_REQUIREMENT){
                item.setWeighProcess(RequirementWeighProcess.FINISHED_SIGN);
                item.setWeighStatus(RequirementWeighStatusEnum.FINISHED_SIGN);
                item.setRequirementStatus(RequirementStatusEnum.WEIGHED);
            }
        });
        weighRequirementMapper.updateBatch(requirements);

        // 刷新任务状态
        this.refreshTaskStatus(Collections.singleton(signDTO.getTaskId()));
    }

    @Override
    public WeighExecuteWeighRecordListVO queryRecordResultByTaskId(Long taskId) {
        WeighTask task = weighTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        WeighCentreDetailVO weighCentreDetailVO = weighCentreService.queryCentreInfo(task.getWeighCentreId());
        if (weighCentreDetailVO == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NOT_EXIST);
        }
        List<WeighExecuteWeighRecordResult> list = weighExecuteWeighRecordMapper.queryRecordResultByTaskId(taskId);
        WeighExecuteWeighRecordListVO result = new WeighExecuteWeighRecordListVO();
        result.setMainList(list.stream().filter(item -> Objects.equals(item.getWeighType(), WeighType.MAIN)).collect(Collectors.toList()));
        result.setOddList(list.stream().filter(item -> Objects.equals(item.getWeighType(), WeighType.ODD)).collect(Collectors.toList()));
        result.setStation(weighCentreDetailVO.getStationIds());
        BaseUserDO weigher = UserUtils.getUser(task.getPreWeigherId());
        BaseUserDO reChecker = UserUtils.getUser(task.getPreReCheckerId());
        if (weigher != null) {
            result.setWeigherId(task.getPreWeigherId());
            result.setWeigherName(weigher.getUserName());
            result.setWeigherLoginName(weigher.getLoginName());
        }
        if (reChecker != null) {
            result.setReCheckerId(task.getPreReCheckerId());
            result.setReCheckerName(reChecker.getUserName());
            result.setReCheckerLoginName(reChecker.getLoginName());
        }
        return result;
    }

    /**
     * 称量消耗 并返回第一个物料件的批次（批次一定相同）
     *
     * @param consumeStorageMaterialIdList 消耗物料件id列表
     * @param remark                       备注
     * @param taskId                       任务id
     * @param requirementId                需求id
     * @param productPlanId                生产计划id
     * @param unitId                       单位id
     * @param plan                         生产计划
     * @return 批次id
     */
    @Nullable
    private Long weighConsume(List<Long> consumeStorageMaterialIdList,
                              WeighType weighType,
                              String remark,
                              Long taskId,
                              Long requirementId,
                              Long productPlanId,
                              Long unitId,
                              Plan plan
    ) {
        if (CollectionUtil.isEmpty(consumeStorageMaterialIdList)) {
            return null;
        }
        List<StorageMaterial> list = storageMaterialService.queryListByIds(consumeStorageMaterialIdList);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        // 校验物料添加列表 并返回物料件批次id
        Long storageMaterialBatchId = validateMaterialAddList(productPlanId, list);
        // 保存消耗列表
        saveConsumeRecord(taskId, requirementId, productPlanId, unitId, list);
        // 消耗物料
        storageMaterialService.weighConsume(list, remark, plan, weighType == WeighType.MAIN ? StorageOperateTypeEnum.MATERIAL_WEIGH_CONSUME : StorageOperateTypeEnum.MATERIAL_ODD_WEIGH_CONSUME);
        return storageMaterialBatchId;
    }

    /**
     * 保存消耗列表
     *
     * @param taskId        任务id
     * @param requirementId 需求id
     * @param productPlanId 生产计划id
     * @param unitId        单位id
     * @param list          消耗列表
     */
    private void saveConsumeRecord(Long taskId, Long requirementId, Long productPlanId, Long unitId, List<StorageMaterial> list) {
        List<WeighExecuteConsumeRecord> consumeRecordList = list.stream()
                .map(storageMaterial -> {
                    WeighExecuteConsumeRecord weighExecuteConsumeRecord = new WeighExecuteConsumeRecord();
                    weighExecuteConsumeRecord.setTaskId(taskId);
                    weighExecuteConsumeRecord.setRequirementId(requirementId);
                    weighExecuteConsumeRecord.setProductPlanId(productPlanId);
                    weighExecuteConsumeRecord.setConsumeQuantity(PrecisionHelper.precision(unitCache.toExt(storageMaterial.getAvailableQuantity(), unitId), unitId));
                    weighExecuteConsumeRecord.setUnitId(unitId);
                    weighExecuteConsumeRecord.setConsumeTime(LocalDateTime.now());
                    weighExecuteConsumeRecord.setStorageMaterialBatchId(storageMaterial.getStorageMaterialBatchId());
                    weighExecuteConsumeRecord.setStorageMaterialId(storageMaterial.getId());
                    weighExecuteConsumeRecord.setStorageMaterialNo(storageMaterial.getNo());
                    return weighExecuteConsumeRecord;
                }).collect(Collectors.toList());
        // 保存消耗记录
        weighExecuteConsumeRecordMapper.insertBatch(consumeRecordList);
    }

    /**
     * 校验物料添加列表 并返回物料件批次id
     *
     * @param productPlanId
     * @param list
     * @return
     */
    private Long validateMaterialAddList(Long productPlanId, List<StorageMaterial> list) {
        // 判断批次是否唯一
        if (list.stream().map(StorageMaterial::getStorageMaterialBatchId).distinct().count() != 1) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_MATERIAL_BATCH_NOT_DUPLICATE);
        }

        // 判断批次有效性
        Long storageMaterialBatchId = list.get(0).getStorageMaterialBatchId();
        StorageMaterialBatch batch = storageMaterialBatchService.getById(storageMaterialBatchId);
        if (!batch.getAvailable()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_EXPIRED);
        }

        // 判断批次质量状态
        if (!Objects.equals(batch.getQualityStatus(), MaterialQualityStatusEnum.QUALIFIED.getValue()) && !Objects.equals(batch.getQualityStatus(), MaterialQualityStatusEnum.RESTRICTED_RELEASE.getValue())){
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_CANT_USE, batch.getQualityStatus());
        }

        // 校验是否存在不可用物料
        List<StorageMaterial> unavailableList = list.stream()
                .filter(item -> !item.isAvailable())
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(unavailableList)) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_AVAILABLE);
        }

        // 判断有无未出库的物料件
        List<StorageMaterial> unOutList = list.stream()
                .filter(item -> item.getMaterialPositionId() != null)
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(unOutList)) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_OUTBOUND);
        }
        return storageMaterialBatchId;
    }

    /**
     * 处理负数，最小值为0
     *
     * @param value
     * @return
     */
    private BigDecimal checkZero(BigDecimal value) {
        if (value == null) {
            return null;
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return value;
    }

    /**
     * 求和
     *
     * @param list 称量记录
     * @param func 统计字段
     * @param <T>  统计类型
     * @return 总和
     */
    private static <T> BigDecimal getSum(List<T> list, Function<T, BigDecimal> func) {
        return list.stream()
                .map(func)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 生成允差
     *
     * @param value           标准值
     * @param formulaMaterial 配方物料
     * @param process         称量阶段
     * @return 允差信息
     */
    private WeighExecuteDiff buildDiff(BigDecimal value, ProductFormulaMaterial formulaMaterial, RequirementWeighProcess process) {
        WeighExecuteDiff weighExecuteDiff = new WeighExecuteDiff();
        if (Objects.equals(process, RequirementWeighProcess.MAIN) || Objects.equals(process, RequirementWeighProcess.CHANGE_REQUIREMENT)) {
            // 物料称量
            weighExecuteDiff.setToleranceTypeEnum(formulaMaterial.getChargeMixtureToleranceType());
            weighExecuteDiff.setMaxTolerance(formulaMaterial.getChargeMixtureToleranceUpper());
            weighExecuteDiff.setMinTolerance(formulaMaterial.getChargeMixtureToleranceLower());
            weighExecuteDiff.setToleranceDiff(DiffUtil.diff(checkZero(value), weighExecuteDiff.getMaxTolerance(), weighExecuteDiff.getMinTolerance(), weighExecuteDiff.getToleranceTypeEnum(), formulaMaterial.getScale(), formulaMaterial.getScaleLength()));
        } else if (Objects.equals(process, RequirementWeighProcess.ODD) || Objects.equals(process, RequirementWeighProcess.ODD_CHANGE_REQUIREMENT)) {
            // 余料称量
            weighExecuteDiff.setToleranceTypeEnum(formulaMaterial.getOddmentToleranceType());
            weighExecuteDiff.setMaxTolerance(formulaMaterial.getOddmentToleranceUpper());
            weighExecuteDiff.setMinTolerance(formulaMaterial.getOddmentToleranceLower());
            weighExecuteDiff.setToleranceDiff(DiffUtil.diff(checkZero(value), weighExecuteDiff.getMaxTolerance(), weighExecuteDiff.getMinTolerance(), weighExecuteDiff.getToleranceTypeEnum(), formulaMaterial.getScale(), formulaMaterial.getScaleLength()));
        }
        return weighExecuteDiff;
    }

    /**
     * 判断是否配置了允差
     *
     * @param diff 允差范围
     * @return 是否配置了允差 true:配置了允差 false:未配置
     */
    private boolean withDiff(BigDecimal[] diff) {
        return diff[0] != null && diff[2] != null;
    }

    private static class StorageMaterialExtInfo {
        public Long positionId;
        public String positionCode;
        public String positionName;
        public Long containerId;
        public String containerCode;
        public String containerName;

        public StorageMaterialExtInfo(CargoPosition cargoPosition, EquipmentInfoFeignVO device){
            if (cargoPosition != null){
                this.positionId = cargoPosition.getId();
                this.positionCode = cargoPosition.getCode();
                this.positionName = cargoPosition.getPosition();
            }
            if (device != null){
                this.containerId = device.getId();
                this.containerCode = device.getCode();
                this.containerName = device.getName();
            }
        }
    }

    private WeighLogSaveDTO createWeighLogDTO(WeighExecuteWeighAndPrintDTO weighAndPrintDTO,
                                              WeighExecuteWeighRecord weighRecord,
                                              WeighType weighType,
                                              Plan plan,
                                              WeighExecuteRequirementDetailVO detail,
                                              Long containerId
    ) {
        WeighLogSaveDTO saveLogDTO = WeighLogSaveDTO
                .builder()
                .unitId(weighRecord.getUnitId())
                .weigherId(weighRecord.getWeigherId())
                .reCheckerId(weighRecord.getReCheckerId())
                .weighType(weighType)
                .netWeight(weighRecord.getNetWeight())
                .grossWeight(weighRecord.getGrossWeight())
                .tareWeight(weighRecord.getTareWeight())
                .weighTime(LocalDateTime.now())
                .materialName(detail.getMaterialName())
                .materialMergeCode(detail.getMaterialMergeCode())
                .materialNo(weighRecord.getStorageMaterialNo())
                .materialId(weighRecord.getStorageMaterialId())
                .productId(plan.getProductId())
                .materialBatchId(weighRecord.getStorageMaterialBatchId())
                .materialId(detail.getMaterialId())
                .materialType(detail.getCategoryType().getValue())
                .materialBatchNo(detail.getStorageMaterialBatchNo())
                .productPlanId(weighRecord.getProductPlanId())
                .equipmentId(containerId)
                .build();
        // 保存称量设备
        if (!Objects.equals(WeighMode.MANUAL.getValue(), weighAndPrintDTO.getWeighMode()) && weighAndPrintDTO.getDeviceId() != null) {
            EquipmentInfoFeignVO device = FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data), weighAndPrintDTO.getDeviceId()).getData();
            if (device != null) {
                WeighBalanceEquipment weighBalanceEquipment = ScanDeviceConvert.INSTANCE.convertToEquipment(device);
                saveLogDTO.setEquipmentCode(device.getCode());
                saveLogDTO.setEquipmentName(device.getName());
                saveLogDTO.setEquipmentExpireDate(weighBalanceEquipment.getCalibrateExpiredDate());
                saveLogDTO.setEquipmentStatus(weighBalanceEquipment.getIsCalibrated());
            }
        }
        return saveLogDTO;
    }

    private WeighExecuteWeighResult buildReturnResult(StorageMaterial storageMaterial,
                                                      Plan plan,
                                                      RequirementWeighProcess process,
                                                      WeighType weighType,
                                                      List<WeighExecuteWeighRecord> weighExecuteWeighRecords, WeighExecuteRequirementDetailVO detail, BigDecimal netWeight) {
        WeighExecuteWeighResult result = new WeighExecuteWeighResult();
        result.setNo(storageMaterial.getNo());
        result.setProductName(plan.getProductName());
        result.setProductMergeCode(plan.getProductMergeCode());
        result.setBatchNo(plan.getBatchNo());
        result.setTargetQuantity(detail.getTargetTotalQuantity());
        result.setUnWeighedQuantity(checkZero(detail.getUnWeighedQuantity().subtract(netWeight)));
        result.setWeighedQuantity(detail.getWeighedQuantity().add(netWeight));
        result.setUnit(detail.getUnit());
        result.setNextProcess(process);
        result.setResultItemList(weighExecuteWeighRecords.stream()
                .filter(item -> Objects.equals(item.getWeighType(), weighType))
                .map(weighExecuteWeighRecord -> {
                    WeighExecuteWeighResult.WeighResultItem weighResultItem = new WeighExecuteWeighResult.WeighResultItem();
                    weighResultItem.setStorageMaterialId(weighExecuteWeighRecord.getStorageMaterialId());
                    weighResultItem.setStorageMaterialNo(weighExecuteWeighRecord.getStorageMaterialNo());
                    weighResultItem.setStorageMaterialBatchNo(weighExecuteWeighRecord.getStorageMaterialBatchNo());
                    weighResultItem.setTareWeight(weighExecuteWeighRecord.getTareWeight());
                    weighResultItem.setGrossWeight(weighExecuteWeighRecord.getGrossWeight());
                    weighResultItem.setNetWeight(weighExecuteWeighRecord.getNetWeight());
                    weighResultItem.setUnit(unitCache.getGlobalUnitName(weighExecuteWeighRecord.getUnitId()));
                    weighResultItem.setContainerName(weighExecuteWeighRecord.getContainerName());
                    weighResultItem.setMaterialPositionName(weighExecuteWeighRecord.getMaterialPositionName());
                    return weighResultItem;
                }).collect(Collectors.toList()));
        return result;
    }

    /**
     * 批量判断是否任务已完成， 刷新任务状态
     *
     * @param taskIds 任务ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshTaskStatus(Collection<Long> taskIds) {
        List<WeighTask> tasks = weighTaskMapper.selectBatchIds(taskIds);
        if (CollectionUtil.isEmpty(tasks)) {
            return;
        }
        List<WeighTask> sendTaskList = tasks.stream().filter(task -> Objects.equals(task.getTaskStatus(), TaskStatusEnum.SEND))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(sendTaskList)) {
            return;
        }

        Map<Long, WeighTask> taskMap = sendTaskList.stream()
                .collect(Collectors.toMap(WeighTask::getId, Function.identity(), (v1, v2) -> v1));

        List<WeighRequirement> requirements = weighRequirementMapper.selectListByTaskIds(sendTaskList.stream()
                .map(WeighTask::getId)
                .collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(requirements)) {
            return;
        }
        Map<Long, List<WeighRequirement>> collect = requirements.stream()
                .collect(Collectors.groupingBy(WeighRequirement::getWeighRequirementTaskId));

        List<WeighTask> changeList = new ArrayList<>();
        for (Map.Entry<Long, List<WeighRequirement>> entry : collect.entrySet()) {
            List<WeighRequirement> processingList = entry.getValue().stream()
                    .filter(WeighRequirement::isProcessing)
                    .collect(Collectors.toList());
            // 不存在进行中的需求 任务结束
            if (CollectionUtil.isEmpty(processingList)) {
                WeighTask task = taskMap.get(entry.getKey());
                task.setTaskStatus(TaskStatusEnum.EXECUTED);
                task.setFinishTime(LocalDateTime.now());
                changeList.add(task);
            }
        }
        if (CollectionUtil.isNotEmpty(changeList)) {
            weighTaskMapper.updateBatch(changeList);
        }
    }
}
