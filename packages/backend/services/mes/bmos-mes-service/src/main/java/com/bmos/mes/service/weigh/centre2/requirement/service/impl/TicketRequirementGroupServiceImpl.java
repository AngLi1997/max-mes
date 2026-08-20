package com.bmos.mes.service.weigh.centre2.requirement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.TicketRequirementReleaseStatus;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.formula.mapper.ProductFormulaMapper;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.mapper.ProductFormulaVersionMapper;
import com.bmos.mes.service.formula.model.ProductFormula;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.model.ProductFormulaVersion;
import com.bmos.mes.service.formula.model.ProductFormulaWeighRequirementInfo;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreMapper;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentre;
import com.bmos.mes.service.weigh.centre2.execute.mapper.WeighRequirementRecordMapper;
import com.bmos.mes.service.weigh.centre2.requirement.dto.*;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementDO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementGroupDO;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementGroupMapper;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementMapper;
import com.bmos.mes.service.weigh.centre2.requirement.service.ITicketRequirementGroupService;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementGroupInfoVO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementGroupPageVO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementOccupancyQuantityResult;
import com.bmos.mes.service.weigh.centre2.ticket.service.ITicketService;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketWeighRecordVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工单需求Service实现类
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 20:05
 */
@Service
@Slf4j
public class TicketRequirementGroupServiceImpl implements ITicketRequirementGroupService {

    private static final String LOG_PREFIX = "[工单需求]";

    @Resource
    private ITicketRequirementGroupMapper ticketRequirementGroupMapper;

    @Resource
    private ITicketRequirementMapper requirementMapper;

    @Resource
    private WeighRequirementRecordMapper weighRequirementRecordMapper;

    @Resource
    private ProductFormulaMapper productFormulaMapper;

    @Resource
    private ProductFormulaMaterialMapper productFormulaMaterialMapper;

    @Resource
    private ProductFormulaVersionMapper productFormulaVersionMapper;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Resource
    private IStorageMaterialMapper storageMaterialMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ITicketService ticketService;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private IWeighCentreMapper weighCentreMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRequirementGroup(TicketRequirementGroupDTO createDTO) {
        log.info("{} 创建称量工单需求, 参数:{}", LOG_PREFIX, createDTO);
        // 创建工单需求
        TicketRequirementGroupDO group = new TicketRequirementGroupDO();
        group.setMaterialId(createDTO.getProductId());
        group.setBomVersionId(createDTO.getBomVersionId());
        group.setBatchNo(createDTO.getBatchNo());
        group.setWeighCentreId(createDTO.getCentreWeighId());
        group.setPlanDate(createDTO.getPlanDate());
        group.setRemark(createDTO.getRemark());
        group.setReleaseStatus(TicketRequirementReleaseStatus.EDIT);
        // 保存工单需求
        ticketRequirementGroupMapper.insert(group);
        Long groupId = group.getId();
//        // 批量保存物料批次信息
//        List<TicketRequirementDO> materialList = new ArrayList<>();
//        for (TicketRequirementGroupDTO.FormulaMaterialBatchDTO formulaMaterialBatchDTOS : createDTO.getFormulaMaterialBatchDTOS()) {
//            for (TicketRequirementGroupDTO.MaterialBatchDTO batchDTO : formulaMaterialBatchDTOS.getBatches()) {
//                TicketRequirementDO requirement = new TicketRequirementDO();
//                requirement.setRequirementGroupId(groupId);
//                requirement.setFormulaMaterialId(formulaMaterialBatchDTOS.getFormulaMaterialId());
//                requirement.setRequirementKey(formulaMaterialBatchDTOS.getKey());
//                requirement.setMaterialId(batchDTO.getMaterialId());
//                requirement.setPlanDate(group.getPlanDate());
//                requirement.setWeighCentreId(group.getWeighCentreId());
//                requirement.setStorageMaterialBatchId(batchDTO.getStorageMaterialBatchId());
//                requirement.setFormulaQuantity(batchDTO.getFormulaQuantity());
//                requirement.setTheoreticalQuantity(batchDTO.getTheoreticalQuantity());
//                requirement.setUnitId(batchDTO.getUnitId());
//                // 默认未规划状态
//                requirement.setRequirementStatus(RequirementStatusEnum.UN_PLANNED);
//                requirement.setWeighStatus(RequirementWeighStatusEnum.PENDING);
//                // 其他字段根据业务需要设置默认值
//                materialList.add(requirement);
//            }
//        }
//        // 批量保存物料批次
//        if (!materialList.isEmpty()) {
//            ticketRequirementMapper.insertBatch(materialList);
//        }
        log.info("{} 创建称量工单需求组成功, requirementId:{}", LOG_PREFIX, groupId);
        return groupId;
    }

