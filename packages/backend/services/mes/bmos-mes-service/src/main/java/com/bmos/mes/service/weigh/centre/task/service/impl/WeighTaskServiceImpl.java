package com.bmos.mes.service.weigh.centre.task.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.TaskProgramTypeEnum;
import com.bmos.mes.common.enums.weigh.centre.TaskStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.PlatformCodeConstants;
import com.bmos.mes.service.platform.code.dto.BatchConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.BatchNextUseCodeDTO;
import com.bmos.mes.service.platform.code.feign.PlatformCodeFeign;
import com.bmos.mes.service.platform.code.vo.BatchNextCodeVO;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreMapper;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentre;
import com.bmos.mes.service.weigh.centre.execute.dto.WeighExecuteTaskPageQuery;
import com.bmos.mes.service.weigh.centre.execute.vo.WeighExecuteTaskPageVO;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighRequirement;
import com.bmos.mes.service.weigh.centre.requirement.service.IWeighRequirementService;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementProgram;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementVO;
import com.bmos.mes.service.weigh.centre.task.dto.WeighTaskEditDTO;
import com.bmos.mes.service.weigh.centre.task.dto.WeighTaskInfoListQuery;
import com.bmos.mes.service.weigh.centre.task.dto.WeighTaskPageQuery;
import com.bmos.mes.service.weigh.centre.task.mapper.IWeighTaskMapper;
import com.bmos.mes.service.weigh.centre.task.model.WeighTask;
import com.bmos.mes.service.weigh.centre.task.service.IWeighTaskService;
import com.bmos.mes.service.weigh.centre.task.vo.WeighTaskAndRequirementPageVO;
import com.bmos.mes.service.weigh.centre.task.vo.WeighTaskPageVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 称量任务service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/8 15:13
 */
@Service
@Slf4j
public class WeighTaskServiceImpl implements IWeighTaskService {

    private static final String LOG_PREFIX = "[称量任务]";

    @Resource
    private IWeighTaskMapper weighTaskMapper;

    @Resource
    private IWeighRequirementService weighRequirementService;

    @Resource
    private IWeighCentreMapper weighCentreMapper;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private PlatformCodeFeign platformCodeFeign;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private FactoryFeign factoryFeign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void programManual(List<Long> requirementIds) {
        // 手动规划
        log.info("{} 开始手动规划:{}", LOG_PREFIX, requirementIds);
        this.program(requirementIds, TaskProgramTypeEnum.MANUAL,
                WeighRequirementProgram::getMaterialId,
                WeighRequirementProgram::getFormulaUnitId,
                WeighRequirementProgram::getWeighCentreId
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void programAuto() {
        // 查询未来一段时间内所有有数据权限的称量中心下的待规划的需求id
        List<Long> autoProgramRequirementsIds = weighRequirementService.listAutoProgramRequirements();
        if (CollectionUtil.isEmpty(autoProgramRequirementsIds)) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NO_REQUIREMENT);
        }
        // 自动规划
        log.info("{} 开始自动规划:{}", LOG_PREFIX, autoProgramRequirementsIds);
        this.program(autoProgramRequirementsIds, TaskProgramTypeEnum.AUTO,
                WeighRequirementProgram::getMaterialId,
                WeighRequirementProgram::getFormulaUnitId,
                WeighRequirementProgram::getWeighCentreId,
                WeighRequirementProgram::getRequirementDate
        );
    }

    @Override
    public CommonPage<WeighTaskPageVO> queryPage(WeighTaskPageQuery pageQuery) {
        List<Long> deptIds = platformApiAdaptor.deptIds();
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<WeighTaskPageVO> list = weighTaskMapper.queryPage(pageQuery, deptIds);
        return CommonPage.convertPage(list);
    }

