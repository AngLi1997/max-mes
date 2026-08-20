package com.bmos.mes.service.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.utils.Graph;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.aspect.OperationHistoryContext;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.record.convert.RecordComponentConvert;
import com.bmos.mes.service.record.dto.ComponentDetailDTO;
import com.bmos.mes.service.record.dto.CopyVersionDTO;
import com.bmos.mes.service.record.dto.FormulaParameterDTO;
import com.bmos.mes.service.record.dto.SaveFormulaDTO;
import com.bmos.mes.service.record.mapper.BatchRecordComponentDetailMapper;
import com.bmos.mes.service.record.mapper.BatchRecordComponentMapper;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.model.BatchRecordComponentBO;
import com.bmos.mes.service.record.model.BatchRecordComponentDetail;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.redis.RecordRedisKeyDefine;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.service.BatchRecordItemService;
import com.bmos.mes.service.record.vo.ComponentDetailVO;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.record.vo.ParseComponentVO;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatchRecordComponentServiceImpl implements BatchRecordComponentService {

    @Autowired
    private BatchRecordComponentMapper componentMapper;

    @Autowired
    private BatchRecordItemService itemService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private BatchRecordComponentDetailMapper detailMapper;

    @Override
    public List<BatchRecordComponent> selectByVersionId(Long versionId) {
        return componentMapper.selectByVersionId(versionId);
    }

    @Override
    public List<BatchRecordComponent> selectComponentList(Long itemId, Long newItemId) {
        List<BatchRecordComponent> componentList = componentMapper.selectComponentList(itemId, null);
        componentList.forEach(component -> {
            component.setId(CustomIdGenerator.nextId());
            component.setRecordItemId(newItemId);
        });
        return componentList;
    }

    @Override
    public List<BatchRecordComponent> getByIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return new ArrayList<>();
        }
        return componentMapper.selectByIdList(idList);
    }

    @Override
    public void saveOrUpdateComponent(List<BatchRecordComponent> componentList) {
        if (CollUtil.isEmpty(componentList)) {
            return;
        }
        // 在记录配置中 非公式编辑下
        // 组件会产生更新的只有used字段以及组件详情中的componentDetail字段
        // 需要筛选出产生变化的组件进行更新 否则数据量大时很慢
        List<Long> ids = CollectionUtils.convertList(componentList, BatchRecordComponent::getId);
        List<BatchRecordComponent> batchRecordComponents = getExistedComponentByIdList(ids);
        Map<Long, BatchRecordComponent> componentMap = CollectionUtils.convertMap(batchRecordComponents,
                BatchRecordComponent::getId);
        List<BatchRecordComponent> updateComponent = new ArrayList<>();
        List<BatchRecordComponent> insertComponent = new ArrayList<>();
        List<BatchRecordComponentDetail> updateDetail = new ArrayList<>();
        List<BatchRecordComponentDetail> insertDetail = new ArrayList<>();
        List<BatchRecordComponentDetail> details = getExistedDetailByIdList(ids);
        Map<Long, BatchRecordComponentDetail> detailMap = CollectionUtils.convertMap(details,
                BatchRecordComponentDetail::getId);
        componentList.forEach(e -> {
            handleComponentList(e, componentMap, insertComponent, updateComponent);
            handleDetailList(e, detailMap, insertDetail, updateDetail);
        });
        componentMapper.insertBatch(insertComponent);
        detailMapper.insertBatch(insertDetail);
        if (CollUtil.isNotEmpty(updateComponent)) {
            componentMapper.updateBatch(updateComponent);
        }
        if (CollUtil.isNotEmpty(updateDetail)) {
            detailMapper.updateBatch(updateDetail);
        }
    }

    private List<BatchRecordComponent> getExistedComponentByIdList(List<Long> ids) {
        List<BatchRecordComponent> batchRecordComponents =
                componentMapper.selectList(new LambdaQueryWrapperX<BatchRecordComponent>()
                .in(BatchRecordComponent::getId, ids)
                .select(BatchRecordComponent::getId, BatchRecordComponent::getUsed));
        return batchRecordComponents;
    }

    private List<BatchRecordComponentDetail> getExistedDetailByIdList(List<Long> ids) {
        List<BatchRecordComponentDetail> details =
                detailMapper.selectList(new LambdaQueryWrapperX<BatchRecordComponentDetail>()
                .in(BatchRecordComponentDetail::getId, ids)
                .select(BatchRecordComponentDetail::getId, BatchRecordComponentDetail::getComponentDetail));
        return details;
    }

    private static void handleComponentList(BatchRecordComponent e, Map<Long, BatchRecordComponent> componentMap,
                                            List<BatchRecordComponent> insertComponent, List<BatchRecordComponent> updateComponent) {
        if (componentMap.get(e.getId()) == null) {
            insertComponent.add(e);
        } else if (!Objects.equals(componentMap.get(e.getId()).getUsed(), e.getUsed())) {
            updateComponent.add(e);
        }
    }

    private static void handleDetailList(BatchRecordComponent e, Map<Long, BatchRecordComponentDetail> detailMap,
                                         List<BatchRecordComponentDetail> insertDetail, List<BatchRecordComponentDetail> updateDetail) {
        BatchRecordComponentDetail detail = RecordComponentConvert.INSTANCE.convertToComponentDetail(e);
        if (detailMap.get(e.getId()) == null) {
            insertDetail.add(detail);
        } else if (!StrUtil.equals(detailMap.get(e.getId()).getComponentDetail(), detail.getComponentDetail())) {
            updateDetail.add(detail);
        }
    }

    @Override
    public void deleteCompoenent(Long itemId, Long recordVersionId) {
        List<BatchRecordComponent> batchRecordComponents = componentMapper.selectComponentList(itemId, recordVersionId);
        if (CollUtil.isEmpty(batchRecordComponents)) {
            return;
        }
        componentMapper.deleteCompoenent(itemId, recordVersionId);
        detailMapper.deleteBatchIds(CollectionUtils.convertList(batchRecordComponents, BatchRecordComponent::getId));
    }

    @Override
    public ParseComponentVO listComponent(Long itemId, Long recordVersionId) {
        BatchRecordItem item = itemService.queryByItemIdAndVersionId(itemId, recordVersionId);
        ParseComponentVO vo = RecordComponentConvert.INSTANCE.convertToVo(item);
        if (ObjectUtil.isNotEmpty(vo)) {
            List<ComponentListVO> componentListVOS =
                    RecordComponentConvert.INSTANCE.convertToVoList(componentMapper.selectComponentList(itemId,
                            item.getRecordVersionId()));
            vo.setComponentList(TreeUtil.buildTree(componentListVOS, false));
            Map<String, Integer> sortMap = BusinessComponentTypeEnum.getSortMap();
            vo.getComponentList().forEach(e->{
                if(CollUtil.isNotEmpty(e.getChildren())) {
                    e.getChildren().sort(Comparator.comparing(o -> sortMap.getOrDefault(o.getComponentType(), Integer.MAX_VALUE)));
                }
            });
        }
        return vo;
    }

    @Override
    @DistributedLock(key = RecordConstant.REDISSON_KEY)
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveFormula(SaveFormulaDTO dto) {
        BatchRecordComponent batchRecordComponent = componentMapper.selectById(dto.getId());
        if (batchRecordComponent == null) {
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        // 从数据库读取历史的公式列表
        Graph<Long> graph = getGraphFromDatabase(dto.getRecordVersionId());
        //删除图表中本次新增或者修改的公式
        graph.delete(batchRecordComponent.getFieldId());
        // 添加新的公式到图标中
        List<Long> filedIdList = CollectionUtils.convertList(dto.getFormulaDetailList(),
                FormulaParameterDTO::getFieldId);
        graph.addWithValidateCycle(batchRecordComponent.getFieldId(), filedIdList);
        dto.setFormulaField(JsonUtils.toJsonString(dto.getFormulaDetailList()));
        dto.setIsResult(RecordConstant.ONE);
        BatchRecordComponent componentList = RecordComponentConvert.INSTANCE.converToFormulaList(dto);
        BatchRecordComponentDetail detail = RecordComponentConvert.INSTANCE.convertToComponentDetail(componentList);
        detailMapper.saveOrUpdate(detail);
        //当公式类型为时间差公式时无需清空修约字段值
        if (ObjectUtils.isNull(componentList.getFormulaPrecision()) && !StrUtil.equals(componentList.getFormulaType(),"3")){
            componentList.setRoundCode(null);
        }
        return componentMapper.saveOrUpdateFormula(componentList);
    }

    private Graph<Long> getGraphFromDatabase(Long recordVersionId) {
        Graph<Long> graph = new Graph<>();
        List<BatchRecordComponent> list = componentMapper.selectGraphList(recordVersionId);
        Map<Long, BatchRecordComponent> map = CollectionUtils.convertMap(list, BatchRecordComponent::getFieldId);
        map.forEach((fileId, value) -> {
            List<Long> fileIdList = JsonUtils.parseArray(value.getFormulaField(), FormulaParameterDTO.class)
                    .stream().map(FormulaParameterDTO::getFieldId).collect(Collectors.toList());
            graph.add(fileId, fileIdList);
        });
        return graph;
    }

    @Override
    public Graph<Long> getGraph(Long recordVersionId) {
        String redisKey = RecordRedisKeyDefine.GRAPH.formatKey(recordVersionId.toString());
        String graphStr = stringRedisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isNotEmpty(graphStr)) {
            return JsonUtils.parseObjectWithTypeReference(graphStr, new TypeReference<Graph<Long>>() {
            });
        }
        Graph<Long> graphFromDatabase = getGraphFromDatabase(recordVersionId);
        stringRedisTemplate.opsForValue().set(redisKey, graphFromDatabase.serialize());
        return graphFromDatabase;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.FORMULA_CLEAR, businessId = "#getRecordVersionId")
    public Boolean deleteFormula(Long componentId) {
        BatchRecordComponent component = componentMapper.selectById(componentId);
        OperationHistoryContext.putVariable(component, BatchRecordComponent::getRecordVersionId);
        componentMapper.deleteFormula(componentId);
        detailMapper.deleteFormula(componentId);
        return Boolean.TRUE;
    }

    @Override
    public List<BatchRecordComponent> selectByRecordVersionIdAndFields(Long recordVersionId, Set<Long> fields, Boolean isResult) {
        return componentMapper.selectByRecordVersionIdAndFields(recordVersionId, fields, isResult);
    }

    @Override
    public void deleteByIdList(List<Long> idInDb) {
        if (CollUtil.isEmpty(idInDb)) {
            return;
        }
        componentMapper.deleteBatchIds(idInDb);
        // 删除组件详细信息
        detailMapper.deleteBatchIds(idInDb);
    }

    @Override
    public List<BatchRecordComponent> selectByVersionAndItem(Long recordVersionId, Long recordItemId) {
        return componentMapper.selectComponentList(recordItemId, recordVersionId);
    }

    @Override
    public ComponentListVO selectUsedComponentDetail(Long recordVersionId, Long recordItemId, Long componentId) {
        List<BatchRecordComponent> components = selectByVersionAndItem(recordVersionId, recordItemId);
        List<ComponentListVO> componentListVOS = RecordComponentConvert.INSTANCE.convertToVoList(components);
        List<ComponentListVO> tree = TreeUtil.buildTree(componentListVOS, false);
        List<ComponentListVO> res = new ArrayList<>();
        this.findTargetParentComponent(res, componentId, tree);
        for (ComponentListVO componentListVO : res) {
            if (!ObjectUtil.equals(componentListVO.getId(), componentId)){
                componentListVO.setChildren(null);
            }
        }
        if (CollectionUtil.isEmpty(res)){
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        return  TreeUtil.buildTree(res, false).get(0);
    }


    @Override
    public ComponentListVO selectParentComponent(Long componentId) {
        BatchRecordComponent component = componentMapper.selectById(componentId);
        if (ObjectUtil.isEmpty(component) || ObjectUtil.isEmpty(component.getParentId())){
            return null;
        }
        return RecordComponentConvert.INSTANCE.convertToComponentVo(componentMapper.selectById(component.getParentId()));
    }

    @Override
    public void refreshGraph(Long recordVersionId) {
        String redisKey = RecordRedisKeyDefine.GRAPH.formatKey(recordVersionId.toString());
        Graph<Long> graphFromDatabase = getGraphFromDatabase(recordVersionId);
        stringRedisTemplate.opsForValue().set(redisKey, graphFromDatabase.serialize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean copyComponent(CopyVersionDTO dto, List<Long> recordItemIdList,Long versionId) {
        List<BatchRecordComponent> batchRecordComponents = componentMapper.selectByVersionId(dto.getVersionOldId());
        if (CollUtil.isEmpty(batchRecordComponents)) {
            return true;
        }
        List<BatchRecordComponentBO> bos = RecordComponentConvert.INSTANCE.convertToBO(batchRecordComponents);
        List<BatchRecordComponentBO> boTree = TreeUtil.buildTree(bos, false);
        List<BatchRecordComponentBO> result = new ArrayList<>();
        this.recHandleComponentId(boTree, 0L, result);
        Map<Long, List<BatchRecordComponent>> componentMap = CollectionUtils.convertMultiMap(RecordComponentConvert.INSTANCE.convertToComponent(result),
                BatchRecordComponent::getRecordItemId);
        List<BatchRecordComponent> componentsList = new ArrayList<>();
        recordItemIdList.forEach(item -> {
            List<BatchRecordComponent> components = componentMap.get(item);
            if (ObjectUtil.isNotEmpty(components)) {
                components.forEach(component -> {
                    component.setRecordVersionId(versionId);
                    component.setRecordId(dto.getRecordId());
                    component.setRecordVersion(dto.getVersion());
                });
                componentsList.addAll(components);
            }
        });
        if (CollUtil.isEmpty(componentsList)){
            return true;
        }
        componentMapper.insertBatch(componentsList);
        List<BatchRecordComponentDetail> detailsList = RecordComponentConvert.INSTANCE.convertToComponentDetail(componentsList);
        List<BatchRecordComponentDetail> filter =
                detailsList.stream().filter(e -> {
                    return !StrUtil.isAllEmpty(e.getComponentDetail(), e.getFormulaField()) ||
                            e.getFormulaConfig() != null;
                }).collect(Collectors.toList());
        detailMapper.insertBatch(filter);
        return true;

    }

    private void recHandleComponentId(List<BatchRecordComponentBO> boTree, Long parentId, List<BatchRecordComponentBO> result) {
        for (BatchRecordComponentBO component : boTree) {
            component.setId(CustomIdGenerator.nextId());
            component.setParentId(parentId);
            result.add(component);
            if (CollUtil.isNotEmpty(component.getChildren())) {
                recHandleComponentId(component.getChildren(), component.getId(), result);
            }
        }
    }

    @Override
    public List<ComponentDetailVO> selectByComponentDetailDTOS(List<ComponentDetailDTO> componentDetailDTOS) {
//        List<ComponentDetailVO> componentDetailVOS = new ArrayList<>();
//        if (CollUtil.isEmpty(componentDetailDTOS)){
//            return componentDetailVOS;
//        }
//        if (componentDetailDTOS.size() > 20){
//            for (int i = 0; i < componentDetailDTOS.size(); i += 20) {
//                int end = i + 20;
//                if (end > componentDetailDTOS.size()){
//                    end = componentDetailDTOS.size();
//                }
//                List<ComponentDetailDTO> subList = componentDetailDTOS.subList(i, end);
//                List<ComponentDetailVO> componentDetailVOS1 = componentMapper.selectByComponentDetailDTOS(subList);
//                componentDetailVOS.addAll(componentDetailVOS1);
//            }
//        }
//        return componentDetailVOS;
        return componentMapper.selectByComponentDetailDTOS(componentDetailDTOS);
    }

    @Override
    public Long getByFieldId(Long fieldId) {
        return componentMapper.getByFieldId(fieldId);
    }

    @Override
    public List<BatchRecordComponent> getByFieldIdList(List<Long> fieldIdList) {
        if (CollUtil.isEmpty(fieldIdList)) {
            return new ArrayList<>();
        }
        return componentMapper.selectByFieldIdList(fieldIdList);
    }

    @Override
    public BatchRecordComponent getById(Long componentId) {
        return componentMapper.selectById(componentId);
    }

    /**
     * @param recordVersionId 记录版本id
     * @param recordItemId    记录项id
     * @return 该记录项表单下需要自动填充的业务组件
     */
    @Override
    public List<ComponentListVO> selectAutoFillComponentTree(Long recordVersionId, Long recordItemId) {
        List<BatchRecordComponent> components = selectByVersionAndItem(recordVersionId, recordItemId);
        List<ComponentListVO> componentListVOS = RecordComponentConvert.INSTANCE.convertToVoList(components);
        List<ComponentListVO> tree = TreeUtil.buildTree(componentListVOS, false);
        return tree.stream().filter(e -> BusinessComponentTypeEnum.BUSINESS_PRODUCT_INFO.getValue().equals(e.getComponentType())
                || BusinessComponentTypeEnum.BUSINESS_FORMULA_INFO.getValue().equals(e.getComponentType())).collect(Collectors.toList());
    }

    @Override
    public List<BatchRecordComponent> selectByRecordVersionIdsAndFields(Collection<Long> longs, Collection<Long> fieldIdList) {
        if(CollUtil.isEmpty(longs) || CollUtil.isEmpty(fieldIdList)){
            return new ArrayList<>();
        }
        return componentMapper.selectByRecordVersionIdListAndFields(longs, fieldIdList);
    }

    @Override
    public List<BatchRecordComponent> getProcedureStepListRequiredConfigList(List<ProcedureStepModel> procedureStepModelList) {
        if(CollUtil.isEmpty(procedureStepModelList)){
            return new ArrayList<>();
        }
        List<Long> recordItemIdList = CollectionUtils.convertList(procedureStepModelList, ProcedureStepModel::getRecordItemId);
        List<Long> recordVersionIdList = CollectionUtils.convertList(procedureStepModelList, ProcedureStepModel::getRecordVersionId);
        List<String> requiredConfigComponentTypes = Arrays.stream(BusinessComponentTypeEnum.values())
                .filter(BusinessComponentTypeEnum::isConfigRequired)
                .map(BusinessComponentTypeEnum::getValue)
                .collect(Collectors.toList());
        if(CollUtil.isEmpty(requiredConfigComponentTypes)){
            return new ArrayList<>();
        }
        return componentMapper.selectByRecordListAndTypeList(recordItemIdList, recordVersionIdList, requiredConfigComponentTypes);
    }

    @Override
    public List<BatchRecordComponent> getProcedureStepListConfigList(List<ProcedureStepModel> procedureStepModelList) {
        if(CollUtil.isEmpty(procedureStepModelList)){
            return new ArrayList<>();
        }
        List<Long> recordItemIdList = CollectionUtils.convertList(procedureStepModelList, ProcedureStepModel::getRecordItemId);
        List<Long> recordVersionIdList = CollectionUtils.convertList(procedureStepModelList, ProcedureStepModel::getRecordVersionId);
        List<String> requiredConfigComponentTypes = Arrays.stream(BusinessComponentTypeEnum.values())
                .map(BusinessComponentTypeEnum::getValue)
                .collect(Collectors.toList());
        if(CollUtil.isEmpty(requiredConfigComponentTypes)){
            return new ArrayList<>();
        }
        return componentMapper.selectByRecordListAndTypeList(recordItemIdList, recordVersionIdList, requiredConfigComponentTypes);
    }

    /**
     * 寻找目标节点相关的直系
     * @param res
     * @param componentId
     * @param tree
     */
    private void findTargetParentComponent(List<ComponentListVO> res, Long componentId, List<ComponentListVO> tree) {
        if (!dfs(res, tree, componentId)){
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
    }

    /**
     * 深度优先寻找目标组件的所有直系
     * @param res
     * @param componentListVOList
     * @param componentId
     * @return
     */
    private Boolean dfs(List<ComponentListVO> res, List<ComponentListVO> componentListVOList, Long componentId){
        if (CollectionUtil.isEmpty(componentListVOList)){
            return false;
        }
        for (ComponentListVO componentListVO : componentListVOList) {
            res.add(componentListVO);
            if (Objects.equals(componentListVO.getId(), componentId)){
                return true;
            }
            if (dfs(res, componentListVO.getChildren(), componentId)){
                return true;
            }
            res.remove(res.size() - 1);
        }
        return false;
    }
}
