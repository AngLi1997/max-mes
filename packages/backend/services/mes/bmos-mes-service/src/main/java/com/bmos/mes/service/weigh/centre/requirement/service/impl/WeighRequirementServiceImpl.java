package com.bmos.mes.service.weigh.centre.requirement.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.constant.SystemParamsConfigConstants;
import com.bmos.mes.common.enums.formula.QuantityTypeEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighProcess;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.components.BusinessComponentManager;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.mapper.ProductFormulaVersionMapper;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.model.ProductFormulaVersion;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreMapper;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentre;
import com.bmos.mes.service.weigh.centre.requirement.dto.MaterialInputComponentConfig;
import com.bmos.mes.service.weigh.centre.requirement.dto.WeighRequirementCreateDTO;
import com.bmos.mes.service.weigh.centre.requirement.dto.WeighRequirementPageQuery;
import com.bmos.mes.service.weigh.centre.requirement.mapper.IWeighRequirementMapper;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighRequirement;
import com.bmos.mes.service.weigh.centre.requirement.service.IWeighRequirementService;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementProgram;
import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementVO;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 称量中心称量需求service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 17:51
 */
@Service
@Slf4j
public class WeighRequirementServiceImpl implements IWeighRequirementService {

    private static final String LOG_PREFIX = "[称量需求]";

    @Resource
    private IWeighRequirementMapper weighRequirementMapper;

    @Resource
    private IWeighCentreMapper weighCentreMapper;

    @Resource
    private ProductFormulaMaterialMapper formulaMaterialMapper;

    @Resource
    private ProductFormulaVersionMapper productFormulaVersionMapper;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private BusinessComponentManager businessComponentManager;

    @Resource
    private PlatformParameterClientImpl platformParameterClient;

    // 称量需求提前规划时间，默认为15天；(优先使用配置里的)
    private static final int DEFAULT_AHEAD_DAYS = 5;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRequirement(Long productPlanId, List<BusinessComponentInstance> componentInstances) {
        log.info("{}创建称量需求: {}", LOG_PREFIX, productPlanId);
        // 根据生产计划id查询所有物料称量组件配置
        if (CollectionUtil.isEmpty(componentInstances)) {
            return;
        }
        List<WeighRequirementCreateDTO> weighRequirementComponentsConfig = queryConfigList(productPlanId, componentInstances);
        log.info("{}称量需求条数: {}", LOG_PREFIX, weighRequirementComponentsConfig.size());
        if (CollectionUtil.isEmpty(weighRequirementComponentsConfig)) {
            log.info("{}称量需求为空, 不进行创建称量需求", LOG_PREFIX);
            return;
        }

        // 校验生产计划
        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }

