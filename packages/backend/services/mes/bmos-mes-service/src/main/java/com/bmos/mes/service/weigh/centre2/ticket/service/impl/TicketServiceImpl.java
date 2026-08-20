package com.bmos.mes.service.weigh.centre2.ticket.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.TaskProgramTypeEnum;
import com.bmos.mes.common.enums.weigh.centre2.TicketStatusEnum;
import com.bmos.mes.common.enums.weigh.centre2.TicketWeighStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.PlatformCodeConstants;
import com.bmos.mes.service.platform.code.dto.BatchConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.BatchNextUseCodeDTO;
import com.bmos.mes.service.platform.code.feign.PlatformCodeFeign;
import com.bmos.mes.service.platform.code.vo.BatchNextCodeVO;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreMapper;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentre;
import com.bmos.mes.service.weigh.centre2.execute.mapper.WeighRequirementRecordMapper;
import com.bmos.mes.service.weigh.centre2.execute.model.WeighRequirementRecordDO;
import com.bmos.mes.service.weigh.centre2.requirement.dto.RequirementQueryDTO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementDO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementGroupDO;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementGroupMapper;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementMapper;
import com.bmos.mes.service.weigh.centre2.requirement.service.ITicketRequirementService;
import com.bmos.mes.service.weigh.centre2.requirement.vo.WeighRequirementListVO;
import com.bmos.mes.service.weigh.centre2.ticket.dto.TicketEditDTO;
import com.bmos.mes.service.weigh.centre2.ticket.dto.TicketPageQuery;
import com.bmos.mes.service.weigh.centre2.ticket.entity.TicketDO;
import com.bmos.mes.service.weigh.centre2.ticket.mapper.ITicketMapper;
import com.bmos.mes.service.weigh.centre2.ticket.service.ITicketService;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketPageVO;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketWeighMaterialRecordVO;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketWeighRecordVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 工单Service实现类
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:30
 */
@Service
@Slf4j
public class TicketServiceImpl implements ITicketService {

    private static final String LOG_PREFIX = "[工单]";

    @Resource
    private ITicketMapper ticketMapper;

    @Resource
    private PlatformCodeFeign platformCodeFeign;

    @Resource
    private ITicketRequirementMapper requirementMapper;