    @Override
    public WeighTaskAndRequirementPageVO queryRequirementListByTaskId(WeighTaskInfoListQuery pageQuery) {
        WeighTask task = weighTaskMapper.selectById(pageQuery.getTaskId());
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        Long materialId = task.getMaterialId();
        ProductMaterial material = productMaterialMapper.selectById(materialId);
        WeighTaskAndRequirementPageVO result = new WeighTaskAndRequirementPageVO();
        result.setId(task.getId());
        result.setTaskNo(task.getTaskNo());
        result.setExecuteDate(task.getExecuteDate());
        result.setWeighCentreId(task.getWeighCentreId());
        WeighCentre weighCentre = weighCentreMapper.selectById(task.getWeighCentreId());
        if (weighCentre != null){
            result.setWeighCentreName(weighCentre.getName());
            result.setWeighCentreCode(weighCentre.getCode());
        }
        if (material != null) {
            result.setMaterialName(material.getName());
            result.setMaterialMergeCode(material.getMergeCode());
            result.setMaterialSpecification(material.getSpecification());
        }
        result.setRequirementList(weighRequirementService.queryListByTaskId(pageQuery.getTaskId()));
        return result;
    }

    @Override
    public List<WeighRequirementVO> queryUnPlanedRequirementListByTaskId(Long taskId) {
        WeighTask task = weighTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        Long materialId = task.getMaterialId();
        Long unitId = task.getUnitId();
        Long weighCentreId = task.getWeighCentreId();
        List<Long> deptIds = platformApiAdaptor.deptIds();
        return weighRequirementService.queryUnPlanedRequirementList(materialId, unitId, weighCentreId, deptIds, RequirementStatusEnum.UN_PLANNED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(WeighTaskEditDTO editDTO) {
        WeighTask task = weighTaskMapper.selectById(editDTO.getTaskId());
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        // 判断状态是否可以编辑
        if (!Objects.equals(task.getTaskStatus(), TaskStatusEnum.EDIT)) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_ALLOWED_EDIT);
        }

        // 校验新增列表
        if (CollectionUtil.isNotEmpty(editDTO.getAddIds())) {
            List<WeighRequirementVO> pools = weighRequirementService.queryUnPlanedRequirementList(
                    task.getMaterialId(),
                    task.getUnitId(),
                    task.getWeighCentreId(),
                    platformApiAdaptor.deptIds(),
                    RequirementStatusEnum.UN_PLANNED);
            if (!CollectionUtil.containsAll(pools.stream()
                    .map(WeighRequirementVO::getId)
                    .collect(Collectors.toList()), editDTO.getAddIds())) {
                throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_PROGRAM_EXIST);
            }
            // 关联需求并更新状态
            List<WeighRequirement> requirements = weighRequirementService.selectByIds(editDTO.getAddIds());
            log.info("{}更新称量需求:{}", LOG_PREFIX, task);
            for (WeighRequirement requirement : requirements) {
                requirement.setRequirementStatus(RequirementStatusEnum.UN_WEIGHED);
                requirement.setWeighRequirementTaskId(task.getId());
                requirement.setProgramTime(LocalDateTime.now());
            }
            weighRequirementService.updateBatch(requirements);
        }