        // 校验配方物料参数
        Set<Long> formulaMaterialIds = weighRequirementComponentsConfig.stream()
                .map(WeighRequirementCreateDTO::getFormulaMaterialId)
                .collect(Collectors.toSet());
        List<ProductFormulaMaterial> formulaMaterials = formulaMaterialMapper.selectBatchIds(formulaMaterialIds);
        if (formulaMaterials.size() != formulaMaterialIds.size()) {
            throw new ValidationException("包含不存在的配方物料id");
        }
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = formulaMaterials.stream()
                .collect(Collectors.toMap(BaseDO::getId, Function.identity(), (v1, v2) -> v1));
        if (!validateCreateParams(productPlanId, weighRequirementComponentsConfig)) {
            log.info("{} 参数校验不通过, 不进行创建称量需求", LOG_PREFIX);
            return;
        }
        List<WeighRequirement> list = new ArrayList<>();
        for (WeighRequirementCreateDTO weighRequirementCreateDTO : weighRequirementComponentsConfig) {
            WeighRequirement weighRequirement = new WeighRequirement();
            weighRequirement.setProcedureStepConfigId(weighRequirementCreateDTO.getProcedureStepConfigId());
            ProductFormulaMaterial productFormulaMaterial = formulaMaterialMap.get(weighRequirementCreateDTO.getFormulaMaterialId());
            if (productFormulaMaterial != null) {
                weighRequirement.setFormulaMaterialId(productFormulaMaterial.getId());
                weighRequirement.setUnitId(productFormulaMaterial.getUnitId());
            }
            weighRequirement.setWeighCentreId(weighRequirementCreateDTO.getWeighCentreId());
            weighRequirement.setRequirementDate(weighRequirementCreateDTO.getRequirementDate());
            weighRequirement.setExpiredDate(weighRequirementCreateDTO.getExpiredDate());
            weighRequirement.setRequirementQuantity(weighRequirementCreateDTO.getRequirementQuantity());
            weighRequirement.setProductPlanId(productPlanId);
            weighRequirement.setBatchNo(plan.getBatchNo());
            weighRequirement.setProductName(plan.getProductName());
            weighRequirement.setProductMergeCode(plan.getProductMergeCode());
            weighRequirement.setRequirementStatus(RequirementStatusEnum.UN_PLANNED);
            weighRequirement.setWeighStatus(RequirementWeighStatusEnum.PENDING);
            // 默认即物料称量
            weighRequirement.setWeighProcess(RequirementWeighProcess.MAIN);
            weighRequirement.setBusinessComponentInstanceId(weighRequirementCreateDTO.getComponentInstanceId());
            list.add(weighRequirement);
        }
        log.info("{}共创建{}条称量需求", LOG_PREFIX, list.size());
        weighRequirementMapper.insertBatch(list);
    }

    @Override
    public CommonPage<WeighRequirementVO> queryPage(WeighRequirementPageQuery pageQuery) {
        if (pageQuery.getRequirementDateEnd() == null) {
            // 默认没传筛时间的情况下 默认根据系统参数显示未来15天的数据
            pageQuery.setRequirementDateEnd(LocalDate.now().plusDays(getSystemConfigDefaultAheadDays()));
        }
        pageQuery.setRequirementStatus(RequirementStatusEnum.UN_PLANNED.getValue());
        List<Long> deptIds = platformApiAdaptor.deptIds();
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<WeighRequirementVO> list = weighRequirementMapper.queryList(pageQuery, deptIds);
        return CommonPage.convertPage(list);
    }

    @Override
    public List<Long> listAutoProgramRequirements() {
        WeighRequirementPageQuery pageQuery = new WeighRequirementPageQuery();
        LocalDate defaultEndDate = LocalDate.now().plusDays(getSystemConfigDefaultAheadDays());
        pageQuery.setRequirementDateEnd(defaultEndDate);
        pageQuery.setRequirementStatus(RequirementStatusEnum.UN_PLANNED.getValue());
        List<Long> deptIds = platformApiAdaptor.deptIds();
        return weighRequirementMapper.queryList(pageQuery, deptIds)
                .stream()
                .map(WeighRequirementVO::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<WeighRequirement> selectByIds(Collection<Long> requirementIds) {
        return weighRequirementMapper.selectBatchIds(requirementIds);
    }

    @Override
    public List<WeighRequirementVO> queryListByTaskId(Long taskId) {
        return weighRequirementMapper.queryListByTaskId(taskId);
    }

    @Override
    public List<WeighRequirementVO> queryUnPlanedRequirementList(Long materialId, Long unitId, Long weighCentreId, List<Long> deptIds, RequirementStatusEnum requirementStatus) {
        return weighRequirementMapper.queryUnPlanedRequirementList(materialId, unitId, weighCentreId, deptIds, requirementStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseRequirement(List<WeighRequirement> requirements) {
        weighRequirementMapper.releaseRequirement(requirements);
    }

    @Override
    public List<WeighRequirement> selectListByTaskId(Long taskId) {
        if (taskId == null) {
            return new ArrayList<>();
        }
        return weighRequirementMapper.selectListByTaskId(taskId);
    }

    @Override
    public List<WeighRequirementProgram> selectRequirementProgramListByIds(List<Long> requirementIds) {
        if (CollectionUtil.isEmpty(requirementIds)) {
            return new ArrayList<>();
        }
        return weighRequirementMapper.selectRequirementProgramListByIds(requirementIds);
    }

    @Override
    public void updateBatch(Collection<WeighRequirement> requirements) {
        weighRequirementMapper.updateBatch(requirements);
    }

    /**
     * 获取称量需求提前规划天数(有配值拿配置 没配置拿系统默认的15)
     *
     * @return 默认提前天数
     */
    private int getSystemConfigDefaultAheadDays() {
        String valueByCode = platformParameterClient.getValueByCode(SystemParamsConfigConstants.MES_WEIGH_REQUIRE_ADVANCE);
        if (StringUtils.isBlank(valueByCode)) {
            log.info("{}系统配置称量需求提前规划时间参数为空，使用默认值:{}", LOG_PREFIX, DEFAULT_AHEAD_DAYS);
            return DEFAULT_AHEAD_DAYS;
        }

        try {
            return Integer.parseInt(valueByCode);
        } catch (Exception e) {
            log.info("{}系统配置称量需求提前规划时间参数[{}]错误，使用默认值:{}", LOG_PREFIX, valueByCode, DEFAULT_AHEAD_DAYS);
            return DEFAULT_AHEAD_DAYS;
        }
    }

    /**
     * 校验创建任务的参数
     *
     * @param productPlanId                    产品计划id
     * @param weighRequirementComponentsConfig 称量需求相关参数
     * @return true：校验通过，false：校验不通过
     */
    private boolean validateCreateParams(Long productPlanId, List<WeighRequirementCreateDTO> weighRequirementComponentsConfig) {
        if (productPlanId == null) {
            log.info("{}无产品计划id，无需创建称量需求", LOG_PREFIX);
            return false;
        }
        if (CollectionUtil.isEmpty(weighRequirementComponentsConfig)) {
            log.info("{}无组件配置信息，无需创建称量需求", LOG_PREFIX);
            return false;
        }
        // 校验称量中心参数
        Set<Long> weighCentreIds = weighRequirementComponentsConfig.stream()
                .map(WeighRequirementCreateDTO::getWeighCentreId)
                .collect(Collectors.toSet());
        List<WeighCentre> weighCentres = weighCentreMapper.selectBatchIds(weighCentreIds);
        if (weighCentres.size() != weighCentreIds.size()) {
            throw new ValidationException("包含不存在的称量中心id");
        }

        if (weighRequirementMapper.existProductPlanId(productPlanId)) {
            log.info("{} 已存在生产批次id{}的称量需求", LOG_PREFIX, productPlanId);
            return false;
        }
        return true;
    }

    /**
     * 从生产计划的组件配置中获取所有需要创建的任务配置
     *
     * @param productPlanId      生产计划id
     * @param componentInstances 产出称量组件实例
     * @return 称量需求相关参数
     */
    private List<WeighRequirementCreateDTO> queryConfigList(Long productPlanId, List<BusinessComponentInstance> componentInstances) {
        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null) {
            return new ArrayList<>();
        }
        Set<Long> existSet = new HashSet<>();
        List<WeighRequirementCreateDTO> result = new ArrayList<>();
        for (BusinessComponentInstance componentInstance : componentInstances) {
            if (componentInstance.getProcedureStepConfigId() != null) {
                if (existSet.contains(componentInstance.getProcedureStepConfigId())) {
                    // 若工序步骤配置为记录不复用，则认为是不同的组件，物料需求是另一个；
                    continue;
                }
                existSet.add(componentInstance.getProcedureStepConfigId());
            }
            List<WeighRequirementCreateDTO> componentsConfigs = getComponentsConfig(componentInstance, plan);
            if (CollectionUtil.isNotEmpty(componentsConfigs)) {
                result.addAll(componentsConfigs);
            }
        }
        return result;
    }

    /**
     * 读取组件中的配置信息
     *
     * @param componentInstance 组件实例
     * @param plan              生产计划
     * @return
     */
    private List<WeighRequirementCreateDTO> getComponentsConfig(BusinessComponentInstance componentInstance, Plan plan) {
        List<WeighRequirementCreateDTO> result = new ArrayList<>();
        if (StrUtil.isBlank(componentInstance.getComponentConfigJson())) {
            return result;
        }
        MaterialInputComponentConfig config = JSONUtil.toBean(componentInstance.getComponentConfigJson(), MaterialInputComponentConfig.class);
        List<Long> formulaMaterialIds = config.getMaterialList()
                .stream()
                .map(MaterialInputComponentConfig.MaterialConfig::getFormulaMaterialId)
                .collect(Collectors.toList());
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = formulaMaterialMapper.selectBatchIds(formulaMaterialIds)
                .stream().collect(Collectors.toMap(ProductFormulaMaterial::getId, Function.identity(), (k1, k2) -> k1));
        List<Long> formulaMaterialVersionIds = formulaMaterialMap.values().stream()
                .map(ProductFormulaMaterial::getVersionId)
                .collect(Collectors.toList());
        Map<Long, ProductFormulaVersion> formulaVersionMap = productFormulaVersionMapper.selectBatchIds(formulaMaterialVersionIds)
                .stream().collect(Collectors.toMap(ProductFormulaVersion::getId, Function.identity(), (k1, k2) -> k1));

        // 计划生产日期
        LocalDate productDate = plan.getProductDate();
        for (MaterialInputComponentConfig.MaterialConfig materialConfig : config.getMaterialList()) {
            ProductFormulaMaterial productFormulaMaterial = formulaMaterialMap.get(materialConfig.getFormulaMaterialId());
            ProductFormulaVersion productFormulaVersion = formulaVersionMap.get(productFormulaMaterial.getVersionId());
            WeighRequirementCreateDTO dto = new WeighRequirementCreateDTO();
            dto.setFormulaMaterialId(productFormulaMaterial.getId());
            dto.setWeighCentreId(materialConfig.getProductionPreparationCenter());
            dto.setRequirementDate(productDate.plusDays(materialConfig.getRequirementTime()));
            dto.setExpiredDate(productDate.plusDays(materialConfig.getDemandExpirationTime()));
            dto.setRequirementQuantity(getFinalTaskQuantity(materialConfig.getDemand(), plan.getBatchQuantity(), productFormulaVersion.getBatchQuantity(), productFormulaMaterial));
            dto.setComponentInstanceId(componentInstance.getId());
            dto.setProcedureStepConfigId(componentInstance.getProcedureStepConfigId());
            result.add(dto);
        }
        return result;
    }

    /**
     * 计算最终称量需求量
     * 1.配方批量为500L，生产批量为1000L，若物料在该组件的需求量为10kg，且数量类型为标准量，则创建的物料需求为20kg；
     * 2.若数量类型为固定量或适量，则创建的物料需求为10kg
     *
     * @param configQuantity       组件配置的需求量
     * @param batchTargetQuantity  生产批量
     * @param formulaBatchQuantity 配方批量
     * @param formulaMaterial      配方物料信息
     * @return
     */
    private BigDecimal getFinalTaskQuantity(BigDecimal configQuantity, BigDecimal batchTargetQuantity, BigDecimal formulaBatchQuantity, ProductFormulaMaterial formulaMaterial) {
        if (Objects.equals(formulaMaterial.getQuantityType(), QuantityTypeEnum.STANDARD_QUANTITY)) {
            // 类型为标准量
            BigDecimal result = configQuantity.multiply(batchTargetQuantity).divide(formulaBatchQuantity, 10, RoundingMode.HALF_UP);
            // 修约
            return MaterialQuantityCalculateUtil.roundingOff(result, formulaMaterial);
        } else {
            // 类型为固定量或适量
            return MaterialQuantityCalculateUtil.roundingOff(configQuantity, formulaMaterial);
        }
    }
}