    @Resource
    private ITicketRequirementGroupMapper requirementGroupMapper;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private WeighRequirementRecordMapper weighRequirementRecordMapper;

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ITicketRequirementService requirementService;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private IWeighCentreMapper weighCentreMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void programAuto() {
        // 查询待规划的需求id

        List<Long> autoProgramRequirementsIds = CollectionUtils.convertList(requirementService.list(new RequirementQueryDTO()), WeighRequirementListVO::getId);
        if (CollectionUtil.isEmpty(autoProgramRequirementsIds)) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NO_REQUIREMENT);
        }
        // 自动规划
        log.info("{} 开始自动规划:{}", LOG_PREFIX, autoProgramRequirementsIds);
        this.program(autoProgramRequirementsIds, TaskProgramTypeEnum.AUTO,
                TicketRequirementDO::getMaterialId,
                TicketRequirementDO::getStorageMaterialBatchId,
                TicketRequirementDO::getUnitId,
                TicketRequirementDO::getWeighCentreId,
                TicketRequirementDO::getPlanDate
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void programManual(List<Long> requirementIds) {
// 手动规划
        log.info("{} 开始手动规划:{}", LOG_PREFIX, requirementIds);
        this.program(requirementIds, TaskProgramTypeEnum.MANUAL,
                TicketRequirementDO::getMaterialId,
                TicketRequirementDO::getStorageMaterialBatchId,
                TicketRequirementDO::getUnitId,
                TicketRequirementDO::getWeighCentreId
        );
    }

    @Override
    public CommonPage<TicketPageVO> page(TicketPageQuery pageDTO) {
        log.info("{} 分页查询称量工单需求组, 参数:{}", LOG_PREFIX, pageDTO);

        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollectionUtils.isAnyEmpty(deptIds)){
            return CommonPage.convertPage(new ArrayList<>());
        }
        List<WeighCentre> weighCentres = weighCentreMapper.listAllByDeptIds(deptIds);
        if (CollectionUtils.isAnyEmpty(weighCentres)){
            return CommonPage.convertPage(new ArrayList<>());
        }

        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), pageDTO.getOrderSql());
        List<TicketPageVO> list = ticketMapper.queryPage(pageDTO, CollectionUtils.convertList(weighCentres, WeighCentre::getId));
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
    private final Map<String, List<TicketRequirementDO>> group(List<TicketRequirementDO> list, Function<TicketRequirementDO, Object>... groupKeys) {
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
    public final void program(List<Long> requirementIds, TaskProgramTypeEnum programType, Function<TicketRequirementDO, Object>... groupKeys) {
        List<TicketRequirementDO> list = requirementMapper.selectBatchIds(requirementIds);
        // 判断有无非待规划状态的数据 有的话提示不允许重复规划
        List<TicketRequirementDO> planed = list.stream()
                .filter(item -> !Objects.equals(item.getRequirementStatus(), RequirementStatusEnum.UN_PLANNED))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(planed)) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_REQUIREMENT_PROGRAM_EXIST);
        }
        Map<String, List<TicketRequirementDO>> group = group(list, groupKeys);
        log.info("{} {}规划结果:{}", LOG_PREFIX, programType.getName(), JsonUtils.toJsonPrettyString(group));
        if (MapUtil.isEmpty(group)) {
            return;
        }
        List<String> serials = createSerials(group.size());
        Map<Long, Integer> indexMap = new HashMap<>();
        // 创建任务列表
        List<TicketDO> tickets = convertToTicket(programType, group, indexMap, serials);
        log.info("{}创建称量工单:{}", LOG_PREFIX, tickets);
        // 保存任务
        ticketMapper.insertBatch(tickets);

        // 更新需求状态
        List<TicketRequirementDO> requirements = requirementMapper.selectBatchIds(requirementIds);
        log.info("{}更新称量需求:{}", LOG_PREFIX, tickets);
        for (TicketRequirementDO requirement : requirements) {
            Integer i = indexMap.get(requirement.getId());
            TicketDO ticket = tickets.get(i);
            requirement.setRequirementStatus(RequirementStatusEnum.UN_WEIGHED);
            requirement.setTicketId(ticket.getId());
            requirement.setProgramTime(LocalDateTime.now());
        }
        requirementMapper.updateBatch(requirements, 500);
        // 确认编号
        confirmSerials(serials);
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
                        .code(PlatformCodeConstants.WEIGH_TICKET_SERIAL)
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
                .code(PlatformCodeConstants.WEIGH_TICKET_SERIAL)
                .fullNos(serials)
                .fields(new HashMap<>())
                .build());
    }

    /**
     * 创建工单
     *
     * @param programType 规划类型
     * @param group       分组数据 分组字段 -> 单组数据
     * @param indexMap    需求id -> 单组数据索引
     * @param serials     流水号
     * @return 任务列表
     */
    private List<TicketDO> convertToTicket(TaskProgramTypeEnum programType,
                                           Map<String, List<TicketRequirementDO>> group,
                                           Map<Long, Integer> indexMap,
                                           List<String> serials
    ) {
        Set<Long> materialIds = group.values().stream()
                .flatMap(Collection::stream)
                .map(TicketRequirementDO::getMaterialId)
                .collect(Collectors.toSet());

        Map<Long, ProductMaterial> materialMap = productMaterialMapper.selectBatchIds(materialIds)
                .stream()
                .collect(Collectors.toMap(ProductMaterial::getId, material -> material, (k1, k2) -> k1));

        List<TicketDO> tickets = new ArrayList<>();
        int n = 0;
        for (Map.Entry<String, List<TicketRequirementDO>> entry : group.entrySet()) {
            List<TicketRequirementDO> requirements = entry.getValue();
            for (TicketRequirementDO requirement : requirements) {
                indexMap.put(requirement.getId(), n);
            }
            BigDecimal sum = requirements.stream()
                    .map(TicketRequirementDO::getFormulaQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            // 任务执行时间默认为该任务中最早的需求时间
            LocalDate earliest = requirements.stream()
                    .map(TicketRequirementDO::getPlanDate)
                    .min(LocalDate::compareTo)
                    .orElse(null);
            TicketRequirementDO first = requirements.get(0);
            TicketDO ticket = new TicketDO();
            ticket.setTicketNo(serials.get(n++));
            ticket.setMaterialId(first.getMaterialId());
            ProductMaterial material = materialMap.get(first.getMaterialId());
            ticket.setMaterialName(material.getName());
            ticket.setMaterialMergeCode(material.getMergeCode());
            ticket.setMaterialSpecification(material.getSpecification());
            ticket.setStorageMaterialBatchId(first.getStorageMaterialBatchId());
            ticket.setWeighCentreId(first.getWeighCentreId());
            ticket.setRequirementQuantity(sum);
            ticket.setUnitId(first.getUnitId());
            ticket.setPlanDate(earliest);
            ticket.setStatus(TicketStatusEnum.EDIT);
            ticket.setTaskProgramType(programType);
            tickets.add(ticket);
        }
        return tickets;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issue(@NotNull Long id) {
        log.info("{} 下发工单, id:{}", LOG_PREFIX, id);
        TicketDO ticket = ticketMapper.selectById(id);
        if (ticket == null) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }

        // 检查工单状态是否为"编辑中"
        if (!TicketStatusEnum.EDIT.equals(ticket.getStatus())) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_STATUS_ERROR);
        }

        // 更新工单状态为"已下发"，记录下发时间
        ticket.setStatus(TicketStatusEnum.SEND);
        ticket.setSendTime(LocalDateTime.now());
        ticket.setTicketWeighStatus(TicketWeighStatusEnum.UN_WEIGHED);

        ticketMapper.updateById(ticket);
        log.info("{} 工单下发成功, id:{}", LOG_PREFIX, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(@NotNull Long id) {
        log.info("{} 取消工单, id:{}", LOG_PREFIX, id);
        TicketDO ticket = ticketMapper.selectById(id);
        if (ticket == null) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }

        // 检查工单状态是否为"编辑中"
        if (!TicketStatusEnum.EDIT.equals(ticket.getStatus())) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_STATUS_ERROR);
        }

        // 更新工单状态为"已取消"
        ticket.setStatus(TicketStatusEnum.CANCELED);
        ticketMapper.updateById(ticket);
        // 查询该工单下的所有需求
        requirementMapper.update(null, new LambdaUpdateWrapper<TicketRequirementDO>()
                .eq(TicketRequirementDO::getTicketId, id)
                .set(TicketRequirementDO::getRequirementStatus, RequirementStatusEnum.UN_PLANNED)
                .set(TicketRequirementDO::getTicketId, null)
                .set(TicketRequirementDO::getProgramTime, null)
        );
        log.info("{} 工单取消成功, id:{}", LOG_PREFIX, id);
    }

    @Override
    public TicketDO getTicketInfo(Long ticketId) {
        return ticketMapper.selectById(ticketId);
    }

    @Override
    public List<TicketWeighRecordVO> getWeighRecord(Long ticketId) {
        // 需求
        List<TicketRequirementDO> requirements = requirementMapper.getRequirementsByTicketId(ticketId);
        return this.getTicketWeighRecords(requirements);
    }

    @Override
    public List<TicketWeighRecordVO> getTicketWeighRecords(List<TicketRequirementDO> requirements) {

        if(CollectionUtil.isEmpty(requirements)){
            return new ArrayList<>();
        }

        // 过滤未规划和已失效的数据
        requirements = requirements.stream()
                .filter(item -> item.getRequirementStatus() != RequirementStatusEnum.UN_PLANNED && item.getRequirementStatus() != RequirementStatusEnum.EXPIRED)
                .collect(Collectors.toList());

        if(CollectionUtil.isEmpty(requirements)){
            return new ArrayList<>();
        }

        // 称量记录
        List<WeighRequirementRecordDO> records = weighRequirementRecordMapper.selectByRequirementIds(CollectionUtils.convertList(requirements, TicketRequirementDO::getId));

        // 需求组
        List<TicketRequirementGroupDO> groups = requirementGroupMapper.selectBatchIds(CollectionUtils.convertList(requirements, TicketRequirementDO::getRequirementGroupId));
        Map<Long, TicketRequirementGroupDO> groupMap = CollectionUtils.convertMap(groups, TicketRequirementGroupDO::getId);

        // 产品信息
        List<ProductMaterial> products = productMaterialMapper.selectBatchIds(CollectionUtils.convertList(groups, TicketRequirementGroupDO::getMaterialId));
        Map<Long, ProductMaterial> productMap = CollectionUtils.convertMap(products, ProductMaterial::getId);

        // 物料信息
        List<ProductMaterial> materials = productMaterialMapper.selectBatchIds(CollectionUtils.convertList(requirements, TicketRequirementDO::getMaterialId));
        Map<Long, ProductMaterial> materialsMap = CollectionUtils.convertMap(materials, ProductMaterial::getId);

        // 批次信息
        List<StorageMaterialBatch> storageMaterialBatches = storageMaterialBatchMapper.selectBatchIds(CollectionUtils.convertList(requirements, TicketRequirementDO::getStorageMaterialBatchId));
        Map<Long, StorageMaterialBatch> storageMaterialBatchMap = CollectionUtils.convertMap(storageMaterialBatches, StorageMaterialBatch::getId);

        List<TicketWeighRecordVO> results = new ArrayList<>();
        for (TicketRequirementDO requirement : requirements) {
            TicketWeighRecordVO record = new TicketWeighRecordVO();
            record.setRequirementId(requirement.getId());
            TicketRequirementGroupDO group = groupMap.get(requirement.getRequirementGroupId());
            record.setBatchNo(group.getBatchNo());
            ProductMaterial product = productMap.get(group.getMaterialId());
            record.setProductName(product.getName());
            record.setProductMergeCode(product.getMergeCode());
            record.setRequirementQuantity(requirement.getTheoreticalQuantity());
            record.setRequirementUsage(requirement.getRequirementUsage());
            record.setUnitId(requirement.getUnitId());
            record.setUnit(unitCache.getGlobalUnitName(requirement.getUnitId()));
            record.setStatus(requirement.getRequirementStatus());

            ProductMaterial material = materialsMap.get(requirement.getMaterialId());
            record.setMaterialName(material.getName());
            record.setMaterialSpecification(material.getSpecification());
            record.setMergeCode(material.getMergeCode());

            StorageMaterialBatch storageMaterialBatch = storageMaterialBatchMap.get(requirement.getStorageMaterialBatchId());
            record.setMaterialBatchNo(storageMaterialBatch.getMaterialBatchNo());

            List<WeighRequirementRecordDO> weighRecords = records.stream()
                    .filter(item -> item.getWeighTicketRequirementId().equals(requirement.getId()))
                    .collect(Collectors.toList());
            List<TicketWeighMaterialRecordVO> recordList = new ArrayList<>();
            for (WeighRequirementRecordDO weighRecord : weighRecords) {
                TicketWeighMaterialRecordVO vo = new TicketWeighMaterialRecordVO();
                vo.setStorageMaterialId(weighRecord.getStorageMaterialId());
                vo.setStorageMaterialNo(weighRecord.getStorageMaterialNo());
                vo.setTareWeight(weighRecord.getTareWeight());
                vo.setGrossWeight(weighRecord.getGrossWeight());
                vo.setNetWeight(weighRecord.getNetWeight());
                vo.setUnitId(weighRecord.getUnitId());
                vo.setUnit(unitCache.getGlobalUnitName(weighRecord.getUnitId()));
                vo.setWeigherId(weighRecord.getWeighUserId());
                vo.setWeigherName(UserUtils.getUsername(weighRecord.getWeighUserId()));
                vo.setRecheckerId(weighRecord.getSignUser());
                vo.setRecheckerName(UserUtils.getUsername(weighRecord.getSignUser()));
                vo.setWeighTime(weighRecord.getWeighTime());
                recordList.add(vo);
            }
            record.setList(recordList);
            results.add(record);
        }
        return results;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(@NotNull TicketEditDTO editDTO) {
        log.info("{} 编辑工单, 参数:{}", LOG_PREFIX, editDTO);
        Long id = editDTO.getId();
        TicketDO ticket = ticketMapper.selectById(id);
        if (ticket == null) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }

        // 检查工单状态是否为"编辑中"
        if (!TicketStatusEnum.EDIT.equals(ticket.getStatus())) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_STATUS_ERROR);
        }

        // 修改计划执行时间
        if (editDTO.getPlanDate() != null) {
            ticket.setPlanDate(editDTO.getPlanDate());
            ticketMapper.updateById(ticket);
        }

        // 删除需求(释放)
        List<Long> deleteRequirementIds = editDTO.getDeleteRequirementIds();
        if (CollectionUtil.isNotEmpty(deleteRequirementIds)) {
            // 查询需要删除的需求
            List<TicketRequirementDO> deleteRequirements = requirementMapper.selectBatchIds(deleteRequirementIds);
            if (CollectionUtil.isNotEmpty(deleteRequirements)) {
                for (TicketRequirementDO requirement : deleteRequirements) {
                    // 检查需求是否属于当前工单
                    if (!Objects.equals(requirement.getTicketId(), id)) {
                        throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_BELONG_TO_TICKET);
                    }
                }
                // 更新该工单下的所有需求
                requirementMapper.update(null, new LambdaUpdateWrapper<TicketRequirementDO>()
                        .in(TicketRequirementDO::getId, deleteRequirementIds)
                        .set(TicketRequirementDO::getRequirementStatus, RequirementStatusEnum.UN_PLANNED)
                        .set(TicketRequirementDO::getTicketId, null)
                        .set(TicketRequirementDO::getProgramTime, null)
                );
            }
        }

        // 添加新的需求
        List<Long> addRequirementIds = editDTO.getAddRequirementIds();
        if (CollectionUtil.isNotEmpty(addRequirementIds)) {
            // 查询需要添加的需求
            List<TicketRequirementDO> addRequirements = requirementMapper.selectBatchIds(addRequirementIds);
            if (CollectionUtil.isNotEmpty(addRequirements)) {
                // 检查所有需求是否都是未规划状态
                for (TicketRequirementDO requirement : addRequirements) {
                    if (!RequirementStatusEnum.UN_PLANNED.equals(requirement.getRequirementStatus())) {
                        throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_ALREADY_PLANNED);
                    }

                    // 检查物料ID是否与工单物料匹配
                    if (!Objects.equals(requirement.getMaterialId(), ticket.getMaterialId())) {
                        throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_MATERIAL_NOT_MATCH);
                    }
                    // 更新需求状态为未称量，并设置工单ID和规划时间
                    requirement.setRequirementStatus(RequirementStatusEnum.UN_WEIGHED);
                    requirement.setTicketId(id);
                    requirement.setProgramTime(LocalDateTime.now());
                }

                // 批量更新需求状态
                requirementMapper.updateBatch(addRequirements, 500);
                // 更新工单所需总量
                updateTicketRequirementQuantity(id);
            }
        }

        // 判断工单下是否存在需求
        List<TicketRequirementDO> requirements = requirementMapper.getRequirementsByTicketId(ticket.getId());
        if (CollectionUtil.isEmpty(requirements)){
            throw new BmosException(MesResponseCode.WEIGHT_TICKET_NO_REQUIREMENT);
        }
        BigDecimal sum = requirements.stream().map(TicketRequirementDO::getFormulaQuantity).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        ticket.setRequirementQuantity(sum);
        ticketMapper.updateById(ticket);

        log.info("{} 工单编辑成功, id:{}", LOG_PREFIX, id);
    }

    /**
     * 更新工单所需总量
     *
     * @param ticketId 工单ID
     */
    private void updateTicketRequirementQuantity(Long ticketId) {
        // 查询工单下的所有需求
        List<TicketRequirementDO> requirements = requirementMapper.getRequirementsByTicketId(ticketId);
        if (CollectionUtil.isNotEmpty(requirements)) {
            // 计算需求总量
            BigDecimal sum = requirements.stream()
                    .map(TicketRequirementDO::getFormulaQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 更新工单所需总量
            TicketDO ticket = ticketMapper.selectById(ticketId);
            ticket.setRequirementQuantity(sum);
            ticketMapper.updateById(ticket);
        }
    }
} 