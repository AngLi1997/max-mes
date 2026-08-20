package com.bmos.mes.service.trace.material.service.impl;

import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.dto.query.ProcessRelationQueryDTO;
import com.bmos.mes.service.process.mapper.ProcedureStepMapper;
import com.bmos.mes.service.process.mapper.ProcessMapper;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.process.vo.ProcedureStepTraceVO;
import com.bmos.mes.service.process.vo.ProcessListItemVO;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialCategoryService;
import com.bmos.mes.service.trace.material.convert.MaterialTraceTemplateProcedureStepConverter;
import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplateCreateDTO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplateEditDTO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplateMaterialDTO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplatePageQuery;
import com.bmos.mes.service.trace.material.entity.MaterialTraceTemplateDO;
import com.bmos.mes.service.trace.material.entity.MaterialTraceTemplateMaterial;
import com.bmos.mes.service.trace.material.entity.MaterialTraceTemplateProcedureStepDO;
import com.bmos.mes.service.trace.material.enums.MaterialTraceType;
import com.bmos.mes.service.trace.material.mapper.IMaterialTraceTemplateMapper;
import com.bmos.mes.service.trace.material.mapper.IMaterialTraceTemplateProcedureStepMapper;
import com.bmos.mes.service.trace.material.service.IMaterialTraceTemplateService;
import com.bmos.mes.service.trace.material.vo.MaterialTraceTemplateDetailVO;
import com.bmos.mes.service.trace.material.vo.MaterialTraceTemplatePageVO;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 物料追溯service
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 18:20
 */
@Service
@Slf4j
public class MaterialTraceTemplateServiceImpl implements IMaterialTraceTemplateService {


    private static final String LOG_PREFIX = "[物料追溯]";

    @Resource
    private ProcedureStepMapper procedureStepMapper;

    @Resource
    private IMaterialTraceTemplateMapper materialTraceTemplateMapper;

    @Resource
    private IMaterialTraceTemplateProcedureStepMapper materialTraceTemplateProcedureStepMapper;

    @Resource
    private ProductMaterialCategoryService productMaterialCategoryService;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private ProcessService processService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTemplate(MaterialTraceTemplateCreateDTO dto) {
        log.info("{}创建模板:{}", LOG_PREFIX, dto);

        if (materialTraceTemplateMapper.existName(dto.getTemplateName())) {
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_NAME_EXIST);
        }

        MaterialTraceTemplateDO template = new MaterialTraceTemplateDO();
        template.setTemplateName(dto.getTemplateName());
        template.setProductId(dto.getProductId());
        template.setProcessId(dto.getProcessId());
        template.setEnabled(false);
        List<MaterialTraceTemplateMaterialDTO> materialDTOTree = dto.getMaterialDTOTree();
        List<MaterialTraceTemplateMaterial> materialTree = this.trans(materialDTOTree);
        template.setMaterialTree(materialTree);
        materialTraceTemplateMapper.insert(template);