        // 校验移除列表
        if (CollectionUtil.isNotEmpty(editDTO.getRemoveIds())) {
            List<WeighRequirement> requirements = weighRequirementService.selectByIds(editDTO.getRemoveIds());
            if (editDTO.getRemoveIds().size() != requirements.size()){
                throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NOT_EXIST);
            }
            for (WeighRequirement requirement : requirements) {
                if (!Objects.equals(requirement.getWeighRequirementTaskId(), task.getId())){
                    throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_NO_PROGRAM);
                }
            }
            // 释放需求
            weighRequirementService.releaseRequirement(requirements);
        }
        List<WeighRequirement> weighRequirements = weighRequirementService.selectListByTaskId(task.getId());
        task.setRequirementQuantity(weighRequirements.stream()
                .map(WeighRequirement::getRequirementQuantity)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
        );
        // 更新执行时间
        task.setExecuteDate(editDTO.getExecuteDate());
        weighTaskMapper.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeSure(Long taskId) {
        WeighTask task = weighTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        // 判断状态是否可以编辑
        if (!Objects.equals(task.getTaskStatus(), TaskStatusEnum.EDIT)) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_ALLOWED_MAKE_SURE);
        }
        task.setTaskStatus(TaskStatusEnum.WAIT_SEND);
        weighTaskMapper.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(Long taskId) {
        WeighTask task = weighTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        // 判断状态是否待下发
        if (!Objects.equals(task.getTaskStatus(), TaskStatusEnum.WAIT_SEND)) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_ALLOWED_SEND);
        }
        task.setTaskStatus(TaskStatusEnum.SEND);
        task.setSendTime(LocalDateTime.now());
        weighTaskMapper.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long taskId) {
        WeighTask task = weighTaskMapper.selectById(taskId);
        if (task == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_EXIST);
        }
        // 判断状态是否为编辑/待下发/已下发状态
        if (!Objects.equals(task.getTaskStatus(), TaskStatusEnum.EDIT) &&
                !Objects.equals(task.getTaskStatus(), TaskStatusEnum.WAIT_SEND) &&
                !Objects.equals(task.getTaskStatus(), TaskStatusEnum.SEND)) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_ALLOWED_CANCEL);
        }
        List<WeighRequirement> requirements = weighRequirementService.selectListByTaskId(task.getId());
        List<WeighRequirement> collect = requirements.stream()
                .filter(item -> Objects.equals(item.getRequirementStatus(), RequirementStatusEnum.WEIGHING)
                        || Objects.equals(item.getRequirementStatus(), RequirementStatusEnum.WEIGHED))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(collect)){
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_TASK_NOT_ALLOWED_CANCEL);
        }

        // 删除任务
        weighTaskMapper.deleteById(task);
        // 释放需求
        weighRequirementService.releaseRequirement(requirements);
    }

    @Override
    public CommonPage<WeighExecuteTaskPageVO> queryExecuteTaskPage(WeighExecuteTaskPageQuery pageQuery) {
        List<Long> stationIds = FeignUtils.handleRequest((userId) ->
                        factoryFeign.getStationIdsByUserId(userId), SysUserHolder.getUser().getUserId())
                .getData()
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollectionUtil.isEmpty(stationIds) || CollectionUtil.isEmpty(deptIds)){
            return CommonPage.convertPage(new ArrayList<>());
        }
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<WeighExecuteTaskPageVO> list = weighTaskMapper.queryExecuteTaskPageWithDeptAndStation(pageQuery, deptIds, stationIds, TaskStatusEnum.SEND.getValue());
        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<WeighExecuteTaskPageVO> queryHistoryTaskPage(WeighExecuteTaskPageQuery pageQuery) {
        List<Long> stationIds = FeignUtils.handleRequest((userId) ->
                        factoryFeign.getStationIdsByUserId(userId), SysUserHolder.getUser().getUserId())
                .getData()
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollectionUtil.isEmpty(stationIds) || CollectionUtil.isEmpty(deptIds)){
            return CommonPage.convertPage(new ArrayList<>());
        }
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<WeighExecuteTaskPageVO> list = weighTaskMapper.queryExecuteTaskPageWithDeptAndStation(pageQuery, deptIds, stationIds, TaskStatusEnum.EXECUTED.getValue());
        return CommonPage.convertPage(list);
    }

    /**
     * 分组规划
     *
     * @param list      待分组的数据
     * @param groupKeys 分组字段
     * @return key: 分组字段(_连接), value: 单组数据
     */
    @SafeVarargs
    private final Map<String, List<WeighRequirementProgram>> group(List<WeighRequirementProgram> list, Function<WeighRequirementProgram, Object>... groupKeys) {
        return list.stream()
                .collect(Collectors.groupingBy(test -> Stream.of(groupKeys)
                        .map(function -> function.apply(test))
                        .map(Object::toString)
                        .collect(Collectors.joining("_"))));
    }

    /**
     * 规划
     *
     * @param requirementIds 需求id
     * @param programType    规划类型
     * @param groupKeys      分组
     */
    @SafeVarargs
    public final void program(List<Long> requirementIds, TaskProgramTypeEnum programType, Function<WeighRequirementProgram, Object>... groupKeys) {
        List<WeighRequirementProgram> list = weighRequirementService.selectRequirementProgramListByIds(requirementIds);
        // 判断有无非待规划状态的数据 有的话提示不允许重复规划
        List<WeighRequirementProgram> planed = list.stream()
                .filter(item -> !Objects.equals(item.getRequirementStatus(), RequirementStatusEnum.UN_PLANNED))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(planed)) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_REQUIREMENT_PROGRAM_EXIST);
        }
        Map<String, List<WeighRequirementProgram>> group = group(list, groupKeys);
        log.info("{} {}规划结果:{}", LOG_PREFIX, programType.getName(), JsonUtils.toJsonPrettyString(group));
        if (MapUtil.isEmpty(group)) {
            return;
        }
        List<String> serials = createSerials(group.size());
        Map<Long, Integer> indexMap = new HashMap<>();
        // 创建任务列表
        List<WeighTask> tasks = convertToTask(programType, group, indexMap, serials);
        log.info("{}创建称量任务:{}", LOG_PREFIX, tasks);
        // 保存任务
        weighTaskMapper.insertBatch(tasks);

        // 更新需求状态
        List<WeighRequirement> requirements = weighRequirementService.selectByIds(requirementIds);
        log.info("{}更新称量需求:{}", LOG_PREFIX, tasks);
        for (WeighRequirement requirement : requirements) {
            Integer i = indexMap.get(requirement.getId());
            WeighTask task = tasks.get(i);
            requirement.setRequirementStatus(RequirementStatusEnum.UN_WEIGHED);
            requirement.setWeighRequirementTaskId(task.getId());
            requirement.setProgramTime(LocalDateTime.now());
        }
        weighRequirementService.updateBatch(requirements);
        // 确认编号
        confirmSerials(serials);
    }

    /**
     * 创建任务
     *
     * @param programType 规划类型
     * @param group       分组数据 分组字段 -> 单组数据
     * @param indexMap    需求id -> 单组数据索引
     * @param serials     流水号
     * @return 任务列表
     */
    private static List<WeighTask> convertToTask(TaskProgramTypeEnum programType,
                                                 Map<String, List<WeighRequirementProgram>> group,
                                                 Map<Long, Integer> indexMap,
                                                 List<String> serials
    ) {
        List<WeighTask> tasks = new ArrayList<>();
        int n = 0;
        for (Map.Entry<String, List<WeighRequirementProgram>> entry : group.entrySet()) {
            List<WeighRequirementProgram> requirementPrograms = entry.getValue();
            for (WeighRequirementProgram requirementProgram : requirementPrograms) {
                indexMap.put(requirementProgram.getId(), n);
            }
            BigDecimal sum = requirementPrograms.stream()
                    .map(WeighRequirementProgram::getRequirementQuantity)
                    .map(BigDecimal::new)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 任务执行时间默认为该任务中最早的需求时间
            LocalDate earliest = requirementPrograms.stream()
                    .map(WeighRequirementProgram::getRequirementDate)
                    .map(LocalDate::parse)
                    .min(LocalDate::compareTo)
                    .orElse(null);
            WeighRequirementProgram first = requirementPrograms.get(0);
            WeighTask task = new WeighTask();
            task.setTaskNo(serials.get(n++));
            task.setMaterialId(first.getMaterialId());
            task.setUnitId(first.getFormulaUnitId());
            task.setWeighCentreId(first.getWeighCentreId());
            task.setRequirementQuantity(sum);
            task.setExecuteDate(earliest);
            task.setTaskStatus(TaskStatusEnum.EDIT);
            task.setTaskProgramType(programType);
            task.setProcessTime(LocalDateTime.now());
            task.setProcessOperatorId(SysUserHolder.getUser().getUserId());
            tasks.add(task);
        }
        return tasks;
    }

    /**
     * 申请编号
     *
     * @param size
     * @return
     */
    private List<String> createSerials(int size) {
        if (size <= 0) {
            return new ArrayList<>();
        }
        return FeignUtils.handleRequest(data -> platformCodeFeign.getBatchNextUseNo(data), BatchNextUseCodeDTO.builder()
                        .code(PlatformCodeConstants.WEIGH_TASK_SERIAL)
                        .fields(new HashMap<>())
                        .num(size)
                        .build())
                .getData().getNos()
                .stream().map(BatchNextCodeVO.NextCodeVO::getNo).collect(Collectors.toList());
    }

    /**
     * 确认编号
     *
     * @param serials
     */
    private void confirmSerials(List<String> serials) {
        if (CollectionUtil.isEmpty(serials)) {
            return;
        }
        FeignUtils.handleRequest(data -> platformCodeFeign.batchConfirmNo(data), BatchConfirmNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.WEIGH_TASK_SERIAL)
                .fullNos(serials)
                .fields(new HashMap<>())
                .build());
    }
}
