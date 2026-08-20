package com.bmos.mes.service.formula.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.common.enums.formula.FormulaVersionStatusEnum;
import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import com.bmos.mes.service.audit.dto.FlowStartDTO;
import com.bmos.mes.service.audit.service.FlowAuditService;
import com.bmos.mes.service.formula.convert.ProductFormulaConverter;
import com.bmos.mes.service.formula.dto.*;
import com.bmos.mes.service.formula.mapper.ProductFormulaMapper;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.mapper.ProductFormulaVersionMapper;
import com.bmos.mes.service.formula.model.ProductFormula;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.model.ProductFormulaVersion;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.formula.vo.*;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.aspect.OperationHistoryContext;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import com.bmos.mes.service.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.mapper.ProcessVersionMapper;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.service.ProcedureModelMaterialService;
import com.bmos.mes.service.process.service.ProcessVersionService;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import com.bmos.mes.service.product.convert.ProductMaterialCategoryConverter;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.requisition.vo.RequisitionPlanMaterialVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductFormulaConfigureServiceImpl implements ProductFormulaConfigureService {

    @Autowired
    private ProductFormulaMapper productFormulaMapper;

    @Autowired
    private ProductFormulaVersionMapper productFormulaVersionMapper;

    @Autowired
    private ProductMaterialService productMaterialService;

    @Autowired
    private ProductFormulaMaterialMapper productFormulaMaterialMapper;

    @Autowired
    private ProcedureModelMaterialService materialService;

    @Autowired
    private ResourcePermissionService resourcePermissionService;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    private FlowAuditService flowAuditService;

    @Autowired
    private OperationHistoryService logService;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private PlanMapper planMapper;

    @Resource
    @Lazy
    private ProcessVersionService versionService;

    @Resource
    @Lazy
    private ProcedureExpressionService expressionService;

    @Resource
    private ProcessVersionMapper processVersionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.PRODUCT_FORMULA, operationType = OperationType.SAVE, remark = "#dto.description", businessId = "#getId")
    public void saveProductFormula(ProductFormulaSaveDTO dto) {
        ProductMaterial product = productMaterialService.selectById(dto.getProductId());
        if (ObjectUtil.isNull(product)) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        // 校验配方名称重复
        if (productFormulaMapper.checkNameExists(dto.getName())) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_NAME_EXISTED);
        }
        ProductFormula productFormula = ProductFormulaConverter.INSTANCE.convertToProductFormula(dto);
        productFormula.setProductName(product.getName());
        productFormula.setProductMergeCode(product.getMergeCode());
        productFormula.setProductSpecification(product.getSpecification());
        productFormulaMapper.insert(productFormula);
        // 保存数据权限
        resourcePermissionService
                .save(ResourcePermissionSaveDTO.builder().resourceId(productFormula.getId()).deptIds(dto.getDeptIds()).build());
        // 初始化版本
        ProductFormulaVersion version = getInitialVersion(dto, productFormula);
        productFormulaVersionMapper.insert(version);
        // 配方物料
        List<ProductFormulaMaterial> materials = ProductFormulaConverter.INSTANCE.convertToProductFormulaMaterialList(dto.getMaterialList());
        materials.forEach(material -> material.setVersionId(version.getId()));
        handleMaterialInfo(materials);
        productFormulaMaterialMapper.insertBatch(materials);
        OperationHistoryContext.putVariable(version, ProductFormulaVersion::getId);
    }

    private void handleMaterialInfo(List<ProductFormulaMaterial> materials) {
        List<Long> ids = CollectionUtils.convertList(materials, ProductFormulaMaterial::getMaterialId);
        List<ProductMaterial> productMaterials = productMaterialService.getByIds(ids);
        if (productMaterials.size() != ids.size()) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        Map<Long, ProductMaterial> map = CollectionUtils.convertMap(productMaterials, ProductMaterial::getId);
        materials.forEach(material -> {
            ProductMaterial productMaterial = map.get(material.getMaterialId());
            material.setMaterialMergeCode(productMaterial.getMergeCode());
            material.setMaterialSpecification(productMaterial.getSpecification());
            material.setMaterialType(CategoryInfoTypeEnum.getEnumByValue(productMaterial.getCategoryType()));
            material.setMaterialName(productMaterial.getName());
            material.setScaleLength(material.getScale().scale());
        });
    }

    @Override
    public CommonPage<ProductFormulaPageVO> getProductFormulaPage(ProductFormulaPageQueryDTO dto) {
        // 数据权限
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return CommonPage.convertPage(PageInfo.emptyPageInfo());
        }
        dto.setDeptIds(deptIds);
        if (ObjectUtil.isNotNull(dto.getCategoryId())) {
            List<Long> productIdList = productMaterialService.getProductIdList(CategoryInfoTypeEnum.PRODUCTION, dto.getCategoryId(), null);
            if(CollUtil.isEmpty(productIdList)){
                return CommonPage.convertPage(PageInfo.emptyPageInfo());
            }
            dto.setProductIdList(productIdList);
            return CommonPage.convertPage(productFormulaMapper.selectPageByProductIdList(dto));
        }
        return CommonPage.convertPage(productFormulaMapper.selectPageList(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.PRODUCT_FORMULA, operationType = OperationType.SAVE, remark = "#dto.description", businessId = "#getId")
    public void saveProductFormulaVersion(ProductFormulaSaveVersionDTO dto) {
        Long formulaId = dto.getProductFormulaId();
        if (productFormulaVersionMapper.checkExistedVersionNo(formulaId, dto.getVersionNo(), null)) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_VERSION_EXISTED);
        }
        ProductFormulaVersion version = ProductFormulaConverter.INSTANCE.convertToProductFormulaVersion(dto);
        version.setStatus(FormulaVersionStatusEnum.EDIT);
        version.setEnable(false);
        productFormulaVersionMapper.insert(version);
        OperationHistoryContext.putVariable(version, ProductFormulaVersion::getId);
        List<ProductFormulaMaterial> materials = ProductFormulaConverter.INSTANCE.convertToProductFormulaMaterialList(dto.getMaterialList());
        materials.forEach(material -> material.setVersionId(version.getId()));
        handleMaterialInfo(materials);
        productFormulaMaterialMapper.insertBatch(materials);
    }

    @Override
    public ProductFormulaVersionDetailVO getProductFormulaVersionDetail(Long versionId) {
        ProductFormulaVersionDetailVO vo = productFormulaVersionMapper.selectDetailById(versionId);
        List<ProductFormulaMaterial> materialList = productFormulaMaterialMapper.selectByVersionId(versionId);
        List<ProductFormulaMaterialVO> materialVOList = ProductFormulaConverter.INSTANCE.convertToProductFormulaMaterialVO(materialList);
        vo.setMaterialList(materialVOList);
        vo.setUnitName(unitCache.getGlobalUnitName(vo.getUnitId()));
        materialVOList.forEach(material->{
            material.setUnitName(unitCache.getGlobalUnitName(material.getUnitId()));
        });
        return vo;
    }

    @Override
    public CommonPage<ProductFormulaVersionPageVO> getProductFormulaVersionPage(ProductFormulaVersionPageQueryDTO dto) {
        return CommonPage.convertPage(productFormulaVersionMapper.selectPageList(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeProductFormulaVersionState(ProductFormulaVersionChangeStateDTO dto) {
        ProductFormulaVersion version = productFormulaVersionMapper.selectById(dto.getId());
        if (ObjectUtil.isNull(version)) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_VERSION_NOT_EXISTED);
        }
        if (BooleanUtil.isTrue(dto.getState())) {
            // 校验是否已有其他启用版本
            Long productFormulaId = version.getProductFormulaId();
            if (productFormulaVersionMapper.checkExistedEnabledVersion(productFormulaId)) {
                throw new BmosException(MesResponseCode.PRODUCT_FORMULA_EXISTED_ENABLE_VERSION);
            }
        }
        version.setEnable(dto.getState());
        productFormulaVersionMapper.updateById(version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.PRODUCT_FORMULA, operationType = OperationType.FORMULA_EDIT, remark = "#dto.description", businessId = "#getId")
    public void editProductFormulaVersion(ProductFormulaVersionEditDTO dto) {
        ProductFormulaVersion version = productFormulaVersionMapper.selectById(dto.getId());
        if (ObjectUtil.isNull(version)) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_VERSION_NOT_EXISTED);
        }
        if (productFormulaVersionMapper.checkExistedVersionNo(version.getProductFormulaId(), dto.getVersionNo(), dto.getId())) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_VERSION_EXISTED);
        }
        productFormulaMaterialMapper.deleteByVersionId(dto.getId());
        List<ProductFormulaMaterial> materials = ProductFormulaConverter.INSTANCE.convertToProductFormulaMaterialList(dto.getMaterialList());
        materials.forEach(material -> material.setVersionId(dto.getId()));
        handleMaterialInfo(materials);
        productFormulaMaterialMapper.insertBatch(materials);
        version.setVersionNo(dto.getVersionNo());
        version.setBatchQuantity(dto.getBatchQuantity());
        version.setUnitId(dto.getUnitId());
        version.setDescription(dto.getDescription());
        productFormulaVersionMapper.updateById(version);
        OperationHistoryContext.putVariable(version, ProductFormulaVersion::getId);
    }

    @Override
    @OperationHistory(module = BusinessModule.PRODUCT_FORMULA, operationType = OperationType.SUBMIT_AUDIT, businessId = "#versionId")
    public void auditProductFormulaVersion(Long versionId) {
        ProductFormulaVersion version = productFormulaVersionMapper.selectById(versionId);
        if (ObjectUtil.isNull(version)) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_VERSION_NOT_EXISTED);
        }
        if (ObjectUtil.notEqual(FormulaVersionStatusEnum.EDIT, version.getStatus())) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_VERSION_NOT_EDIT);
        }
        version.setStatus(FormulaVersionStatusEnum.APPROVAL);
        ProductFormula productFormula = productFormulaMapper.selectById(version.getProductFormulaId());
        // 提交审核
        FlowStartDTO flowStartDTO = new FlowStartDTO();
        flowStartDTO.setExtField(version.getVersionNo());
        flowStartDTO.setCode(AuditCategoryCodeEnum.PRODUCT_FORMULA.getCode());
        flowStartDTO.setCategoryCode(AuditCategoryCodeEnum.PRODUCT_FORMULA.getCode());
        flowStartDTO.setName(productFormula.getName());
        flowStartDTO.setBusinessKey(String.valueOf(version.getId()));
        String processInstanceId = flowAuditService.flowAuditStart(flowStartDTO);
        version.setProcessInstanceId(processInstanceId);
        productFormulaVersionMapper.updateById(version);
    }

    @Override
    @OperationHistory(module = BusinessModule.PRODUCT_FORMULA, operationType = OperationType.APPROVE_AUDIT, businessId = "#getId", remark = "#comment")
    public void auditSuccess(String processInstanceId, String comment, String userId) {
        ProductFormulaVersion version = productFormulaVersionMapper.selectByProcessInstanceId(processInstanceId);
        version.setStatus(FormulaVersionStatusEnum.CONFIRM);
        productFormulaVersionMapper.updateById(version);
        OperationHistoryContext.putVariable(version, ProductFormulaVersion::getId);
    }

    @Override
    @OperationHistory(module = BusinessModule.PRODUCT_FORMULA, operationType = OperationType.REJECT_AUDIT, businessId = "#getId",
                    remark = "#remark",nodeName = "#nodeName",comment = "#comment")
    public void auditTermination(String processInstanceId, String comment,String remark, String userId,String nodeName) {
        ProductFormulaVersion version = productFormulaVersionMapper.selectByProcessInstanceId(processInstanceId);
        version.setStatus(FormulaVersionStatusEnum.EDIT);
        productFormulaVersionMapper.updateById(version);
        OperationHistoryContext.putVariable(version, ProductFormulaVersion::getId);
    }

    @Override
    public CommonPage<ProductFormulaAuditPageVO> getProductFormulaAuditPage(ProductFormulaAuditPageQueryDTO dto) {
        FlowAuditTaskDTO flowAuditTaskDTO = dto.convertAuditTaskDTO();
        if (dto.isExistsSearchCondition()) {
            List<Long> ids = productFormulaVersionMapper.selectAuditBusinessIds(dto);
            if (CollUtil.isEmpty(ids)) {
                return CommonPage.convertPage(PageInfo.emptyPageInfo());
            }
            flowAuditTaskDTO.setBusinessKeyList(CollectionUtils.convertList(ids, String::valueOf));
        }
        PageQueryResp<List<TaskListResp>> listPageQueryResp = flowAuditService.queryToDoListByCategory(flowAuditTaskDTO);
        return handleProductFormulaAuditPage(dto, listPageQueryResp);
    }

    @Override
    public boolean existedProductFormula(Long productId) {
        return productFormulaMapper.existedProductFormula(productId);
    }

    @Override
    public boolean existedFormulaMaterial(Long materialId) {
        return productFormulaMaterialMapper.existedFormulaMaterial(materialId);
    }

    @Override
    public List<ProductFormulaListVO> getEnableProductFormulaList(Long productId) {
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        return productFormulaMapper.selectEnableProductFormulaByProductId(productId, deptIds);
    }

    @Override
    public List<ProductFormulaMaterialListVO> getProductFormulaMaterialPullDownList(Long versionId) {
        List<ProductFormulaMaterial> materials = productFormulaMaterialMapper.selectByVersionId(versionId);
        return ProductFormulaConverter.INSTANCE.convertToProductFormulaMaterialListVO(materials);
    }

    @Override
    public List<ProductFormulaMaterial> getProductFormulaMaterialList(Long versionId) {
        return productFormulaMaterialMapper.selectByVersionId(versionId);
    }

    @Override
    public ProductFormulaInfo getProductFormulaInfo(Long formulaVersionId) {
        ProductFormulaVersion productFormulaVersion = productFormulaVersionMapper.selectById(formulaVersionId);
        List<ProductFormulaMaterial> materials = productFormulaMaterialMapper.selectByVersionId(formulaVersionId);
        ProductFormulaInfo productFormulaInfo = new ProductFormulaInfo();
        productFormulaInfo.setBatchQuantity(productFormulaVersion.getBatchQuantity());
        productFormulaInfo.setMaterials(materials);
        return productFormulaInfo;
    }

    @Override
    public List<ProductFormulaMaterialListVO> getFormulaMaterialVOListByProcedureModelId(ListProcedureMaterialDTO dto) {
        List<ProductFormulaMaterial> materials = productFormulaMaterialMapper.selectProcedureMaterial(dto);
        List<ProductFormulaMaterialListVO> result = ProductFormulaConverter.INSTANCE.convertToProductFormulaMaterialListVO(materials);
        result.forEach(e->e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId())));
        if (ObjectUtil.isNotNull(dto.getStepModelId())){
            List<Long> deleteMaterialIds = new ArrayList<>();
            List<Long> materialIds = CollectionUtils.convertList(result, ProductFormulaMaterialListVO::getId);
            List<String> conditionList = expressionService.getStepModelCondition(Collections.singletonList(dto.getStepModelId()),
                    Collections.singletonList(ConditionTypeEnum.MATERIAL_RESERVE_NUMBER.getValue()));
            conditionList.forEach(item->{
                ConditionDetailVO detailVO = JsonUtils.parseObject(item, ConditionDetailVO.class);
                if (!materialIds.contains(detailVO.getMaterialId())){
                    deleteMaterialIds.add(detailVO.getMaterialId());
                }
            });
            if (CollUtil.isNotEmpty(deleteMaterialIds)){
                List<ProductFormulaMaterial> list = productFormulaMaterialMapper.selectListByDisabledIds(deleteMaterialIds);
                result.addAll(list.stream().map(item->{
                    ProductFormulaMaterialListVO vo = new ProductFormulaMaterialListVO();
                    vo.setId(item.getId());
                    vo.setMaterialId(item.getMaterialId());
                    vo.setMaterialName(item.getMaterialName());
                    vo.setMaterialMergeCode(item.getMaterialMergeCode());
                    vo.setDisabled(true);
                    vo.setUnitId(item.getUnitId());
                    return vo;
                }).collect(Collectors.toList()));
            }
        }
        return result;
    }

    @Override
    public List<ProductFormulaMaterial> getFormulaMaterialListByProcedureModelId(Long procedureModelId) {
        return productFormulaMaterialMapper.selectByProcedureId(procedureModelId);
    }

    @Override
    public void auditNodeProductLog(String businessKey, String remark, String userId, String name,String comment) {
        saveRecordExecutionHistoryLog(remark,userId,Long.valueOf(businessKey),name,comment);
    }

    @Override
    public List<String> getAuditBusinessKey(List<Long> deptIdList) {
        return productFormulaMapper.getAuditBusinessKey(deptIdList);
    }

    private void saveRecordExecutionHistoryLog(String remark, String userId, Long businessId, String nodeName,String comment) {
        logService.save(OperationLogModel.builder()
                .module(BusinessModule.PRODUCT_FORMULA.name())
                .businessId(businessId)
                .operationType(OperationType.APPROVE_AUDIT.getValue())
                .remark(remark)
                .comment(comment)
                .nodeName(nodeName)
                .createBy(userId)
                .build());
    }

    @Override
    public List<ProductFormulaMaterial> getFormulaMaterialListByIds(List<Long> formulaMaterialIdList) {
        if(CollUtil.isEmpty(formulaMaterialIdList)){
            return new ArrayList<>();
        }
        return productFormulaMaterialMapper.selectByIdList(formulaMaterialIdList);
    }

    @Override
    public ProductFormulaMaterial getFormulaMaterialById(Long formulaMaterialId) {
        return productFormulaMaterialMapper.selectById(formulaMaterialId);
    }

    @Override
    public ProductFormulaInfo getProductFormulaInfoByPlanId(Long productPlanId) {
        Long formulaVersionId = planMapper.selectPlanFormulaVersionId(productPlanId);
        if (formulaVersionId == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_VERSION_NOT_EXISTED);
        }
        ProductFormulaVersion productFormulaVersion = productFormulaVersionMapper.selectById(formulaVersionId);
        List<ProductFormulaMaterial> materials = productFormulaMaterialMapper.selectByVersionId(formulaVersionId);
        ProductFormulaInfo productFormulaInfo = new ProductFormulaInfo();
        productFormulaInfo.setBatchQuantity(productFormulaVersion.getBatchQuantity());
        productFormulaInfo.setMaterials(materials);
        return productFormulaInfo;
    }

    @Override
    public List<ProductFormulaMaterial> selectByIds(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)){
            return new ArrayList<>();
        }
        return productFormulaMaterialMapper.selectBatchIds(ids);
    }

    @Override
    public ProductFormulaVersion getVersionById(Long versionId) {
        return productFormulaVersionMapper.selectById(versionId);
    }

    @Override
    public Map<Long, ProductFormulaInfo> getProductFormulaInfoByPlanIds(List<Long> planIds) {
        Map<Long, ProductFormulaInfo> map = new HashMap<>();
        for (Long planId : planIds) {
            if (!map.containsKey(planId)){
                map.put(planId, getProductFormulaInfoByPlanId(planId));
            }
        }
        return map;
    }

    @Override
    public List<ProductFormulaListVO> getProcessEnableProductFormulaList(Long productId, Long processVersionId) {
        List<ProductFormulaListVO> vos = this.getEnableProductFormulaList(productId);
        if (ObjectUtil.isNull(processVersionId)){
            return vos;
        }
        ProcessVersion version = versionService.getById(processVersionId);
        List<Long> formulaVersionIds = CollectionUtils.convertList(vos, ProductFormulaListVO::getProductFormulaVersionId);
        ProductFormulaListVO vo = productFormulaMapper.getFormulaByVersionId(version.getProductFormulaVersionId());
        if (CollUtil.isEmpty(vos) || !formulaVersionIds.contains(version.getProductFormulaVersionId())){
            vo.setDisabled(true);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<ProductFormulaMaterialListVO> getModelMaterialList(Long versionId, Long procedureModelId) {
        List<ProductFormulaMaterialListVO> voList = this.getProductFormulaMaterialPullDownList(versionId);
        if (ObjectUtil.isNull(procedureModelId)){
            return voList;
        }
        List<Long> materialIdList = materialService.getByProcedureModelId(Collections.singletonList(procedureModelId));
        List<Long> idList = CollectionUtils.convertList(voList, ProductFormulaMaterialListVO::getId);
        List<Long> disabledId = CollectionUtils.filterList(materialIdList, item -> !idList.contains(item));
        if (CollUtil.isNotEmpty(disabledId)){
            List<ProductFormulaMaterial> list = productFormulaMaterialMapper.selectListByDisabledIds(disabledId);
            voList.addAll(list.stream().map(item->{
                ProductFormulaMaterialListVO vo = new ProductFormulaMaterialListVO();
                vo.setDisabled(true);
                vo.setId(item.getId());
                vo.setMaterialId(item.getMaterialId());
                vo.setMaterialMergeCode(item.getMaterialMergeCode());
                vo.setMaterialName(item.getMaterialName());
                vo.setUnitId(item.getUnitId());
                return vo;
            }).collect(Collectors.toList()));
        }
        return voList;
    }

    @Override
    public List<RequisitionPlanMaterialVO> getFormulaMaterialListByPlanId(Long productPlanId) {
        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        Long formulaVersionId = planMapper.selectPlanFormulaVersionId(productPlanId);
        if (formulaVersionId == null) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_VERSION_NOT_EXISTED);
        }
        ProductFormulaVersion formulaVersion = productFormulaVersionMapper.selectById(formulaVersionId);
        List<ProductFormulaMaterial> planMaterialList = productFormulaMaterialMapper.selectByVersionId(formulaVersionId);
        return planMaterialList.stream().map(material -> {
            RequisitionPlanMaterialVO vo =
                    ProductFormulaConverter.INSTANCE.convertToRequisitionPlanMaterialVO(material);
            vo.setTheoreticalQuantity(BusinessComponentStrategy.calculateQuantity(plan.getBatchQuantity(), formulaVersion.getBatchQuantity(), material));
            vo.setUnitName(unitCache.getGlobalUnitName(material.getUnitId()));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public ProductFormula getOneByVersionId(Long businessId) {
        ProductFormulaVersion productFormulaVersion = productFormulaVersionMapper.selectById(businessId);
        ProductFormula productFormula = productFormulaMapper.selectById(productFormulaVersion.getProductFormulaId());
        productFormula.setVersion(productFormulaVersion.getVersionNo());
        return productFormula;
    }

    @Override
    public List<ProductFormulaMaterialListVO> getFormulaMaterialListByProcessVersionId(ListProcessMaterialDTO dto) {
        ProcessVersion processVersion = processVersionMapper.selectById(dto.getProcessVersionId());
        if (processVersion == null) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_NOT_EXIST);
        }
        Long formulaVersionId = processVersion.getProductFormulaVersionId();
        List<ProductFormulaMaterial> materials = productFormulaMaterialMapper.selectByVersionId(formulaVersionId);
        if (dto.getCategoryType() != null) {
            materials = materials.stream()
                    .filter(item -> ObjectUtil.equal(dto.getCategoryType(), item.getMaterialType().getValue()))
                    .collect(Collectors.toList());
        }
        List<ProductFormulaMaterialListVO> result = ProductFormulaConverter.INSTANCE.convertToProductFormulaMaterialListVO(materials);
        result.forEach(e->e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId())));
        return result;
    }

    @Override
    public ProductFormulaMaterial selectById(Long id) {
        return productFormulaMaterialMapper.selectById(id);
    }

    @Override
    public ProductFormulaVersionDetailVO getProductFormulaVersionDetailByProcess(FormulaVersionDetailByProcessDTO dto) {
        ProcessVersion processVersion = processVersionMapper.selectByProcessIdAndVersion(dto.getProcessId(), dto.getProcessVersion());
        if (processVersion == null) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_NOT_EXIST);
        }
        return getProductFormulaVersionDetail(processVersion.getProductFormulaVersionId());
    }

    private CommonPage<ProductFormulaAuditPageVO> handleProductFormulaAuditPage(ProductFormulaAuditPageQueryDTO dto, PageQueryResp<List<TaskListResp>> listPageQueryResp) {
        List<TaskListResp> taskListResps = listPageQueryResp.getData();
        if (CollUtil.isEmpty(taskListResps)) {
            return CommonPage.convertPage(PageInfo.emptyPageInfo());
        }
        List<String> instanceIds = CollectionUtils.convertList(taskListResps, TaskListResp::getProcessInstanceId)
                .stream().distinct().collect(Collectors.toList());
        List<ProductFormulaVersionAuditVO> list = productFormulaVersionMapper.selectByProcessInstanceIds(instanceIds);
        Map<String, ProductFormulaVersionAuditVO> map = CollectionUtils.convertMap(list, ProductFormulaVersionAuditVO::getProcessInstanceId);
        List<ProductFormulaAuditPageVO> result = ProductMaterialCategoryConverter.INSTANCE.convertToAuditPageVO(taskListResps, map);
        return CommonPage.CommonPage(result, listPageQueryResp.getTotal(), dto.convertBasePage());
    }


    private static ProductFormulaVersion getInitialVersion(ProductFormulaSaveDTO dto, ProductFormula productFormula) {
        ProductFormulaVersion version = new ProductFormulaVersion();
        version.setDescription(dto.getDescription());
        version.setVersionNo(dto.getVersionNo());
        version.setEnable(false);
        version.setStatus(FormulaVersionStatusEnum.EDIT);
        version.setProductFormulaId(productFormula.getId());
        version.setBatchQuantity(dto.getBatchQuantity());
        version.setUnitId(dto.getUnitId());
        return version;
    }


}