    @Override
    public TicketRequirementGroupInfoVO queryInfo(TicketRequirementInfoQuery query) {

        log.info("{} 查询工单需求信息, 参数:{}", LOG_PREFIX, query);

        // 参数校验
        if (query.getBomVersionId() == null && query.getId() == null) {
            throw new ValidationException("bomVersionId和id不能同时为空");
        }

        TicketRequirementGroupInfoVO resultVO = new TicketRequirementGroupInfoVO();
        TicketRequirementGroupDO group;
        ProductFormulaVersion formulaVersion;

        // 根据需求ID查询详情
        if (query.getId() != null) {
            group = ticketRequirementGroupMapper.selectById(query.getId());
            if (group == null) {
                throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_GROUP_NOT_EXIST);
            }
            // 设置查询参数中的bomVersionId
            query.setBomVersionId(group.getBomVersionId());
            // 设置VO的基本信息
            resultVO.setProductId(group.getMaterialId());
            resultVO.setBomVersionId(group.getBomVersionId());
            resultVO.setBatchNo(group.getBatchNo());
            resultVO.setCentreWeighId(group.getWeighCentreId());
            resultVO.setPlanDate(group.getPlanDate());
            resultVO.setRemark(group.getRemark());
        }

        // 查询配方版本信息
        formulaVersion = productFormulaVersionMapper.selectById(query.getBomVersionId());
        if (formulaVersion == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_VERSION_NOT_EXISTED);
        }

        // 查询配方信息
        ProductFormula formula = productFormulaMapper.selectById(formulaVersion.getProductFormulaId());
        if (formula == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_VERSION_NOT_EXISTED);
        }

        // 如果是通过bomVersionId查询，需要设置产品信息
        if (query.getId() == null) {
            ProductMaterial product = productMaterialMapper.selectById(formula.getProductId());
            if (product == null) {
                throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
            }

            resultVO.setProductId(formula.getProductId());
            resultVO.setBomVersionId(query.getBomVersionId());
            // 其他字段保持为null
        }

        // 查询配方物料列表
        List<ProductFormulaMaterial> formulaMaterials = productFormulaMaterialMapper.selectByVersionId(query.getBomVersionId());
        if (CollectionUtils.isAnyEmpty(formulaMaterials)) {
            log.warn("{} 配方版本下没有物料信息, versionId:{}", LOG_PREFIX, query.getBomVersionId());
            resultVO.setFormulas(new ArrayList<>());
            return resultVO;
        }

        // 构建配方物料映射，用于后续设置物料批次信息
        Map<String, TicketRequirementGroupInfoVO.FormulaMaterialBatchInfo> formulaMaterialMap = new HashMap<>();

        // 构建配方物料列表
        List<TicketRequirementGroupInfoVO.FormulaMaterialBatchInfo> formulaBatchInfoList = new ArrayList<>();
        for (ProductFormulaMaterial formulaMaterial : formulaMaterials) {
            int index = 0;
            for (Object obj : formulaMaterial.getWeighRequirementList()) {
                ProductFormulaWeighRequirementInfo productFormulaWeighRequirementInfo = BeanUtil.toBean(obj, ProductFormulaWeighRequirementInfo.class);
                TicketRequirementGroupInfoVO.FormulaMaterialBatchInfo batchInfo = new TicketRequirementGroupInfoVO.FormulaMaterialBatchInfo();
                batchInfo.setFormulaMaterialId(formulaMaterial.getId());
                String key = formulaMaterial.getId() + "_" + index++;
                batchInfo.setKey(key);
                // 获取物料信息
                ProductMaterial material = productMaterialMapper.selectById(formulaMaterial.getMaterialId());
                if (material != null) {
                    batchInfo.setMaterialName(material.getName());
                    batchInfo.setMaterialSpecification(material.getSpecification());
                }
                batchInfo.setDryAndPureType(formulaMaterial.getDryPureType());
                batchInfo.setFormulaQuantity(BigDecimal.ZERO);
                batchInfo.setRequirementQuantity(productFormulaWeighRequirementInfo.getRequirementQuantity());
                // 需求用途
                batchInfo.setRequirementUsage(productFormulaWeighRequirementInfo.getRequirementUsage());
                batchInfo.setUnitId(productFormulaWeighRequirementInfo.getUnitId());
                // 设置单位名称
                String unitName = unitCache.getGlobalUnitName(productFormulaWeighRequirementInfo.getUnitId());
                batchInfo.setUnit(unitName);

                // 初始化批次列表
                batchInfo.setBatches(new ArrayList<>());
                batchInfo.setEnough(false); // 默认不足

                formulaBatchInfoList.add(batchInfo);
                formulaMaterialMap.put(key, batchInfo);
            }

        }

        // 如果是根据需求ID查询，需要查询物料批次信息
        if (query.getId() != null) {
            // 查询物料批次列表
            LambdaQueryWrapper<TicketRequirementDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TicketRequirementDO::getRequirementGroupId, query.getId());
            List<TicketRequirementDO> materialDOList = requirementMapper.selectList(wrapper);

            if (!CollectionUtils.isAnyEmpty(materialDOList)) {
                // 获取所有批次ID
                List<Long> batchIds = materialDOList.stream()
                        .map(TicketRequirementDO::getStorageMaterialBatchId)
                        .collect(Collectors.toList());

                // 批量查询物料批次信息
                List<StorageMaterialBatch> batchList = storageMaterialBatchMapper.selectBatchIds(batchIds);
                Map<Long, StorageMaterialBatch> batchMap = batchList.stream()
                        .collect(Collectors.toMap(StorageMaterialBatch::getId, batch -> batch, (a, b) -> a));

                // 按照配方物料ID分组
                Map<String, List<TicketRequirementDO>> requirementFormulaMap = new HashMap<>();
                for (TicketRequirementDO requirement : materialDOList) {
                    requirementFormulaMap.computeIfAbsent(requirement.getRequirementKey(), k -> new ArrayList<>()).add(requirement);
                }

                // 更新配方物料的批次信息
                for (Map.Entry<String, List<TicketRequirementDO>> entry : requirementFormulaMap.entrySet()) {
                    String key = entry.getKey();
                    List<TicketRequirementDO> materialList = entry.getValue();

                    TicketRequirementGroupInfoVO.FormulaMaterialBatchInfo formulaBatchInfo = formulaMaterialMap.get(key);
                    if (formulaBatchInfo == null) {
                        continue;
                    }

                    // 计算总配料量
                    BigDecimal totalFormulaQuantity = BigDecimal.ZERO;
                    // 计算总理论量，用于判断是否满足需求
                    BigDecimal totalTheoreticalQuantity = BigDecimal.ZERO;

                    // 添加批次信息
                    for (TicketRequirementDO materialDO : materialList) {
                        TicketRequirementGroupInfoVO.MaterialBatchInfo batchInfo = new TicketRequirementGroupInfoVO.MaterialBatchInfo();
                        batchInfo.setMaterialId(materialDO.getMaterialId());
                        batchInfo.setStorageMaterialBatchId(materialDO.getStorageMaterialBatchId());
                        batchInfo.setFormulaQuantity(materialDO.getFormulaQuantity());
                        batchInfo.setTheoreticalQuantity(materialDO.getTheoreticalQuantity());
                        batchInfo.setUnitId(materialDO.getUnitId());

                        // 累加配料量
                        totalFormulaQuantity = totalFormulaQuantity.add(materialDO.getFormulaQuantity());
                        // 累加理论量
                        totalTheoreticalQuantity = totalTheoreticalQuantity.add(materialDO.getTheoreticalQuantity());

                        // 设置单位名称
                        String unitName = unitCache.getGlobalUnitName(materialDO.getUnitId());
                        batchInfo.setUnit(unitName);

                        // 设置批次详细信息
                        StorageMaterialBatch batch = batchMap.get(materialDO.getStorageMaterialBatchId());
                        if (batch != null) {
                            batchInfo.setStorageMaterialBatchNo(batch.getMaterialBatchNo());
                            batchInfo.setHydration(batch.getHydration());
                            batchInfo.setNoHydrationContent(batch.getNoHydrationContent());
                            batchInfo.setExpiredDate(batch.getExpiredDate());
                            batchInfo.setSupplier(batch.getSupplier());
                            batchInfo.setProducer(batch.getProducer());
                        }

                        formulaBatchInfo.getBatches().add(batchInfo);
                        formulaMaterialMap.get(key).setFormulaQuantity(totalFormulaQuantity);
                    }

                    // 判断是否满足需求
                    if (totalTheoreticalQuantity.compareTo(formulaBatchInfo.getRequirementQuantity()) >= 0) {
                        formulaBatchInfo.setEnough(true);
                    }
                }
            }
        }

        // 设置返回结果
        resultVO.setFormulas(formulaBatchInfoList);

        log.info("{} 查询工单需求信息成功", LOG_PREFIX);
        return resultVO;
    }

    @Override
    public CommonPage<TicketRequirementGroupPageVO> page(TicketRequirementGroupPageDTO pageDTO) {
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
        List<TicketRequirementGroupPageVO> list = ticketRequirementGroupMapper.queryGroupPage(pageDTO, CollectionUtils.convertList(weighCentres, WeighCentre::getId));
        return CommonPage.convertPage(list);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editRequirementGroup(TicketRequirementGroupEditDTO editDTO) {
        log.info("{} 修改称量工单需求组, 参数:{}", LOG_PREFIX, editDTO);

        // 查询需求
        TicketRequirementGroupDO group = ticketRequirementGroupMapper.selectById(editDTO.getId());
        if (group == null) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_GROUP_NOT_EXIST);
        }

        // 更新需求主信息
        group.setMaterialId(editDTO.getProductId());
        group.setBomVersionId(editDTO.getBomVersionId());
        group.setBatchNo(editDTO.getBatchNo());
        group.setWeighCentreId(editDTO.getCentreWeighId());
        group.setPlanDate(editDTO.getPlanDate());
        group.setRemark(editDTO.getRemark());

        // 保存需求组信息
        ticketRequirementGroupMapper.updateById(group);


        // 更新需求组下所有需求的计划生产日期和称量中心
        List<TicketRequirementDO> requirementList = requirementMapper.selectByRequirementGroupId(group.getId());
        if (!CollectionUtils.isAnyEmpty(requirementList)) {
            for (TicketRequirementDO requirement : requirementList) {
                requirement.setPlanDate(editDTO.getPlanDate());
                requirement.setWeighCentreId(editDTO.getCentreWeighId());
            }
            requirementMapper.updateBatch(requirementList, 500);
        }
        log.info("{} 修改称量需求组成功, requirementId:{}", LOG_PREFIX, editDTO.getId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean makeSureRequirementGroup(Long id) {
        log.info("{} 确认称量组工单需求组, id: {}", LOG_PREFIX, id);

        // 查询需求
        TicketRequirementGroupDO group = ticketRequirementGroupMapper.selectById(id);
        if (group == null) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_GROUP_NOT_EXIST);
        }

        // 检查需求发布状态，只有编辑中状态才能确认
        if (group.getReleaseStatus() != TicketRequirementReleaseStatus.EDIT) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_GROUP_NOT_ALLOWED_MAKE_SURE);
        }

        // 更新需求发布状态为已确认
        group.setReleaseStatus(TicketRequirementReleaseStatus.RELEASE);

        // 保存需求信息
        int result = ticketRequirementGroupMapper.updateById(group);
        // 更新需求信息
        requirementMapper.update(null, new LambdaUpdateWrapper<TicketRequirementDO>()
                .eq(TicketRequirementDO::getRequirementGroupId, id)
                .set(TicketRequirementDO::getRequirementStatus, RequirementStatusEnum.UN_PLANNED)
        );
        log.info("{} 确认称量工单需求组成功, id: {}", LOG_PREFIX, id);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelRequirement(Long id) {
        log.info("{} 取消称量工单需求组, id: {}", LOG_PREFIX, id);

        // 查询需求
        TicketRequirementGroupDO group = ticketRequirementGroupMapper.selectById(id);
        if (group == null) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_GROUP_NOT_EXIST);
        }

        // 检查需求状态，只有未规划状态才能取消
        List<TicketRequirementDO> materialList = requirementMapper.selectByRequirementGroupId(group.getId());
        for (TicketRequirementDO ticketRequirementDO : materialList) {
            if (ticketRequirementDO.getRequirementStatus() != RequirementStatusEnum.UN_PLANNED) {
                throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_GROUP_NOT_ALLOWED_CANCEL);
            }
        }
        // 更新需求发布状态为已取消
        group.setReleaseStatus(TicketRequirementReleaseStatus.CANCELED);

        // 保存需求信息
        int result = ticketRequirementGroupMapper.updateById(group);

        log.info("{} 取消称量工单需求组成功, id: {}", LOG_PREFIX, id);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRequirement(TicketRequirementGroupRequirementDTO createDTO) {

        log.info("{} 保存称量工单需求组物料批次信息, id: {}", LOG_PREFIX, createDTO.getGroupId());

        // 查询需求组是否存在
        TicketRequirementGroupDO group = ticketRequirementGroupMapper.selectById(createDTO.getGroupId());
        if (group == null) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_GROUP_NOT_EXIST);
        }

        ProductFormulaMaterial productFormulaMaterial = productFormulaMaterialMapper.selectById(createDTO.getFormulaMaterialId());
        if (productFormulaMaterial == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXIST);
        }

        Map<String, String> usageMap = new HashMap<>();
        int index = 0;
        for (Object obj : productFormulaMaterial.getWeighRequirementList()) {
            ProductFormulaWeighRequirementInfo productFormulaWeighRequirementInfo = BeanUtil.toBean(obj, ProductFormulaWeighRequirementInfo.class);
            usageMap.put(createDTO.getFormulaMaterialId() + "_" + index++, productFormulaWeighRequirementInfo.getRequirementUsage());
        }


            // 删除原有的需求数据
        LambdaQueryWrapper<TicketRequirementDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TicketRequirementDO::getRequirementGroupId, createDTO.getGroupId());
        wrapper.eq(TicketRequirementDO::getRequirementKey, createDTO.getKey());
        requirementMapper.delete(wrapper);

        // 保存新的需求数据
        List<TicketRequirementDO> requirements = new ArrayList<>();

        for (TicketRequirementGroupRequirementDTO.MaterialBatchDTO batchDTO : createDTO.getBatches()) {
            TicketRequirementDO requirement = new TicketRequirementDO();
            requirement.setRequirementGroupId(createDTO.getGroupId());
            requirement.setMaterialId(batchDTO.getMaterialId());
            requirement.setFormulaMaterialId(createDTO.getFormulaMaterialId());
            requirement.setRequirementKey(createDTO.getKey());
            requirement.setStorageMaterialBatchId(batchDTO.getStorageMaterialBatchId());
            requirement.setFormulaQuantity(batchDTO.getFormulaQuantity());
            requirement.setTheoreticalQuantity(batchDTO.getTempTheoreticalQuantity());
            requirement.setUnitId(batchDTO.getUnitId());
            requirement.setPlanDate(group.getPlanDate());
            requirement.setWeighCentreId(group.getWeighCentreId());
            // 默认未规划状态
            requirement.setRequirementStatus(RequirementStatusEnum.UN_PLANNED);
            requirement.setRequirementUsage(usageMap.get(createDTO.getKey()));
            requirements.add(requirement);
        }

        // 保存新的物料批次信息
        if (!requirements.isEmpty()) {
            requirementMapper.insertBatch(requirements);
        }
        log.info("{} 保存称量工单需求组物料批次信息成功, id: {}", LOG_PREFIX, createDTO.getGroupId());
    }

    @Override
    public BigDecimal calcFormulaQuantity(TicketCalcFormulaQuantityDTO dto) {
        log.info("{} 计算配料量, 参数:{}", LOG_PREFIX, dto);
        ProductFormulaMaterial productFormulaMaterial = productFormulaMaterialMapper.selectById(dto.getFormulaMaterialId());
        BigDecimal hydration = dto.getHydration();
        BigDecimal noHydrationContent = dto.getNoHydrationContent();
        // 计算理论量 = 可用量 * (1-水分) * 含量
        BigDecimal theoreticalQuantity = MaterialQuantityCalculateUtil.calculateTheoreticalQuantity(dto.getAvailableQuantity(), hydration, noHydrationContent, productFormulaMaterial);
        log.info("{} 计算理论量结果: {}", LOG_PREFIX, theoreticalQuantity);
        // 若理论量小于等于需求量，则返回可用量
        if (theoreticalQuantity.compareTo(dto.getRequirementQuantity()) <= 0) {
            return dto.getAvailableQuantity();
        }
        BigDecimal formulaQuantity = PrecisionHelper.precision(MaterialQuantityCalculateUtil.calculateFormulaQuantity(dto.getRequirementQuantity(), hydration, noHydrationContent, productFormulaMaterial), productFormulaMaterial.getUnitId());
        log.info("{} 计算配料量结果: {}", LOG_PREFIX, formulaQuantity);
        return formulaQuantity;
    }

    @Override
    public List<TicketWeighRecordVO> getWeighRecord(Long groupId) {
        // 查询需求
        List<TicketRequirementDO> requirements = requirementMapper.selectByRequirementGroupId(groupId);
        return ticketService.getTicketWeighRecords(requirements);
    }

    @Override
    public List<String> validateSaveRequirement(TicketRequirementGroupRequirementDTO createDTO) {
        if (CollectionUtils.isAnyEmpty(createDTO.getBatches())){
            return new ArrayList<>();
        }
        List<Long> storageMaterialBatchIds = createDTO.getBatches().stream()
                .map(TicketRequirementGroupRequirementDTO.MaterialBatchDTO::getStorageMaterialBatchId)
                .collect(Collectors.toList());

        Map<Long, Long> batchUnitMap = createDTO.getBatches()
                .stream()
                .collect(Collectors.toMap(
                        TicketRequirementGroupRequirementDTO.MaterialBatchDTO::getStorageMaterialBatchId,
                        TicketRequirementGroupRequirementDTO.MaterialBatchDTO::getUnitId,
                        (v1, v2) -> v1));

        // 查询所有物料批次的占用量
        List<TicketRequirementOccupancyQuantityResult> occupancy = requirementMapper.selectOccupancyQuantity(storageMaterialBatchIds, createDTO.getKey());

        // 占用量
        Map<Long, BigDecimal> occupancyMap = new HashMap<>();

        // 批次占有量求和(基本单位)
        for (TicketRequirementOccupancyQuantityResult result : occupancy) {
            occupancyMap.putIfAbsent(result.getStorageMaterialBatchId(), BigDecimal.ZERO);
            BigDecimal addValue = unitCache.toBasic(result.getOccupancyQuantity(), result.getUnitId());
            occupancyMap.put(result.getStorageMaterialBatchId(), occupancyMap.get(result.getStorageMaterialBatchId()).add(addValue));
        }

        // 库存量
        Map<Long, BigDecimal> storageMaterialMap = new HashMap<>();
        List<StorageMaterial> storageMaterials = storageMaterialMapper.selectAvailableByBatchIds(storageMaterialBatchIds);

        for (StorageMaterial storageMaterial : storageMaterials){
            storageMaterialMap.putIfAbsent(storageMaterial.getStorageMaterialBatchId(), BigDecimal.ZERO);
            storageMaterialMap.put(storageMaterial.getStorageMaterialBatchId(), storageMaterialMap.get(storageMaterial.getStorageMaterialBatchId()).add(storageMaterial.getQuantity()));
        }

        List<String> list = new ArrayList<>();

        for (TicketRequirementGroupRequirementDTO.MaterialBatchDTO batchDTO : createDTO.getBatches()) {
            BigDecimal occupancyQuantity = occupancyMap.getOrDefault(batchDTO.getStorageMaterialBatchId(), BigDecimal.ZERO);
            BigDecimal storageQuantity = storageMaterialMap.getOrDefault(batchDTO.getStorageMaterialBatchId(), BigDecimal.ZERO);
            BigDecimal availableQuantity = storageQuantity.subtract(occupancyQuantity);
            BigDecimal theoretical = unitCache.toBasic(batchDTO.getTempTheoreticalQuantity(), batchDTO.getUnitId());
            if (availableQuantity.compareTo(theoretical) < 0) {
                log.info("{}批次库存预警: available: {}, theoretical: {}", LOG_PREFIX, availableQuantity, theoretical);
                list.add(storageMaterialBatchMapper.selectById(batchDTO.getStorageMaterialBatchId()).getMaterialBatchNo());
            }
        }
        return list;
    }
} 