        List<MaterialTraceTemplateMaterialDTO.ProcedureStepDTO> steps = materialDTOTree.stream()
                .flatMap(item -> item.getAllProcedureStepDTOList().stream())
                .collect(Collectors.toList());
        this.createSteps(steps, template);
    }

    @Override
    public CommonPage<MaterialTraceTemplatePageVO> queryPage(MaterialTraceTemplatePageQuery pageQuery) {
        if (pageQuery.getProductId() != null) {
            pageQuery.getMaterialIds().add(pageQuery.getProductId());
        } else {
            pageQuery.getMaterialIds().addAll(productMaterialCategoryService.getAllProductIds(pageQuery.getCategoryId()));
        }
        // 工艺权限
        List<Process> processes = processMapper.selectByProductIdsAndDeptIds(pageQuery.getMaterialIds(), platformApiAdaptor.deptIds());
        if (CollectionUtils.isAnyEmpty(pageQuery.getMaterialIds(), processes)) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        pageQuery.setProcessIds(CollectionUtils.convertList(processes, Process::getId));
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderBy());
        List<MaterialTraceTemplatePageVO> list = materialTraceTemplateMapper.queryPage(pageQuery);
        return CommonPage.convertPage(list);
    }

    @Nullable
    @Override
    public MaterialTraceTemplateDetailVO queryDetail(Long id) {
        if (id == null) {
            return null;
        }
        MaterialTraceTemplateDO materialTraceTemplateDO = materialTraceTemplateMapper.selectById(id);
        if (materialTraceTemplateDO == null) {
            return null;
        }
        MaterialTraceTemplateDetailVO result = new MaterialTraceTemplateDetailVO();
        result.setId(materialTraceTemplateDO.getId());
        result.setTemplateName(materialTraceTemplateDO.getTemplateName());
        Optional.ofNullable(materialTraceTemplateDO.getProductId())
                .map(pid -> productMaterialMapper.selectById(pid))
                .ifPresent(productMaterial -> {
                    result.setProductId(productMaterial.getId());
                    result.setProductName(productMaterial.getName());
                    result.setMergeCode(productMaterial.getMergeCode());
                });
        Optional.ofNullable(materialTraceTemplateDO.getProcessId())
                .map(pid -> processMapper.selectById(pid))
                .ifPresent(process -> {
                    result.setProcessId(process.getId());
                    result.setProcessName(process.getName());
                });
        result.setEnabled(materialTraceTemplateDO.getEnabled());
        result.setMaterialTree(this.renderTree(materialTraceTemplateDO.getMaterialTree(), null));
        List<MaterialTraceTemplateProcedureStepDO> steps = materialTraceTemplateProcedureStepMapper.selectByTemplateId(materialTraceTemplateDO.getId());
        Map<Long, List<MaterialTraceTemplateProcedureStepDO>> group = steps.stream()
                .collect(Collectors.groupingBy(MaterialTraceTemplateProcedureStepDO::getRelationId));
        List<MaterialTraceTemplateDetailVO.ProcedureStepData> list = new ArrayList<>();
        for (Map.Entry<Long, List<MaterialTraceTemplateProcedureStepDO>> entry : group.entrySet()) {
            MaterialTraceTemplateDetailVO.ProcedureStepData procedureStepData = new MaterialTraceTemplateDetailVO.ProcedureStepData();
            procedureStepData.setRelationId(entry.getKey());
            entry.getValue().forEach(item -> {
                MaterialTraceTemplateDetailVO.ProcedureStepVO procedureStepVO = MaterialTraceTemplateProcedureStepConverter.INSTANCE.convertToVO(item);
                if (item.getTraceType() == MaterialTraceType.OUTPUT) {
                    procedureStepData.getOutputStepList().add(procedureStepVO);
                } else if (item.getTraceType() == MaterialTraceType.CONSUME) {
                    procedureStepData.getConsumeStepList().add(procedureStepVO);
                }
            });
            list.add(procedureStepData);
        }
        result.setProcedureStepDataList(list);
        return result;
    }

    private List<MaterialTraceTemplateMaterial> renderTree(List<MaterialTraceTemplateMaterial> materialTree, Long parentId) {
        if (CollectionUtils.isAnyEmpty(materialTree)) {
            return new ArrayList<>();
        }
        for (MaterialTraceTemplateMaterial node : materialTree) {
            node.setParentId(parentId);
            if (!CollectionUtils.isAnyEmpty(node.getChildren())) {
                renderTree(node.getChildren(), node.getId());
            }
        }
        return materialTree;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableTemplate(Long id) {
        MaterialTraceTemplateDO materialTraceTemplateDO = materialTraceTemplateMapper.selectById(id);
        if (materialTraceTemplateDO == null) {
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_NOT_EXIST);
        }
        if (materialTraceTemplateDO.getEnabled()) {
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_ENABLED);
        }

        Long processId = materialTraceTemplateDO.getProcessId();
        MaterialTraceTemplateDO processEnabledTemplate = materialTraceTemplateMapper.selectEnabledTemplateByProcessId(processId);
        if (processEnabledTemplate != null && !Objects.equals(processEnabledTemplate.getId(), id)) {
            log.info("{}工艺存在启用的模板:{}", LOG_PREFIX, processEnabledTemplate);
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_PROCESS_ENABLED, processEnabledTemplate.getTemplateName());
        }
        materialTraceTemplateDO.setEnabled(true);
        materialTraceTemplateMapper.updateById(materialTraceTemplateDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableTemplate(Long id) {
        MaterialTraceTemplateDO materialTraceTemplateDO = materialTraceTemplateMapper.selectById(id);
        if (materialTraceTemplateDO == null) {
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_NOT_EXIST);
        }
        if (!materialTraceTemplateDO.getEnabled()) {
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_DISABLED);
        }
        materialTraceTemplateDO.setEnabled(false);
        materialTraceTemplateMapper.updateById(materialTraceTemplateDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editTemplate(MaterialTraceTemplateEditDTO dto) {
        log.info("{}编辑模板:{}", LOG_PREFIX, dto);
        MaterialTraceTemplateDO materialTraceTemplateDO = materialTraceTemplateMapper.selectById(dto.getId());
        if (materialTraceTemplateDO == null) {
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_NOT_EXIST);
        }
        if (materialTraceTemplateDO.getEnabled()) {
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_ENABLED);
        }

        if (!StringUtils.equals(dto.getTemplateName(), materialTraceTemplateDO.getTemplateName()) && materialTraceTemplateMapper.existName(dto.getTemplateName())) {
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_NAME_EXIST);
        }

        materialTraceTemplateDO.setTemplateName(dto.getTemplateName());
        materialTraceTemplateDO.setProcessId(dto.getProcessId());
        materialTraceTemplateDO.setProductId(dto.getProductId());
        List<MaterialTraceTemplateMaterialDTO> materialDTOTree = dto.getMaterialDTOTree();
        materialTraceTemplateDO.setMaterialTree(this.trans(materialDTOTree));
        materialTraceTemplateMapper.updateById(materialTraceTemplateDO);

        List<MaterialTraceTemplateMaterialDTO.ProcedureStepDTO> steps = materialDTOTree.stream()
                .flatMap(item -> item.getAllProcedureStepDTOList().stream())
                .collect(Collectors.toList());
        // 新增的
        List<MaterialTraceTemplateMaterialDTO.ProcedureStepDTO> addList = steps.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        log.info("{}物料追溯新增工步:{}", LOG_PREFIX, addList);
        this.createSteps(addList, materialTraceTemplateDO);
        // 修改的
        List<MaterialTraceTemplateMaterialDTO.ProcedureStepDTO> updateList = steps.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
        log.info("{}物料追溯更新工步:{}", LOG_PREFIX, updateList);
        this.updateSteps(updateList, materialTraceTemplateDO);
        // 删除的
        List<Long> stepRemoveIdList = dto.getStepRemoveIdList();
        log.info("{}物料追溯移除工步:{}", LOG_PREFIX, stepRemoveIdList);
        this.deleteSteps(stepRemoveIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(Long id) {
        log.info("{}删除模板:{}", LOG_PREFIX, id);
        MaterialTraceTemplateDO materialTraceTemplateDO = materialTraceTemplateMapper.selectById(id);
        if (materialTraceTemplateDO == null) {
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_NOT_EXIST);
        }
        if (materialTraceTemplateDO.getEnabled()) {
            throw new BmosException(MesResponseCode.MATERIAL_TRACE_TEMPLATE_ENABLED);
        }
        materialTraceTemplateMapper.deleteById(id);
        materialTraceTemplateProcedureStepMapper.deleteByTemplateId(id);
    }

    private void createSteps(List<MaterialTraceTemplateMaterialDTO.ProcedureStepDTO> steps, MaterialTraceTemplateDO template) {
        if (steps == null || steps.isEmpty()) {
            return;
        }
        List<Long> stepIds = CollectionUtils.convertList(steps, MaterialTraceTemplateMaterialDTO.ProcedureStepDTO::getProcedureStepId);
        List<ProcedureStepTraceVO> procedureSteps = procedureStepMapper.selectTraceInfoListByProcedureStepIds(stepIds);
        List<Long> processIds = CollectionUtils.convertList(procedureSteps, ProcedureStepTraceVO::getProcessId);
        Map<Long, ProcedureStepTraceVO> procedureStepMap = CollectionUtils.convertMap(procedureSteps, ProcedureStepTraceVO::getId, Function.identity());
        // 查询关联工艺
        List<Long> relationProcessIds = CollectionUtils.convertList(processService.getRecursionRelationProcessList(new ProcessRelationQueryDTO(template.getProcessId())), ProcessListItemVO::getId);
        relationProcessIds.add(template.getProcessId());

        Map<Long, String> processVersionMap = processMapper.selectBatchIds(relationProcessIds)
                .stream()
                .filter(item -> StringUtils.isNotBlank(item.getActiveVersion()))
                .collect(Collectors.toMap(Process::getId, Process::getActiveVersion, (k1, k2) -> k1));
        List<MaterialTraceTemplateProcedureStepDO> stepList = new ArrayList<>();
        for (MaterialTraceTemplateMaterialDTO.ProcedureStepDTO step : steps) {
            MaterialTraceTemplateProcedureStepDO stepDO = new MaterialTraceTemplateProcedureStepDO();
            ProcedureStepTraceVO procedureStep = procedureStepMap.get(step.getProcedureStepId());
            if (procedureStep == null) {
                continue;
            }
            if (!relationProcessIds.contains(procedureStep.getProcessId())) {
                this.exceptionMessage(step);
            }
            stepDO.setRelationId(step.getRelationId());
            stepDO.setTemplateId(template.getId());
            stepDO.setMaterialId(step.getMaterialId());
            stepDO.setProcessId(procedureStep.getProcessId());
            stepDO.setProcessName(procedureStep.getProcessName());
            stepDO.setProcessVersion(processVersionMap.get(procedureStep.getProcessId()));
            stepDO.setProcedureId(procedureStep.getProcedureId());
            stepDO.setProcedureName(procedureStep.getProcedureName());
            stepDO.setProcedureStepId(procedureStep.getId());
            stepDO.setProcedureStepName(procedureStep.getName());
            stepDO.setTraceType(MaterialTraceType.getByValue(step.getTraceType()));
            stepList.add(stepDO);
        }
        materialTraceTemplateProcedureStepMapper.insertBatch(stepList);
    }

    private void exceptionMessage(MaterialTraceTemplateMaterialDTO.ProcedureStepDTO step) {
        ProductMaterial productMaterial = productMaterialMapper.selectById(step.getMaterialId());
        if (productMaterial == null) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        throw new BmosException(MesResponseCode.MATERIAL_TRACE_PROCESS_MATERIAL_NO_RELATION,
                productMaterial.getMergeCode() + "-" + productMaterial.getName(),
                MaterialTraceType.getByValue(step.getTraceType()).getName()
        );
    }

    private void updateSteps(List<MaterialTraceTemplateMaterialDTO.ProcedureStepDTO> steps,  MaterialTraceTemplateDO template) {
        if (steps == null || steps.isEmpty()) {
            return;
        }
        // 参数map 索引 -> 关联工步
        Map<Long, MaterialTraceTemplateMaterialDTO.ProcedureStepDTO> dtoMap = CollectionUtils.convertMap(steps, MaterialTraceTemplateMaterialDTO.ProcedureStepDTO::getId, Function.identity());
        // 查询工步信息
        List<Long> stepIds = steps.stream()
                .map(MaterialTraceTemplateMaterialDTO.ProcedureStepDTO::getProcedureStepId).collect(Collectors.toList());
        List<ProcedureStepTraceVO> procedureSteps = procedureStepMapper.selectTraceInfoListByProcedureStepIds(stepIds);
        // 新的工步map 工步id -> 工步信息
        Map<Long, ProcedureStepTraceVO> procedureStepMap = CollectionUtils.convertMap(procedureSteps, ProcedureStepTraceVO::getId, Function.identity());

        // 要更新的关联工步id列表
        List<Long> idList = steps.stream()
                .map(MaterialTraceTemplateMaterialDTO.ProcedureStepDTO::getId).collect(Collectors.toList());
        // 要更新的关联工步列表
        List<MaterialTraceTemplateProcedureStepDO> stepList = materialTraceTemplateProcedureStepMapper.selectBatchIds(idList);

        // 查询关联工艺
        List<Long> relationProcessIds = CollectionUtils.convertList(processService.getRecursionRelationProcessList(new ProcessRelationQueryDTO(template.getProcessId())), ProcessListItemVO::getId);

        relationProcessIds.add(template.getProcessId());

        Map<Long, String> processVersionMap = processMapper.selectBatchIds(relationProcessIds)
                .stream()
                .filter(item -> StringUtils.isNotBlank(item.getActiveVersion()))
                .collect(Collectors.toMap(Process::getId, Process::getActiveVersion, (k1, k2) -> k1));

        for (MaterialTraceTemplateProcedureStepDO stepDO : stepList) {
            MaterialTraceTemplateMaterialDTO.ProcedureStepDTO procedureStepDTO = dtoMap.get(stepDO.getId());
            if (procedureStepDTO == null) {
                continue;
            }
            ProcedureStepTraceVO procedureStep = procedureStepMap.get(procedureStepDTO.getProcedureStepId());
            if (procedureStep == null) {
                continue;
            }
            if (!relationProcessIds.contains(procedureStep.getProcessId())) {
                this.exceptionMessage(procedureStepDTO);
            }
            stepDO.setProcessId(procedureStep.getProcessId());
            stepDO.setProcessName(procedureStep.getProcessName());
            stepDO.setProcessVersion(processVersionMap.get(procedureStep.getProcessId()));
            stepDO.setProcedureId(procedureStep.getProcedureId());
            stepDO.setProcedureName(procedureStep.getProcedureName());
            stepDO.setProcedureStepId(procedureStep.getId());
            stepDO.setProcedureStepName(procedureStep.getName());
            stepDO.setTraceType(MaterialTraceType.getByValue(procedureStepDTO.getTraceType()));
        }
        materialTraceTemplateProcedureStepMapper.updateBatch(stepList);
    }

    private void deleteSteps(List<Long> stepRemoveIdList) {
        if (stepRemoveIdList == null || stepRemoveIdList.isEmpty()) {
            return;
        }
        materialTraceTemplateProcedureStepMapper.deleteBatchIds(stepRemoveIdList);
    }

    private List<MaterialTraceTemplateMaterial> trans(List<MaterialTraceTemplateMaterialDTO> materialDTOTree) {
        if (CollectionUtils.isAnyEmpty(materialDTOTree)) {
            return new ArrayList<>();
        }
        List<MaterialTraceTemplateMaterial> result = new ArrayList<>();
        for (MaterialTraceTemplateMaterialDTO materialDTO : materialDTOTree) {
            MaterialTraceTemplateMaterial materialDO = new MaterialTraceTemplateMaterial();
            materialDO.setId(materialDTO.getId());
            materialDO.setMaterialId(materialDTO.getMaterialId());
            materialDO.setMaterialType(materialDTO.getMaterialType());
            materialDO.setMaterialName(materialDTO.getMaterialName());
            materialDO.setMergeCode(materialDTO.getMergeCode());
            materialDO.setShowPercentYield(materialDTO.getShowPercentYield());
            materialDO.setPercentYieldRange(materialDTO.getPercentYieldRange());
            materialDO.setCalcFlag(materialDTO.getCalcFlag());
            List<MaterialTraceTemplateMaterialDTO> children = materialDTO.getChildren();
            List<MaterialTraceTemplateMaterial> childList = trans(children);
            materialDO.setChildren(childList);
            result.add(materialDO);
        }
        return result;
    }
}
