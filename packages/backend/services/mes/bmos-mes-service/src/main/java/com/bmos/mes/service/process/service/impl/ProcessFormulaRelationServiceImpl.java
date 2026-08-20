package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.process.dto.ProcessStepQueryDTO;
import com.bmos.mes.service.process.mapper.*;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.service.ProcessFormulaRelationService;
import com.bmos.mes.service.process.vo.ProcessStepVO;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class ProcessFormulaRelationServiceImpl implements ProcessFormulaRelationService {

    @Resource
    private ProductFormulaMaterialMapper formulaMaterialMapper;

    @Resource
    private ProcessVersionMapper processVersionMapper;

    @Resource
    private ProcedureModelMapper procedureModelMapper;

    @Resource
    private ProcedureModelMaterialMapper procedureModelMaterialMapper;

    @Resource
    private ProcedureStepConfigMapper procedureStepConfigMapper;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String formulaMaterialIdField = "formulaMaterialId";
    private final String formulaMaterialIdsField = "formulaMaterialIds";

    @Override
    public void replaceFormulaMaterial(Long processVersionId, Long oldFormulaVersionId) {
        // 构建替换需要的信息
        ReplaceContext context = buildContext(processVersionId);
        // 替换工序绑定的配方物料
        replaceProcedureFormulaMaterial(context);
        // 替换组件配置的配方物料
        replaceStepConfigFormulaMaterial(context);
    }

    private ReplaceContext buildContext(Long processVersionId) {
        ProcessVersion processVersion = processVersionMapper.selectById(processVersionId);
        Map<Long, Long> newMap =
                CollectionUtils.convertMap(formulaMaterialMapper.selectByVersionId(processVersion.getProductFormulaVersionId()),
                        ProductFormulaMaterial::getMaterialId, ProductFormulaMaterial::getId);
        // 查询有配方物料配置的数据
        List<ProcedureStepConfig> stepConfigs =
                procedureStepConfigMapper.selectList(new LambdaQueryWrapperX<ProcedureStepConfig>()
                .eq(ProcedureStepConfig::getProcessId, processVersion.getProcessId())
                .eq(ProcedureStepConfig::getVersion, processVersion.getVersion())
                .like(ProcedureStepConfig::getConfigInfo, formulaMaterialIdField));
        ReplaceContext context = ReplaceContext.builder()
                .processVersion(processVersion)
                .configs(stepConfigs)
                .materialIdMap(newMap)
                .procedureModelMaterialList(procedureModelMaterialMapper.selectByProcessVersionId(processVersionId))
                .build();
        Map<Long, Long> oldMap = getOldMap(context);
        context.setOldMap(oldMap);
        return context;
    }

    /**
     * 获取配置中的配方物料map
     *
     * @param context
     * @return
     */
    private Map<Long, Long> getOldMap(ReplaceContext context) {
        Set<Long> formulaMaterialIds =
                new HashSet<>(CollectionUtils.convertSet(context.getProcedureModelMaterialList(),
                        ProcedureModelMaterial::getProductFormulaMaterialId));
        for (ProcedureStepConfig stepConfig : context.getConfigs()) {
            String configInfo = stepConfig.getConfigInfo();
            JSONObject jsonObject = JSONUtil.parseObj(configInfo);
            // 对单个配方物料绑定处理
            Long formulaMaterialId = jsonObject.get(formulaMaterialIdField, Long.class);
            formulaMaterialIds.add(formulaMaterialId);
            JSONArray jsonArray = jsonObject.getJSONArray(formulaMaterialIdsField);
            // 对多个配方物料绑定处理
            if (CollUtil.isNotEmpty(jsonArray)) {
                List<Long> list = jsonArray.toList(Long.class);
                formulaMaterialIds.addAll(list);
            }
        }
        formulaMaterialIds.remove(null);
        List<ProductFormulaMaterial> materials = CollUtil.isEmpty(formulaMaterialIds) ? new ArrayList<>() :
                formulaMaterialMapper.selectBatchIds(formulaMaterialIds);
        return CollectionUtils.convertMap(materials, ProductFormulaMaterial::getId,
                ProductFormulaMaterial::getMaterialId);
    }

    @Override
    public void validateFormulaMaterialMatch(Long id) {
        ProcessVersion processVersion = processVersionMapper.selectById(id);
        List<ProductFormulaMaterial> materials =
                formulaMaterialMapper.selectByVersionId(processVersion.getProductFormulaVersionId());
        Set<Long> formulaMaterials = CollectionUtils.convertSet(materials, ProductFormulaMaterial::getId);
        // 查询工序模型列表
        validateProcedures(id, formulaMaterials);
        validateStepConfig(processVersion, formulaMaterials);
    }

    private void validateStepConfig(ProcessVersion processVersion, Set<Long> formulaMaterials) {
        // 查询有配方物料配置的数据
        List<ProcedureStepConfig> stepConfigs =
                procedureStepConfigMapper.selectList(new LambdaQueryWrapperX<ProcedureStepConfig>()
                .eq(ProcedureStepConfig::getProcessId, processVersion.getProcessId())
                .eq(ProcedureStepConfig::getVersion, processVersion.getVersion())
                .like(ProcedureStepConfig::getConfigInfo, formulaMaterialIdField));
        if (CollUtil.isEmpty(stepConfigs)) {
            return;
        }
        List<ProcessStepVO> processStepVOS =
                procedureStepModelMapper.selectListByProcess(ProcessStepQueryDTO.builder().processId(processVersion.getProcessId()).processVersion(processVersion.getVersion()).build());
        Set<Long> stepIds = CollectionUtils.convertSet(processStepVOS, ProcessStepVO::getProcedureStepId);
        for (ProcedureStepConfig stepConfig : stepConfigs) {
            String configInfo = stepConfig.getConfigInfo();
            JSONObject jsonObject = JSONUtil.parseObj(configInfo);
            // 对单个配方物料绑定处理
            Long formulaMaterialId = jsonObject.get(formulaMaterialIdField, Long.class);
            if (formulaMaterialId != null && !formulaMaterials.contains(formulaMaterialId)) {
                thrStepException(stepConfig, formulaMaterialId, stepIds);
            }
            JSONArray jsonArray = jsonObject.getJSONArray(formulaMaterialIdsField);
            // 对多个配方物料绑定处理
            if (CollUtil.isNotEmpty(jsonArray)) {
                List<Long> list = jsonArray.toList(Long.class);
                for (Long oldId : list) {
                    if (formulaMaterials.contains(oldId)) {
                        continue;
                    }
                    thrStepException(stepConfig, oldId, stepIds);
                }
            }
        }
    }

    private void thrStepException(ProcedureStepConfig stepConfig, Long id, Set<Long> stepIds) {
        if (!stepIds.contains(stepConfig.getProcedureStepId())) {
            return;
        }
        ProcedureStepModel stepModel = procedureStepModelMapper.selectByStepId(stepConfig.getProcessId(),
                stepConfig.getVersion(), stepConfig.getProcedureStepId());
        if (stepModel == null) {
            return;
        }
        ProcedureModel procedureModel = procedureModelMapper.selectById(stepModel.getProcedureModelId());
        ProductFormulaMaterial formulaMaterial = formulaMaterialMapper.selectById(id);
        throw new BmosException(MesResponseCode.PROCEDURE_STEP_CONFIG_MATERIAL_NOT_MATCH, procedureModel.getName(),
                stepModel.getName(), formulaMaterial.getMaterialName());
    }

    private void validateProcedures(Long id, Set<Long> formulaMaterials) {
        List<ProcedureModel> procedureModels = procedureModelMapper.selectByProcessVersion(id);
        if (CollUtil.isEmpty(procedureModels)) {
            return;
        }
        Set<Long> procedureModelIds = CollectionUtils.convertSet(procedureModels, ProcedureModel::getId);
        List<ProcedureModelMaterial> materials =
                procedureModelMaterialMapper.selectByProcedureModelIds(procedureModelIds);
        if (CollUtil.isEmpty(materials)) {
            return;
        }
        for (ProcedureModelMaterial material : materials) {
            if (formulaMaterials.contains(material.getProductFormulaMaterialId())) {
                continue;
            }
            thrException(material.getProcedureModelId(), material.getProductFormulaMaterialId());
        }

    }

    private void thrException(Long procedureModelId, Long productFormulaMaterialId) {
        ProcedureModel procedureModel = procedureModelMapper.selectById(procedureModelId);
        ProductFormulaMaterial formulaMaterial = formulaMaterialMapper.selectById(productFormulaMaterialId);
        throw new BmosException(MesResponseCode.PROCEDURE_FORMULA_MATERIAL_NOT_MATCH, procedureModel.getName(),
                formulaMaterial.getMaterialName());
    }

    /**
     * 处理工步记录配置组件绑定的配方物料
     */
    private void replaceStepConfigFormulaMaterial(ReplaceContext context) {
        // 查询有配方物料配置的数据
        List<ProcedureStepConfig> stepConfigs = context.getConfigs();
        if (CollUtil.isEmpty(stepConfigs)) {
            return;
        }
        for (ProcedureStepConfig stepConfig : stepConfigs) {
            try {
                replaceStepConfigFormulaMaterial(stepConfig, context);
            } catch (JsonProcessingException e) {
                log.error("工步记录配置组件绑定的配方物料替换失败", e);
            }
        }
        procedureStepConfigMapper.updateBatch(stepConfigs);
    }

    @Data
    private static class JsonPathNode {
        private String path;
        private Queue<String> pathList;

        public JsonPathNode(String path) {
            this.path = path;
            this.pathList = new LinkedList<>(StrUtil.split(path, "."));
        }
    }

    private void replaceStepConfigFormulaMaterial(ProcedureStepConfig stepConfig, ReplaceContext context) throws JsonProcessingException {
        String configInfo = stepConfig.getConfigInfo();
        JsonNode jsonNode = objectMapper.readTree(configInfo);
        if (jsonNode == null) {
            return;
        }
        List<JsonPathNode> jsonPathNodes = new ArrayList<JsonPathNode>() {{
            add(new JsonPathNode("formulaMaterialId"));
            add(new JsonPathNode("formulaMaterialIds"));
            add(new JsonPathNode("materialList.formulaMaterialId"));
        }};
        for (JsonPathNode jsonPathNode : jsonPathNodes) {
            recHandleJsonNode(jsonNode, jsonPathNode.pathList, context);
        }
        stepConfig.setConfigInfo(JsonUtils.toJsonString(jsonNode));
    }

    private void recHandleJsonNode(JsonNode currentNode, Queue<String> nodeConfig, ReplaceContext context) {
        Map<Long, Long> oldMap = context.getOldMap();
        Map<Long, Long> newMap = context.getMaterialIdMap();
        if (currentNode == null) {
            return;
        }
        String fieldName = nodeConfig.poll();
        JsonNode nextNode = currentNode.get(fieldName);
        if (!nodeConfig.isEmpty() || nextNode == null) {
            if (nextNode != null && nextNode.isArray()) {
                nextNode.forEach(node -> {
                    recHandleJsonNode(node, new LinkedList<>(nodeConfig), context);
                });
            } else {
                recHandleJsonNode(nextNode, new LinkedList<>(nodeConfig), context);
            }
            return;
        }
        if (nextNode.isValueNode()) {
            ObjectNode objectNode = (ObjectNode) currentNode;
            if (currentNode.get(fieldName) == null) {
                return;
            }
            objectNode.set(fieldName, new TextNode(getNewFormulaMaterialId(objectNode.get(fieldName).asLong(), oldMap
                    , newMap)));
        } else {
            ArrayNode arrayNode = (ArrayNode) nextNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                arrayNode.set(i, getNewFormulaMaterialId(arrayNode.get(i).asLong(), oldMap, newMap));
            }
        }

    }

    private String getNewFormulaMaterialId(Long oldId, Map<Long, Long> oldMap, Map<Long, Long> newMap) {
        if (oldMap.get(oldId) != null && newMap.get(oldMap.get(oldId)) != null) {
            return String.valueOf(newMap.get(oldMap.get(oldId)));
        }
        return oldId.toString();
    }


    /**
     * 处理工序绑定的配方物料
     */
    private void replaceProcedureFormulaMaterial(ReplaceContext context) {
        // 查询工序模型物料列表
        if (CollUtil.isEmpty(context.getProcedureModelMaterialList())) {
            return;
        }
        // 删除旧绑定关系
        procedureModelMaterialMapper.deleteByProcedureModelIds(CollectionUtils.convertList(context.getProcedureModelMaterialList(), ProcedureModelMaterial::getProcedureModelId));
        for (ProcedureModelMaterial material : context.getProcedureModelMaterialList()) {
            Long materialId = context.getOldMap().get(material.getProductFormulaMaterialId());
            Long formulaMaterialId = context.getMaterialIdMap().get(materialId);
            if (formulaMaterialId != null) {
                material.setProductFormulaMaterialId(formulaMaterialId);
            }
        }
        procedureModelMaterialMapper.insertBatch(context.getProcedureModelMaterialList());

    }

    @Builder
    @Getter
    @Setter
    public static class ReplaceContext {

        /**
         * 工艺版本信息
         */
        private ProcessVersion processVersion;

        /**
         * 配置列表
         */
        private List<ProcedureStepConfig> configs;

        /**
         * 新配方<物料id,配方物料id>map
         */
        private Map<Long, Long> materialIdMap;

        /**
         * 工序配方物料列表
         */
        private List<ProcedureModelMaterial> procedureModelMaterialList;

        /**
         * 配置中 <旧配方物料id,物料id> map
         */
        private Map<Long, Long> oldMap;
    }

}
